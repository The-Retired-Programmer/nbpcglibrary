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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;

/**
 * Simple implementation of an LRU cache
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> the Type of the Primary Key
 * @param <E> The entity Class being managed
 * 
 */
public class LRUCache<K, E> extends LinkedHashMap<K, E> implements HasInstanceDescription {

    private static final float LOADFACTOR = (float) 0.9;
    private final int maxcache;
    private final String name;

    /**
     * Constructor - defines cache name (for reporting) and max cache size
     *
     * @param name the name of the cache (used with reporting/logging)
     * @param maxcache the max number of entities stored in cache - after this
     * limit is reached the oldest will be dropped
     */
    public LRUCache(String name, int maxcache) {
        super((int) (maxcache / LOADFACTOR), LOADFACTOR, true);
        this.maxcache = maxcache;
        this.name = name;
    }
    
    
    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, name);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, E> eldest) {
        if (size() > maxcache) {
            LogBuilder.create("nbpcglibrary.data", Level.FINER).addMethodName(this, "removeEldestEntry")
                .addMsg("frees {0}", eldest.getKey()).write();
            return true;
        }
        return false;
    }
}