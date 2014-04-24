/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package linsdale.nbpcg.datasupportlib.dbfields;

import java.util.Map;
import linsdale.nbpcg.datasupportlib.entity.EntityRW;

/**
 * Interface for handling entity field states for a read-write entity.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the entity class
 */
public interface DBFieldsRW<E extends EntityRW> extends DBFieldsRO {

    /**
     * Get a collection of all fields (and their values), whose data values have
     * changed.
     *
     * @param map the map of names and values
     */
    public void diffs(Map<String, Object> map);

    /**
     * Get a collection of all fields and their values.
     *
     * @param map the map of names and values
     */
    public void values(Map<String, Object> map);

    /**
     * Copy data from given Entity into these entity fields.
     *
     * @param source the source entity
     */
    public void copy(E source);
}
