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
package linsdale.nbpcg.datasupportlib.dataservice;

import java.sql.ResultSet;

/**
 * Interface for a ResultSet Loader. A resultsetloader takes a resultset and
 * processes this. Typical use case: creating an entity and initialising its
 * fields to those of the resultset.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public interface ResultSetLoader {

    /**
     * Process the given resultset. Use case: load it into an entity
     *
     * @param rs the resultset
     */
    public void load(ResultSet rs);
}
