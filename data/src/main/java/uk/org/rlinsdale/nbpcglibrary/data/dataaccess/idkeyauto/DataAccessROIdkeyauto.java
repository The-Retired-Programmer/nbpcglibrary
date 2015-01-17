/*
 * Copyright (C) 2014-2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.data.dataaccess.DataAccessRO;
import uk.org.rlinsdale.nbpcglibrary.data.dataservice.DBDataService;
import uk.org.rlinsdale.nbpcglibrary.data.dataservice.ResultSetLoader;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;

/**
 * Data Access Class - for a Read-Only Entity, with an auto generated key
 * (generated by its entity Storage).
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class DataAccessROIdkeyauto implements DataAccessRO {

    private final String tablename;
    private final DBDataService dbdataservice;
    private final String idx;

    /**
     * Constructor.
     *
     * @param tablename the entity table name in entity storage
     * @param dbdataservice the dataservice to be used to access the entity
     * storage
     */
    public DataAccessROIdkeyauto(String tablename, DBDataService dbdataservice) {
        this.tablename = tablename;
        this.dbdataservice = dbdataservice;
        this.idx = null;
    }
    
    @Override
    public String classDescription() {
        return LogBuilder.classDescription(this, LogBuilder.classDescription(dbdataservice)+"-"+tablename);
    }

    /**
     * Constructor.
     *
     * @param tablename the entity table name in entity storage
     * @param idx the index field - used to order the returned entities
     * @param dbdataservice the dataservice to be used to access the entity
     * storage
     */
    public DataAccessROIdkeyauto(String tablename, String idx, DBDataService dbdataservice) {
        this.tablename = tablename;
        this.dbdataservice = dbdataservice;
        this.idx = idx;
    }

    @Override
    public final void load(int id, ResultSetLoader loader) {
        LogBuilder.writeLog("nbpcglibrary.data", this, "load", id, loader);
        dbdataservice.simpleQuery("SELECT * from " + tablename + " where id=" + id, loader);
    }

    @Override
    public final List<Integer> find() {
        return idx == null
                ? dbdataservice.query("SELECT id from " + tablename)
                : dbdataservice.query("SELECT id from " + tablename + " ORDER BY " + idx);
    }

    @Override
    public final List<Integer> find(String parametername, Object parametervalue) {
        return idx == null
                ? dbdataservice.query("SELECT id from " + tablename + " where " + parametername + "={P}", parametervalue)
                : dbdataservice.query("SELECT id from " + tablename + " where " + parametername + "={P} ORDER BY " + idx, parametervalue);
    }

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
        return dbdataservice.simpleIntQuery("SELECT max(" + idx + ")+1 as nextidx from " + tablename, "nextidx");
    }
}
