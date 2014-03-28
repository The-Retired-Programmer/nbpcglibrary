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
package linsdale.nbpcg.datasupportlib.dataaccess.idkeyauto;

import java.util.List;
import java.util.logging.Level;
import linsdale.nbpcg.datasupportlib.dataaccess.DataAccessRO;
import linsdale.nbpcg.datasupportlib.dataservice.DBDataService;
import linsdale.nbpcg.datasupportlib.dataservice.ResultSetLoader;
import linsdale.nbpcg.supportlib.Log;
import linsdale.nbpcg.supportlib.LogicException;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class DataAccessROIdkeyauto implements DataAccessRO {

    private final String tablename;
    private final DBDataService dbdataservice;
    private final String idx;

    /**
     * Create an instance of a Factory.
     * @param entityname
     * @param tablename
     * @param dbdataservice
     */
    public DataAccessROIdkeyauto(String entityname, String tablename, DBDataService dbdataservice) {
        this.tablename = tablename;
        this.dbdataservice = dbdataservice;
        this.idx = null;
    }

    /**
     * Create an instance of a Factory.
     * @param entityname
     * @param tablename
     * @param idx
     * @param dbdataservice
     */
    public DataAccessROIdkeyauto(String entityname, String tablename, String idx, DBDataService dbdataservice) {
        this.tablename = tablename;
        this.dbdataservice = dbdataservice;
        this.idx = idx;
    }

    /**
     * Find a required Resultset based on its primary key (id).
     *
     * @param id the primary key
     * @param loader
     */
    @Override
    public final void load(int id, ResultSetLoader loader) {
        Log.get("linsdale.nbpcg.datasupportlib").log(Level.FINEST, "StandardDBPersistentDataAccess.getData({0})", id);
        dbdataservice.simpleQuery("SELECT * from " + tablename + " where id=" + id, loader);
    }

    /**
     * Obtain a list of entity id - selecting all rows from the table.
     *
     * @return list of entities
     */
    @Override
    public final List<Integer> find() {
        return idx == null
                ? dbdataservice.query("SELECT id from " + tablename)
                : dbdataservice.query("SELECT id from " + tablename + " ORDER BY " + idx);
    }

    /**
     * Obtain a list of entity instances - selecting rows from the table based
     * on defined whereclause and associated parameter.
     *
     * for futher details of the SQl template and parameter syntax see
     * {@link DB}
     *
     * @param parametername
     * @param parametervalue
     * @return list of entities
     */
    @Override
    public final List<Integer> find(String parametername, Object parametervalue) {
        return idx == null
                ? dbdataservice.query("SELECT id from " + tablename + " where " + parametername + "={P}", parametervalue)
                : dbdataservice.query("SELECT id from " + tablename + " where " + parametername + "={P} ORDER BY " + idx, parametervalue);
    }

    /**
     * Obtain an entity instance - selecting a row from the table based on
     * defined parameter and value.
     *
     * @param parametername the column name to be used
     * @param parametervalue the parameter used for comparison
     * @return list of entities
     * @throws LogicException if 0 or many rows returned by Query
     */
    @Override
    public final int findOne(String parametername, Object parametervalue) {
        List<Integer> all = dbdataservice.query("SELECT id from " + tablename + " where " + parametername + "={P}", parametervalue);
        if (all.size() != 1) {
            throw new LogicException("Single row expected");
        }
        return all.get(0);
    }

    @Override
    public final int getNextIdx() {
        if (idx == null) {
            throw new LogicException("DataAccessROIdkeyauto:getNextIdx() should not be called if the entity is not ordered");
        }
        return dbdataservice.simpleIntQuery("SELECT max("+idx+")+1 as nextidx from " + tablename, "nextidx");
    }
}