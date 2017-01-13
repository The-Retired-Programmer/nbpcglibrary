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

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Provider of a EntityPersistence Service for a particular entity
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * 
 * @param <K> the Primary Key type
 */
public interface EntityPersistenceProvider<K> extends HasInstanceDescription {
    
    /**
     * Initialise this provider.
     * 
     * @param tablename the entity table name in entity storage
     * @param properties the properties used for configuration
     * @param pup the PersistenceUnitProvider
     * @throws IOException if problem
     */
    public void init(String tablename, Properties properties, PersistenceUnitProvider pup) throws IOException;
    
    /**
     * Initialise the provider
     *
     * @param tablename the entity table name in entity storage
     * @param idx the index field - used to order the returned entities
     * @param properties the properties used for configuration
     * @param pup the PersistenceUnitProvider
     * @throws IOException if problem
     */
    public void init(String tablename, String idx, Properties properties, PersistenceUnitProvider pup) throws IOException;

    /**
     * Close down the provider.
     */
    public void close();
    
    /**
     * Get the primary key from a entity described by entityfields
     *
     * @param ef the entity fields
     * @return the primary Key
     */
    public K getPK(EntityFields ef);
    
    /**
     * create an auto generated primary key (if required)
     *
     * @param ef the entity fields into which the new primary key can be added
     */
    public void autoGenPrimaryKeyHook(EntityFields ef);
    
    /**
     * Add the timestamp info for this entity
     * 
     * @param ef the entity fields
     */
    public void addTimestampInfo(EntityFields ef);
    
    /**
     * Update the timestamp info for this entity
     * 
     * @param ef the entity fields
     */
    public void updateTimestampInfo(EntityFields ef);
            
    /**
     * Get the set of entity Primary Keys for all stored entities.
     *
     * @return the set of entity primary keys.
     */
    public List<K> find();

    /**
     * Get the entity primary keys for a many (0 to many) entities - using selected by an
     * column filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the set of entity primary keys - using selected by an column filter.
     */
    public List<K> find(String parametername, Object parametervalue);

    /**
     * Get entity primary key for a single entity - using selected by an column filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the entity primary key
     */
    public K findOne(String parametername, Object parametervalue);

   /**
     * Get the set of entity for all stored entities.
     *
     * @return the set of entities
     */
    public List<EntityFields> get();

    /**
     * Get entity data for a many (0 to many) entities - using selected by an
     * column filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the array of entity data objects
     */
    public List<EntityFields> get(String parametername, Object parametervalue);
    
    /**
     * Get entity data for a single entity - using selected by an column filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the entity object representation
     */
    public EntityFields getOne(String parametername, Object parametervalue);

    /**
     * Get the next index value for entities which have an explicit ordering
     * column defined.
     *
     * @return the next index value
     */
    public int findNextIdx();

    /**
     * Get entity data - using primary key
     *
     * @param pk the entity primary key
     * @return the Object containing field values
     */
    public EntityFields get(K pk);

    /**
     * Insert a new entity (set of values) into entity storage.
     *
     * @param values the set of values
     * @return the full set of entity fields
     */
    public EntityFields insert(EntityFields values);

    /**
     * Update an existing entity in entity storage with a new set of values.
     *
     * @param pk the entity primary key
     * @param diff the set of values to be updated
     * @return the full set of entity fields
     */
    public EntityFields update(K pk, EntityFields diff);

    /**
     * Delete an entity from entity storage.
     *
     * @param pk the entity primary key
     */
    public void delete(K pk) ;
}
