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
package uk.org.rlinsdale.nbpcglibrary.api;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Provider of a EntityPersistence Service for a particular entity
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
