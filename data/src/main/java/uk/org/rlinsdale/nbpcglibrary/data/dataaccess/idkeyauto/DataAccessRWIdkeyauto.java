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
package uk.org.rlinsdale.nbpcglibrary.data.dataaccess.idkeyauto;

import java.util.Map;
import uk.org.rlinsdale.nbpcglibrary.data.dataaccess.DataAccessRW;
import uk.org.rlinsdale.nbpcglibrary.data.dataservice.DBDataService;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * DB data access methods for a "Standard" data base table
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class DataAccessRWIdkeyauto extends DataAccessROIdkeyauto implements DataAccessRW {

    private final String tablename;
    private final DBDataService dbdataservice;

    /**
     * Constructor.
     *
     * @param tablename the entity table name in entity storage
     * @param dbdataservice the dataservice to be used to access the entity
     */
    public DataAccessRWIdkeyauto(String tablename, DBDataService dbdataservice) {
        super(tablename, dbdataservice);
        this.tablename = tablename;
        this.dbdataservice = dbdataservice;
    }

    /**
     * Constructor.
     *
     * @param tablename the entity table name in entity storage
     * @param idx the index field - used to order the returned entities
     * @param dbdataservice the dataservice to be used to access the entity
     */
    public DataAccessRWIdkeyauto(String tablename, String idx, DBDataService dbdataservice) {
        super(tablename, idx, dbdataservice);
        this.tablename = tablename;
        this.dbdataservice = dbdataservice;
    }

    @Override
    public final int insert(Map<String, Object> values) {
        LogBuilder.writeEnteringLog("nbpcglibrary.data", "DataAccessRWIdkeyauto", "insert", values);
        dbdataservice.execute("INSERT INTO " + tablename + " ({$KEYLIST}) VALUES ({$VALUELIST})", values);
        int id = dbdataservice.simpleIntQuery("SELECT LAST_INSERT_ID() as id", "id");
        LogBuilder.writeExitingLog("nbpcglibrary.data", "DataAccessRWIdkeyauto", "insert", id);
        return id;
    }

    @Override
    public final void update(int id, Map<String, Object> diff) {
        LogBuilder.writeEnteringLog("nbpcglibrary.data", "DataAccessRWIdkeyauto", "update", id, diff);
        dbdataservice.execute("UPDATE " + tablename + " SET {$KEYVALUELIST} WHERE id=" + id, diff);
         LogBuilder.writeExitingLog("nbpcglibrary.data", "DataAccessRWIdkeyauto", "update");
    }

    @Override
    public final void delete(int id) {
        LogBuilder.writeEnteringLog("nbpcglibrary.data", "DataAccessRWIdkeyauto", "delete", id);
        dbdataservice.execute("DELETE from " + tablename + " WHERE id = {P}", id);
    }
}
