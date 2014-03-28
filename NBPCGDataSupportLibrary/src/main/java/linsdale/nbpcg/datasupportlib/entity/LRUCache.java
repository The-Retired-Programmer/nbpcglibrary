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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import linsdale.nbpcg.supportlib.Log;

/**
 * Simple implementation of an LRU cache
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
public class LRUCache<E> extends LinkedHashMap<Integer, E> {

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

    /**
     * test if eldest (based on recent use) entry in cache should be removed
     *
     * @param eldest the eldest entry
     * @return true if max cache size has be reached and eldest should be
     * removed
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, E> eldest) {
        if (size() > maxcache) {
            Log.get("linsdale.nbpcg.datacache").log(Level.FINER, "Cache Free {0}({1}): LRU cache", new Object[]{name, eldest.getKey()});
            return true;
        }
        return false;
    }
}