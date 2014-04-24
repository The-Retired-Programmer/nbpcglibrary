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

import java.util.Map;
import linsdale.nbpcg.datasupportlib.dbfields.DBFieldsRW;
import linsdale.nbpcg.datasupportlib.entity.EntityRW;
import linsdale.nbpcg.supportlib.Timestamp;

/**
 * Handles Read-Write entity field management, for a entity which includes a Id and
 * standardised timestamp (created and updated).
 * 
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the entity class
 */
public class DBFieldsRWIdandstamp<E extends EntityRW> extends DBFieldsROIdandstamp implements DBFieldsRW<E> {

    @Override
    public void diffs(Map<String, Object> map) {
        if (!map.isEmpty()) {
            updatedon.setDateUsingSQLString(new Timestamp().toSQLString());
            map.put("updatedon", updatedon);
            map.put("updatedby", updatedby);
        }
    }

    @Override
    public void values(Map<String, Object> map) {
        updatedon.setDateUsingSQLString(new Timestamp().toSQLString());
        map.put("updatedon", updatedon);
        map.put("updatedby", updatedby);
        map.put("createdon", updatedon);
        map.put("createdby", updatedby);
    }

    @Override
    public final void copy(E source) {
    }
}