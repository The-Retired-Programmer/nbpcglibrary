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
package uk.org.rlinsdale.nbpcg.datasupportlib.dataaccess;

import java.util.Map;

/**
 * Data Access Interface - for a Read-Write Entity.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public interface DataAccessRW extends DataAccessRO {

    /**
     * Insert a new entity (set of values) into entity storage.
     *
     * @param values the set of values
     * @return the new entity Id
     */
    public int insert(Map<String, Object> values);

    /**
     * Update an existing entity in entity storage with a new set of values.
     *
     * @param id the new entity Id
     * @param diff the set of values to be updated
     */
    public void update(int id, Map<String, Object> diff);

    /**
     * Delete an entity from entity storage.
     *
     * @param id the entity Id
     */
    public void delete(int id);
}
