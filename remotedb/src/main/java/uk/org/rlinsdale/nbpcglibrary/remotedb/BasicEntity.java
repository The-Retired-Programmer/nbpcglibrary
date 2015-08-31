/*
 * Copyright (C) 2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package uk.org.rlinsdale.nbpcglibrary.remotedb;

import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import uk.org.rlinsdale.nbpcglibrary.json.JsonConversionException;

/**
 * The basic Entity interface. This is incremental to the standards setters and
 * getters which are normally required (for JPA for example).
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the entity class
 */
public abstract class BasicEntity<T extends BasicEntity> {

    /**
     * set a field.
     *
     * @param key the name of the field
     * @param value the value to insert
     * @throws JsonConversionException if parsing problems
     */
    public abstract void setField(String key, JsonValue value) throws JsonConversionException;

    /**
     * Write a field to the JsonObject
     *
     * @param generator the JsonGenerator being used to build the JsonObject
     * @param key the name of the field
     * @throws JsonConversionException if parsing problems
     */
    public abstract void writeField(JsonGenerator generator, String key) throws JsonConversionException;

    /**
     * Write the primary Key value to the JsonArray
     *
     * @param generator the JsonGenerator being used to build the JsonArray
     */
    public abstract void writePK(JsonGenerator generator);

    /**
     * Write the primary Key name and value to the JsonObject
     *
     * @param generator the JsonGenerator being used to build the JsonObject
     */
    public abstract void writePKwithkey(JsonGenerator generator);

    /**
     * Write all fields as a jsonObject
     *
     * @param generator the JsonGenerator being used to build the JsonObject
     * @param label the JsonObject's label
     */
    public abstract void writeAllFields(JsonGenerator generator, String label);
    
    /**
     * Set the standard field prior to persisting a newly created entity.
     * 
     * @param user the usercode to be attached (as required)
     */
    public void setFieldsPreInsert(String user) {
    }
    
    /**
     * Set the standard field prior to persisting an updated entity.
     * 
     * @param user the usercode to be attached (as required)
     */
    public void setFieldsPreUpdate(String user) {
    }
}
