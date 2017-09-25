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

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache for Basic Entity Class.
 *
 * Implements a Cache for basic entity objects.
 *
 * Implemented as:
 *
 * 1) a softreference cache to ensure that all code is referring to a single
 * shared entity (and not each holding their own unique copy.)
 *
 * 2) a small LRU cache is implemented in parallel to reduce possible database
 * reads by holding references to those recently touched objects.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> The Basic Entity Class being managed
 */
public class BasicEntityCache<E extends IdTimestampBaseEntity> {

    private static final int MAXLRUCACHE = 10;

    /**
     * The entity cache.
     */
    private final Map<Integer, SoftReference<E>> cache;

    /**
     * The LRU cache.
     */
    private final LRUCache<E> lrucache;

    /**
     * Constructor.
     *
     */
    public BasicEntityCache() {
        lrucache = new LRUCache<>(MAXLRUCACHE);
        cache = new HashMap<>();
    }

    /**
     * Get an Entity
     *
     * Lookup cache.
     *
     * @param id the id
     * @return the entity
     */
    public final synchronized E get(int id) {
        // lookup LRU cache
        E e = lrucache.get(id);
        if (e != null) {
            return e;
        }
        // lookup the softreference cache
        if (cache.containsKey(id)) {
            SoftReference<E> ref = cache.get(id);
            if (ref != null) {
                e = ref.get();
                if (e == null) {
                    cache.remove(id);
                } else {
                    lrucache.put(id, e); // insert object into LRU cache
                    return e;
                }
            } else {
                cache.remove(id);
            }
        }
        return null;
    }

    /**
     * Insert an entity into the cache.
     *
     * @param e the entity
     */
    public final synchronized void insert(E e) {
        int id = e.getId();
        lrucache.put(id, e);
        cache.put(id, new SoftReference<>(e));
    }
    
    /**
     * Remove an entity from the cache.
     *
     * @param id the entity id
     */
    public final synchronized void remove(int id) {
        lrucache.remove(id);
        cache.remove(id);
    }
}
