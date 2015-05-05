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
package uk.org.rlinsdale.nbpcglibrary.data.dbfields.idandstamp;

import java.io.IOException;
import javax.json.JsonObject;
import uk.org.rlinsdale.nbpcglibrary.data.dbfields.DBFieldsRO;
import uk.org.rlinsdale.nbpcglibrary.common.Settings;
import uk.org.rlinsdale.nbpcglibrary.api.Timestamp;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

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
    public void load(JsonObject data) throws IOException {
        createdby = JsonUtil.getObjectKeyStringValue(data, "createdby");
        createdon.setDateUsingSQLString(JsonUtil.getObjectKeyStringValue(data,"createdon" ));
        updatedby = JsonUtil.getObjectKeyStringValue(data,"updatedby" );
        updatedon.setDateUsingSQLString(JsonUtil.getObjectKeyStringValue(data,"updatedon" ));
    }

    @Override
    public void restoreState() {
    }

    @Override
    public void saveState() {
    }
}
