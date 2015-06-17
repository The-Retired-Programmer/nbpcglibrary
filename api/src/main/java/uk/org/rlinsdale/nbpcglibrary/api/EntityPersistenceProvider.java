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

import java.util.List;

/**
 * Provider of a EntityPersistence Service for a particular entity
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * 
 * @param <K> the Primary Key type
 */
public interface EntityPersistenceProvider<K> extends HasInstanceDescription {

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
