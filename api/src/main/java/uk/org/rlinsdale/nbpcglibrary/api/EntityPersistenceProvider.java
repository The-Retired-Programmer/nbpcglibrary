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
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * Provider of a EntityPersistence Service for a particular entity
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public interface EntityPersistenceProvider extends HasInstanceDescription {

    /**
     * Get the set of entity Ids for all stored entities.
     *
     * @return the set of entity Ids
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public JsonArray find() throws IOException;

    /**
     * Get the entity Id(s) for a many (0 to many) entities - using selected by an
     * column filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the set of entity Ids - using selected by an column filter.
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public JsonArray find(String parametername, JsonValue parametervalue) throws IOException;

    /**
     * Get entity Id for a single entity - using selected by an column filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the entity object representation
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public JsonValue findOne(String parametername, JsonValue parametervalue) throws IOException;

    /**
     * Get the set of entity for all stored entities.
     *
     * @return the set of entities
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public JsonArray get() throws IOException;

    /**
     * Get entity data for a many (0 to many) entities - using selected by an
     * column filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the array of entity data objects
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public JsonArray get(String parametername, JsonValue parametervalue) throws IOException;

    /**
     * Get entity data for a single entity - using selected by an column filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the entity object representation
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public JsonObject getOne(String parametername, JsonValue parametervalue) throws IOException;

    /**
     * Get the next index value for entities which have an explicit ordering
     * column defined.
     *
     * @return the next index value
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public int findNextIdx() throws IOException;

    /**
     * Get entity data - using PK lookup
     *
     * @param id the entity Id
     * @return the JsonObject containing field values
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public JsonObject get(int id) throws IOException;

    /**
     * Insert a new entity (set of values) into entity storage.
     *
     * @param values the set of values
     * @return the new entity Id
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public int insert(JsonObject values) throws IOException;

    /**
     * Update an existing entity in entity storage with a new set of values.
     *
     * @param id the new entity Id
     * @param diff the set of values to be updated
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public void update(int id, JsonObject diff) throws IOException;

    /**
     * Delete an entity from entity storage.
     *
     * @param id the entity Id
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public void delete(int id) throws IOException;
}
