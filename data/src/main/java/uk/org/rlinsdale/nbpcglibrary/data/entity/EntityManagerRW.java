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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceManager;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;

/**
 * Entity Manager for an Entity Class.
 *
 * Adds a transient cache to hold all new entity objects which have been created
 * and not yet persisted
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class being managed
 * @param <P> the Parent Entity Class
 */
public abstract class EntityManagerRW<E extends EntityRW, P extends Entity> extends EntityManagerRO<E> {

    private final Map<Integer, E> transientCache;
    private int nextId = -1;
    private EntityPersistenceManager entityPersistenceManager;

    /**
     * Constructor
     *
     * @param name the entity name
     */
    public EntityManagerRW(String name) {
        super(name);
        transientCache = new HashMap<>();
    }

    /**
     * Constructor
     *
     * @param name the entity name
     * @param maxLRUcache the maximum size of the LRU cache
     */
    public EntityManagerRW(String name, int maxLRUcache) {
        super(name, maxLRUcache);
        transientCache = new HashMap<>();
    }

    /**
     * Create a new entity (initialised)
     *
     * @return the new entity
     * @throws java.io.IOException
     */
    public final synchronized E getNew() throws IOException {
        LogBuilder.writeLog("nbpcglibrary.data", this, "getNew", nextId);
        E e = createNewEntity(nextId);
        e.setId(nextId);
        transientCache.put(nextId--, e);
        return e;
    }

    /**
     * Create a new entity (initialised) and link it as child of a parent
     * entity.
     *
     * @param parent the parent entity
     * @return the new entity
     * @throws java.io.IOException
     */
    public final synchronized E getNew(P parent) throws IOException {
        E e = getNew();
        link2parent(e, parent);
        return e;
    }

    /**
     * Link a child entity to its parent.
     *
     * @param e the child entity
     * @param rs the parent entity
     * @throws java.io.IOException
     */
    abstract protected void link2parent(E e, P rs) throws IOException;

    /**
     * Create a new entity, copy it's field from another entity and link it as
     * child of a parent entity.
     *
     * @param from the copy source entity
     * @param parent the parent entity
     * @return the new entity
     * @throws java.io.IOException
     */
    public final synchronized E getNew(E from, P parent) throws IOException {
        E e = getNew(parent);
        e.copy(from);
        return e;
    }

    /**
     * Remove from Transient Cache.
     *
     * @param e the entity
     */
    protected final synchronized void removeFromTransientCache(E e) {
        int id = e.getId();
        if (transientCache.containsKey(id)) {
            transientCache.remove(id);
            LogBuilder.writeLog("nbpcglibrary.data", this, "removeFromTransientCache", id);
            return;
        }
        throw new LogicException("Remove Transient Failure (class=" + name + ";id=" + id + ")");
    }

    /**
     * Persist a Transient Cache Entity into standard persistance cache.
     *
     * @param e the entity
     * @param newId the new entity Id
     */
    protected synchronized void persistTransient(E e, int newId) {
        int id = e.getId();
        if (newId > 0 && id < 0 && transientCache.containsKey(id)) {
            transientCache.remove(id);
            e.updateId(newId);
            insertIntoCache(newId, e);
            LogBuilder.writeLog("nbpcglibrary.data", this, "persistTransient", id, "as", newId);
            return;
        }
        throw new LogicException("Persist Transient Failure (class=" + name + ";id=" + id + ";newid=" + newId + ")");
    }

    @Override
    public final EntityPersistenceManager getEntityPersistenceManager() {
        if (entityPersistenceManager == null) {
                entityPersistenceManager = createEntityPersistenceManager();
        }
        return entityPersistenceManager;
    }

    @Override
    abstract protected EntityPersistenceManager createEntityPersistenceManager();
}
