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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import linsdale.nbpcg.annotations.RegisterLog;
import linsdale.nbpcg.datasupportlib.dataaccess.DataAccessRO;
import linsdale.nbpcg.supportlib.Log;
import linsdale.nbpcg.supportlib.LogicException;

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
 * reads by holding references to these recently touched objects.
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
@RegisterLog("linsdale.nbpcg.entitymanager")
abstract public class EntityManagerRO<E extends EntityRO> {

    private static final int MAXLRUCACHE = 10;
    protected final String name;
    protected final Map<Integer, SoftReference<E>> cache;
    private final ReferenceQueue<E> refqueue;
    protected final LRUCache<E> lrucache;
    private DataAccessRO dataAccess;

    /**
     * Constructor - defines cache name (for reporting) has default max cache
     * size
     *
     * @param name
     */
    public EntityManagerRO(String name) {
        this(name, MAXLRUCACHE);
    }

    /**
     * Constructor - defines cache name (for reporting) and max LRU cache size
     *
     * @param name
     * @param maxLRUcache
     */
    public EntityManagerRO(String name, int maxLRUcache) {
        super();
        this.name = name;
        lrucache = new LRUCache<>(name, maxLRUcache);
        cache = new HashMap<>();
        refqueue = new ReferenceQueue<>();
    }

    public final synchronized E get(int id) {
        if (id <= 0) {
            throw new LogicException("Cache Get() Failure (class=" + name + ";id=" + id + ")");
        }
        freeReleasedEntries();
        E e = lrucache.get(id);
        if (e != null) {
            Log.get("linsdale.nbpcg.entitymanager").log(Level.FINEST, "Cache Get {0}({1}): hit on LRUCache", new Object[]{name, id});
            return e;
        }
        // not in lru cache - now look up the entity in the cache
        SoftReference<E> ref = cache.get(id);
        if (ref != null) {
            e = ref.get();
            if (e != null) {
                lrucache.put(id, e); // insert object into LRU cache
                Log.get("linsdale.nbpcg.entitymanager").log(Level.FINEST, "Cache Get {0}({1}): hit on Cache (& reinserted into LRU cache)", new Object[]{name, id});
                return e;
            } else {
                Log.get("linsdale.nbpcg.entitymanager").log(Level.FINEST, "Cache Get {0}({1}): miss on Cache (SoftReference clear)", new Object[]{name, id});
            }
        } else {
            Log.get("linsdale.nbpcg.entitymanager").log(Level.FINEST, "Cache Get {0}({1}): miss on Cache", new Object[]{name, id});
        }
        Log.get("linsdale.nbpcg.entitymanager").log(Level.FINER, "Create New {0} Entity (id = {1})", new Object[]{name, id});
        e = _createNewEntity(id);
        e.load(id);
        insertIntoCache(id, e);
        return e;
    }
    
    abstract protected E _createNewEntity(int id);

    private void freeReleasedEntries() {
        Reference<? extends E> r;
        while ((r = (Reference<? extends E>) refqueue.poll()) != null) {
            int id = r.get().getId();
            cache.remove(id);
            Log.get("linsdale.nbpcg.entitymanager").log(Level.FINER, "Cache Free {0}({1}): SoftReference cache", new Object[]{name, id});
        }
    }
    
    protected void insertIntoCache(int id, E e) {
        lrucache.put(id, e);
        cache.put(id, new SoftReference<>(e));
    }
    
    protected synchronized void removeFromCache(E e) {
        int id = e.getId();
        if (id > 0 ) {
            lrucache.remove(id);
            cache.remove(id);
            Log.get("linsdale.nbpcg.entitymanager").log(Level.FINEST, "Cache Remove {0}({1})", new Object[]{name, id});
            return;
        }
        throw new LogicException("Remove from Cache Failure (class=" + name + ";id=" + id +")");
    }
    
    // and the service function to provide access to the data access object for the entity class
    
    public DataAccessRO getDataAccess() {
        if (dataAccess == null) {
            dataAccess = _createDataAccess();
        }
        return dataAccess;
    }

    abstract protected DataAccessRO _createDataAccess();
}