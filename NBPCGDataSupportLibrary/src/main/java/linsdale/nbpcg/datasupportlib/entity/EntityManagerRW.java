/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package linsdale.nbpcg.datasupportlib.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import linsdale.nbpcg.datasupportlib.dataaccess.DataAccessRW;
import linsdale.nbpcg.supportlib.Log;
import linsdale.nbpcg.supportlib.LogicException;

/**
 * Entity Manager for an Entity Class.
 *
 * adds a transient cache to hold all new entity objects which have been created
 * and not yet persisted
 * 
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
public abstract class EntityManagerRW<E extends EntityRW> extends EntityManagerRO<E> {

    private final Map<Integer, E> transientCache;
    private int nextId = -1;
    private DataAccessRW dataAccess;
    
    /**
     * Constructor - defines cache name (for reporting) has default max cache
     * size
     *
     * @param name
     */
    public EntityManagerRW(String name) {
        super(name);
        transientCache = new HashMap<>();
    }

    public EntityManagerRW(String name, int maxLRUcache) {
        super(name, maxLRUcache);
        transientCache = new HashMap<>();
    }

    public final synchronized E getNew() {
        Log.get("linsdale.nbpcg.entitymanager").log(Level.FINER, "Create New {0} Entity (id = {1})", new Object[]{name, nextId});
        E e = _createNewEntity(nextId);
        e.setId(nextId);
        transientCache.put(nextId--, e);
        return e;
    }
    
    public final synchronized E getNew(Entity parent) {
        E e = getNew();
        _link2parent(e, parent);
        return e;
    }
    
    abstract protected void _link2parent(E e, Entity rs);
    
    public final synchronized E getNew(E from, Entity parent) {
        E e = getNew(parent);
        e.copy(from);
        return e;
    }
    
    protected final synchronized void removeFromTransientCache(E e) {
        int id = e.getId();
        if (transientCache.containsKey(id)) {
            transientCache.remove(id);
            Log.get("linsdale.nbpcg.entitymanager").log(Level.FINEST, "Transient Cache Remove {0}({1})", new Object[]{name, e.getId()});
            return;
        }
        throw new LogicException("Remove Transient Failure (class=" + name + ";id=" + id + ")");
    }

    protected synchronized void persistTransient(E e, int newId) {
        int id = e.getId();
        if (newId > 0 && id < 0 && transientCache.containsKey(id)) {
            transientCache.remove(id);
            e.updateId(newId);
            insertIntoCache(newId,e);
            Log.get("linsdale.nbpcg.entitymanager").log(Level.FINEST, "Persist (Transient Cache to Cache) {0}({1} as {2})", new Object[]{name, id, newId});
            return;
        }
        throw new LogicException("Persist Transient Failure (class=" + name + ";id=" + id + ";newid=" + newId+")");
    }

    // and the service function to provide access to the data access object for the entity class
    
    @Override
    public final DataAccessRW getDataAccess() {
        if (dataAccess == null) {
            dataAccess = _createDataAccess();
        }
        return dataAccess;
    }

    @Override
    abstract protected DataAccessRW _createDataAccess();
}