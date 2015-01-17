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
import uk.org.rlinsdale.nbpcglibrary.common.LogHelper;
import uk.org.rlinsdale.nbpcglibrary.data.dataaccess.DataAccessRO;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;

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
 * @param <E> The Entity Class being managed
 */
abstract public class EntityManagerRO<E extends EntityRO> implements LogHelper {

    private static final int MAXLRUCACHE = 10;

    /**
     * The entity name.
     */
    protected final String name;

    /**
     * The entity cache.
     */
    protected final Map<Integer, SoftReference<E>> cache;
    private final ReferenceQueue<E> refqueue;

    /**
     * The LRU cache.
     */
    protected final LRUCache<E> lrucache;
    private DataAccessRO dataAccess;

    /**
     * Constructor.
     *
     * @param name the entity name
     */
    public EntityManagerRO(String name) {
        this(name, MAXLRUCACHE);
    }

    /**
     * Constructor.
     *
     * @param name the entity name
     * @param maxLRUcache the max size of the LRU cache (entities)
     */
    public EntityManagerRO(String name, int maxLRUcache) {
        super();
        this.name = name;
        lrucache = new LRUCache<>(name, maxLRUcache);
        cache = new HashMap<>();
        refqueue = new ReferenceQueue<>();
    }

    @Override
    public String classDescription() {
        return LogBuilder.classDescription(this, name);
    }

    /**
     * Get an Entity. Lookup caches and if not present then create a new entity
     * and load it using data obtained from entity storage.
     *
     * @param id the entity Id
     * @return the entity
     */
    public final synchronized E get(int id) {
        if (id <= 0) {
            throw new LogicException("Cache Get() Failure (class=" + name + ";id=" + id + ")");
        }
        freeReleasedEntries();
        E e = lrucache.get(id);
        if (e != null) {
            LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "get", id)
                    .addMsg("hit on LRUCache for {0}", e.classDescription()).write();
            return e;
        }
        // not in lru cache - now look up the entity in the cache
        SoftReference<E> ref = cache.get(id);
        if (ref != null) {
            e = ref.get();
            if (e != null) {
                lrucache.put(id, e); // insert object into LRU cache
                LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "get", id)
                        .addMsg("hit on Cache (& reinserted into LRU cache) for {0}", e.classDescription()).write();
                return e;
            } else {
                LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "get", id)
                        .addMsg("miss on Cache (SoftReference clear)").write();
            }
        } else {
            LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "get", id)
                    .addMsg("miss on Cache").write();
        }
        e = _createNewEntity(id);
        e.load(id);
        insertIntoCache(id, e);
        LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "get", id)
                .addMsg("create new Entity {0} (and insert into Cache)", e.classDescription()).write();
        return e;
    }

    /**
     * Create a new Entity. Does not load entity data into the entity.
     *
     * @param id the entity id
     * @return the created entity
     */
    abstract protected E _createNewEntity(int id);

    private void freeReleasedEntries() {
        Reference<? extends E> r;
        while ((r = (Reference<? extends E>) refqueue.poll()) != null) {
            int id = r.get().getId();
            cache.remove(id);
            LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "freeReleasedEntries")
                    .addMsg("Cache Free ({0}) (SoftReference cache)", id).write();
        }
    }

    /**
     * Insert an entity into the caches.
     *
     * @param id the entity Id
     * @param e the entity
     */
    protected void insertIntoCache(int id, E e) {
        lrucache.put(id, e);
        cache.put(id, new SoftReference<>(e));
    }

    /**
     * Remove an entity from the caches.
     *
     * @param e the entity
     */
    protected synchronized void removeFromCache(E e) {
        int id = e.getId();
        if (id > 0) {
            lrucache.remove(id);
            cache.remove(id);
            LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMethodName(this, "removeFromCache", e)
                    .addMsg("Cache Remove {0}", e.classDescription()).write();
            return;
        }
        throw new LogicException("Remove from Cache Failure (class=" + name + ";id=" + id + ")");
    }

    // and the service function to provide access to the data access object for the entity class
    /**
     * Get the DataAccess Object for this Entity Class.
     *
     * @return the DataAccess object
     */
    public DataAccessRO getDataAccess() {
        if (dataAccess == null) {
            dataAccess = _createDataAccess();
        }
        return dataAccess;
    }

    /**
     * Create the DataAccess Object for this Entity Class.
     *
     * @return the DataAccess object
     */
    abstract protected DataAccessRO _createDataAccess();
}
