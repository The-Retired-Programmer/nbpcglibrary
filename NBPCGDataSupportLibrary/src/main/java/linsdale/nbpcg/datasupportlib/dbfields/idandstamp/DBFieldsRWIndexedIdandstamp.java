/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
import java.util.Map;
import linsdale.nbpcg.datasupportlib.dbfields.DBFieldsRWIndexed;
import linsdale.nbpcg.datasupportlib.entity.EntityRWIndexed;

/**
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
public class DBFieldsRWIndexedIdandstamp <E extends EntityRWIndexed> extends DBFieldsRWIdandstamp<E>  implements DBFieldsRWIndexed<E>  {

    private int idx = Integer.MAX_VALUE;
    private int idxOriginal;
    
    @Override
    public void restoreState() {
        super.restoreState();
        idx = idxOriginal;
    }

    @Override
    public void saveState() {
        super.saveState();
        idxOriginal = idx;
    }

    @Override
    public void load(ResultSet rs) throws SQLException {
        super.load(rs);
        idx = rs.getInt("idx");
    }

    @Override
    public void setIndex(int idx) {
        this.idx = idx;
    }
    
    @Override
    public int getIndex(){
        return idx;
    }
    
    @Override
    public final void diffs(Map<String, Object> map) {
        if (idx != idxOriginal) {
            map.put("idx", idx);
        }
        super.diffs(map);
    }

    /**
     * Update map with standard db fields
     *
     */
    @Override
    public final void values(Map<String, Object> map) {
        map.put("idx", idx);
        super.values(map);
    }
}