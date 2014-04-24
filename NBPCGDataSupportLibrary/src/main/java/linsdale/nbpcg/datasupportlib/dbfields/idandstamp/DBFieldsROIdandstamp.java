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
package linsdale.nbpcg.datasupportlib.dbfields.idandstamp;

import java.sql.ResultSet;
import java.sql.SQLException;
import linsdale.nbpcg.datasupportlib.dbfields.DBFieldsRO;
import linsdale.nbpcg.supportlib.Settings;
import linsdale.nbpcg.supportlib.Timestamp;

/**
 * Handles Read-Only entity field management, for a entity which includes a Id and
 * standardised timestamp (created and updated).
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class DBFieldsROIdandstamp implements DBFieldsRO {

    private String createdby;
    private final Timestamp createdon;

    /**
     * Field - updated by
     */
    protected String updatedby;

    /**
     * Field - updated on
     */
    protected Timestamp updatedon;

    /**
     * Constructor.
     */
    public DBFieldsROIdandstamp() {
        String usercode = Settings.get("Usercode", "????");
        createdon = new Timestamp();
        updatedon = new Timestamp();
        createdby = usercode;
        updatedby = usercode;
    }

    @Override
    public void load(ResultSet rs) throws SQLException {
        createdby = rs.getString("createdby");
        createdon.setDateUsingSQLString(rs.getString("createdon"));
        updatedby = rs.getString("updatedby");
        updatedon.setDateUsingSQLString(rs.getString("updatedon"));
    }

    @Override
    public void restoreState() {
    }

    @Override
    public void saveState() {
    }
}
