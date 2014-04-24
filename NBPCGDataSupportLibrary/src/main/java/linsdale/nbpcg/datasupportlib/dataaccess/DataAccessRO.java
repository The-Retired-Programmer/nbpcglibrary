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
package linsdale.nbpcg.datasupportlib.dataaccess;

import java.util.List;
import linsdale.nbpcg.datasupportlib.dataservice.ResultSetLoader;

/**
 * Data Access Interface - for a Read-Only Entity.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public interface DataAccessRO {

    /**
     * Get the set of entity Ids for all stored entities.
     *
     * @return the set of entity Ids
     */
    public List<Integer> find();

    /**
     * Get the set of entity Ids for all stored entities, where an equality
     * filter is applied.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the set of entity Ids
     */
    public List<Integer> find(String parametername, Object parametervalue);

    /**
     * Get an entity Id for a single entity selected by an equality filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the entity Id
     */
    public int findOne(String parametername, Object parametervalue);

    /**
     * Get the next index value for entities which have an explicit ordering
     * column defined.
     *
     * @return the next index value
     */
    public int getNextIdx();

    /**
     * Find a required entity based on its Id and use the loader to insert data
     * into entity.
     *
     * @param id the entity Id
     * @param loader the loader which provides the data values
     */
    public void load(int id, ResultSetLoader loader);

}
