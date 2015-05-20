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
package uk.org.rlinsdale.nbpcglibrary.data.dbfields;

import java.io.IOException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;

/**
 * Interface for handling entity field states for an entity.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity this is being applied to
 */
public interface DBFields<E extends Entity> {

    /**
     * Restore the State (from the last SaveState())
     */
    public void restoreState();

    /**
     * Save the State.
     */
    public void saveState();

    /**
     * Use the Json formatted to insert the data into the entity
     * fields.
     *
     * @param data the Json formatted data
     * @throws IOException if problems in parsing the json object
     */
    public void load(JsonObject data) throws IOException;
    
    /**
     * Get a collection of all fields (and their values), whose data values have
     * changed.
     *
     * @param job a JasonObjectBuilder into which names and values can be inserted
     * @throws IOException if problems in creating the json  object
     */
    public void diffs(JsonObjectBuilder job) throws IOException;

    /**
     * Get a collection of all fields and their values.
     *
     * @param job a JasonObjectBuilder into which names and values can be inserted
     * @throws IOException if problems in creating the json  object
     */
    public void values(JsonObjectBuilder job) throws IOException;

    /**
     * Copy data from given Entity into these entity fields.
     *
     * @param source the source entity
     */
    public void copy(E source);
}
