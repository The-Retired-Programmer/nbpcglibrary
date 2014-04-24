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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface for handling entity field states for a read-only entity.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public interface DBFieldsRO {

    /**
     * Restore the State (from the last SaveState())
     */
    public void restoreState();

    /**
     * Save the State.
     */
    public void saveState();

    /**
     * Use the provided loader to insert the resultset data into the entity
     * fields.
     *
     * @param rs the resultset
     * @throws SQLException if problems
     */
    public void load(ResultSet rs) throws SQLException;
}
