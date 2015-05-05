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
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T>
 */
public interface BasicEntity<T extends BasicEntity> {

    /**
     *
     * @param key
     * @param value
     * @throws JsonConversionException
     */
    public void setField(String key, JsonValue value) throws JsonConversionException;

    /**
     *
     * @param generator
     * @param key
     * @throws JsonConversionException
     */
    public void writeField(JsonGenerator generator, String key) throws JsonConversionException;

    /**
     *
     * @param generator
     */
    public void writePK(JsonGenerator generator);

    /**
     *
     * @param generator
     */
    public void writePKwithkey(JsonGenerator generator);

    /**
     *
     * @param generator
     * @param label
     */
    public void writeAllFields(JsonGenerator generator, String label);
}
