/*
 * Copyright 2017 richard linsdale.
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

import java.util.List;
import java.util.Map;

/**
 * The Rest interface to interface with Rest services, including permanent
 * storage solutions - databases, html rest services etc.
 *
 * @author richard linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the entity class being transferred
 */
public interface Rest<E> {

    /**
     * Open the Rest Stream.
     *
     * used to create any pooled resourses etc which may be needed to support
     * the Rest calls.
     *
     * @return true if stream is opened without problems
     */
    public boolean open();

    /**
     * Close the Rest Stream.
     *
     * used to close any pooled resources, save any cached information etc which
     * may be needed to support the Rest calls.
     *
     * @return true if stream is closed without problems.
     */
    public boolean close();

    /**
     * Get an entity
     *
     * @param id the id for the entity
     * @return the entity or null if a problem
     */
    public E get(int id);

    /**
     * Get a list of Entities
     *
     * @return the entity list or null if a problem
     */
    public List<E> getAll();
    
    /**
     * Get a list of Entities
     *
     * @param filtername the name of the filter item to be applied
     * @param filtervalue the filter value - if matched then include in list
     * @return the entity list or null if a problem
     */
    public List<E> getMany(String filtername, int filtervalue);

    /**
     * Create a new entity
     *
     * @param entity the entity being saved
     * @return the created entity as now stored in the permanent store or null
     * if a problem
     */
    public E create(E entity);

    /**
     * Update an entity, replacing it entirely with the provided entity. Will do
     * a create if the request URI does not exist.
     *
     * @param id the id of the entity to be updated
     * @param entity the entity to be used for the update
     * @return the updated/created entity as now stored in the permanent store
     * or null if a problem
     */
    public E update(int id, E entity);

    /**
     * Patch an existing entity.
     *
     * @param id the id of the entity to be patched
     * @param patches a map, containing field names and values which are to be
     * updated
     * @return the updated entity as now stored in the permanent store or null
     * if a problem
     */
    public E patch(int id, Map<String, Object> patches);

    /**
     * Delete an existing entity
     *
     * @param id the id of the entity to be deleted
     * @return true if deleted, false if a problem
     */
    public boolean delete(int id);

}
