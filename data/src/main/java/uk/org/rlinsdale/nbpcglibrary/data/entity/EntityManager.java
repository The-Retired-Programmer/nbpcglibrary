/*
 * Copyright (C) 2014-2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package uk.org.rlinsdale.nbpcglibrary.data.entity;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProvider;
import uk.org.rlinsdale.nbpcglibrary.api.LogicException;

/**
 * Entity Manager for an Entity Class.
 *
 * Implements a Cache for entity objects, plus capability to create new objects.
 *
 * Implemented as:
 *
 * 1) a softreference cache to ensure that all code is referring to a single
 * shared entity (and not each holding their own unique copy.)
 *
 * 2) a small LRU cache is implemented in parallel to reduce possible database
 * reads by holding references to those recently touched objects.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> the type of the Primary Key
 * @param <E> The Entity Class being managed
 * @param <P> The Parent Entity Class
 */
abstract public class EntityManager<K, E extends Entity<K, E, P, ?>, P extends CoreEntity> implements HasInstanceDescription {

    private static final int MAXLRUCACHE = 10;

    /**
     * The entity name.
     */
    protected final String name;

    /**
     * The entity cache.
     */
    protected final Map<K, SoftReference<E>> cache;
    private final ReferenceQueue<E> refqueue;

    /**
     * The LRU cache.
     */
    protected final LRUCache<K, E> lrucache;
    private EntityPersistenceProvider<K> entityPersistenceProvider;
    private final Map<K, E> transientCache;

    /**
     * Constructor.
     *
     * @param name the entity name
     */
    public EntityManager(String name) {
        this(name, MAXLRUCACHE);
    }

    /**
     * Constructor.
     *
     * @param name the entity name
     * @param maxLRUcache the max size of the LRU cache (entities)
     */
    public EntityManager(String name, int maxLRUcache) {
        super();
        this.name = name;
        lrucache = new LRUCache<>(name, maxLRUcache);
        cache = new HashMap<>();
        refqueue = new ReferenceQueue<>();
        transientCache = new HashMap<>();
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, name);
    }

    /**
     * Get an Entity. Lookup caches and if not present then create a new entity
     * and load it using data obtained from entity storage.
     *
     * @param pk the primary key value
     * @return the entity
     */
    public final synchronized E get(K pk) {
        if (isPersistent(pk)) {
            freeReleasedEntries();
            E e = lrucache.get(pk);
            if (e != null) {
                LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "get", pk)
                        .addMsg("hit on LRUCache for {0}", e.instanceDescription()).write();
                return e;
            }
            // not in lru cache - now look up the entity in the cache
            SoftReference<E> ref = cache.get(pk);
            if (ref != null) {
                e = ref.get();
                if (e != null) {
                    lrucache.put(pk, e); // insert object into LRU cache
                    LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "get", pk)
                            .addMsg("hit on Cache (& reinserted into LRU cache) for {0}", e.instanceDescription()).write();
                    return e;
                } else {
                    LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "get", pk)
                            .addMsg("miss on Cache (SoftReference clear)").write();
                }
            } else {
                LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "get", pk)
                        .addMsg("miss on Cache").write();
            }
            e = createNewEntity(pk);
            e.load(pk);
            insertIntoCache(pk, e);
            LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "get", pk)
                    .addMsg("create new Entity {0} (and insert into Cache)", e.instanceDescription()).write();
            return e;
        } else {
            E e = transientCache.get(pk);
            if (e == null) {
                throw new LogicException("Can't find transient entry in cache");
            }
            LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "get", pk)
                    .addMsg("hit on Transient Cache for {0}", e.instanceDescription()).write();
            return e;
        }
    }

    /**
     * Create a new Entity. Does not load entity data into the entity. this will
     * be a transient entity initially until it is persisted.
     *
     * @return the created entity
     */
    abstract protected E createNewEntity();

    /**
     * Test if given primary key is transient or persistent
     *
     * @param pkey the primary key value
     * @return true if persistent primary key
     */
    abstract protected boolean isPersistent(K pkey);

    /**
     * Create an Entity. Does not load entity data into the entity. this will be
     * initialise with its primary key initially.
     *
     * @param pk the primary key for this entity
     * @return the created entity
     */
    abstract protected E createNewEntity(K pk);

    private void freeReleasedEntries() {
        Reference<? extends E> r;
        while ((r = (Reference<? extends E>) refqueue.poll()) != null) {
            K pk = r.get().getPK();
            cache.remove(pk);
            LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "freeReleasedEntries")
                    .addMsg("Cache Free ({0}) (SoftReference cache)", pk).write();
        }
    }

    /**
     * Insert an entity into the caches.
     *
     * @param pk the primary key
     * @param e the entity
     */
    protected void insertIntoCache(K pk, E e) {
        lrucache.put(pk, e);
        cache.put(pk, new SoftReference<>(e));
    }

    /**
     * Remove an entity from the caches.
     *
     * @param e the entity
     */
    protected synchronized void removeFromCache(E e) {
        K pk = e.getPK();
        if (e.isPersistent()) {
            lrucache.remove(pk);
            cache.remove(pk);
            LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "removeFromCache", e)
                    .addMsg("Cache Remove {0}", e.instanceDescription()).write();
        } else {
            transientCache.remove(pk);
            LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "removeFromCache", e)
                    .addMsg("Transient Cache Remove {0}", e.instanceDescription()).write();
        }
    }

    /**
     * Create a new entity (initialised)
     *
     * @return the new entity
     */
    public final synchronized E getNew() {
        E e = createNewEntity();
        K pk = e.getPK();
        LogBuilder.writeLog("nbpcglibrary.data", this, "getNew", pk);
        transientCache.put(pk, e);
        return e;
    }
    
    /**
     * Create a new entity (initialised) and link it as child of a parent
     * entity.
     *
     * @param parent the parent entity
     * @return the new entity
     */
    public final synchronized E getNew(P parent) {
        E e = getNew();
        link2parent(e, parent);
        return e;
    }
    
    /**
     * Create a new entity, copy it's field from another entity and link it as
     * child of a parent entity.
     *
     * @param from the copy source entity
     * @param parent the parent entity
     * @return the new entity
     */
    public final synchronized E getNew(E from, P parent) {
        E e = getNew(parent);
        e.copy(from);
        return e;
    }
    
    /**
     * Get the EntityPersistenceProvider for this Entity Class.
     *
     * @return the EntityPersistenceProvider
     */
    public EntityPersistenceProvider<K> getEntityPersistenceProvider() {
        if (entityPersistenceProvider == null) {
            entityPersistenceProvider = createEntityPersistenceProvider();
        }
        return entityPersistenceProvider;
    }

    /**
     * Create the EntityPersistenceProvider for this Entity Class.
     *
     * @return the EntityPersistenceProvider
     */
    abstract protected EntityPersistenceProvider<K> createEntityPersistenceProvider();

    /**
     * Link a child entity to its parent.
     *
     * @param e the child entity
     * @param p the parent entity
     */
    abstract protected void link2parent(E e, P p);
}
