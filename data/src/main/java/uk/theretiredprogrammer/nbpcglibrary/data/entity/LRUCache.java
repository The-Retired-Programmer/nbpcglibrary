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
package uk.theretiredprogrammer.nbpcglibrary.data.entity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.api.HasInstanceDescription;

/**
 * Simple implementation of an LRU cache
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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