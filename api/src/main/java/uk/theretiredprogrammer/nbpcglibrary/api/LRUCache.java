/*
 * Copyright 2014-2017 Richard Linsdale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.nbpcglibrary.api;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple implementation of an LRU cache
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> The entity Class being managed
 * 
 */
public class LRUCache<E> extends LinkedHashMap<Integer, E> {

    private static final float LOADFACTOR = (float) 0.9;
    private final int maxcache;

    /**
     * Constructor - defines cache name (for reporting) and max cache size
     *
     * @param maxcache the max number of entities stored in cache - after this
     * limit is reached the oldest will be dropped
     */
    public LRUCache(int maxcache) {
        super((int) (maxcache / LOADFACTOR), LOADFACTOR, true);
        this.maxcache = maxcache;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, E> eldest) {
        return size() > maxcache;
    }
}