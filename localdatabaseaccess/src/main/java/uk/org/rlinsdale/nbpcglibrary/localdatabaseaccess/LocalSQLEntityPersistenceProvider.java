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
package uk.org.rlinsdale.nbpcglibrary.localdatabaseaccess;

import java.io.IOException;
import java.util.Properties;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProvider;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 * EntityPersistenceProvider Class for access localSQL databases
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class LocalSQLEntityPersistenceProvider implements EntityPersistenceProvider {

    private final String tablename;
    private final LocalSQLPersistenceUnitProvider persistenceUnitProvider;
    private final String idx;

    /**
     * Constructor.
     *
     * @param tablename the entity table name in entity storage
     * @param properties the properties used for configuration
     * @param pup the PersistenceUnitProvider
     */
    public LocalSQLEntityPersistenceProvider(String tablename, Properties properties, LocalSQLPersistenceUnitProvider pup) {
        this.tablename = tablename;
        this.persistenceUnitProvider = pup;
        this.idx = null;
    }

    /**
     * Constructor.
     *
     * @param tablename the entity table name in entity storage
     * @param idx the index field - used to order the returned entities
     * @param properties the properties used for configuration
     * @param pup the PersistenceUnitProvider
     */
    public LocalSQLEntityPersistenceProvider(String tablename, String idx, Properties properties, LocalSQLPersistenceUnitProvider pup) {
        this.tablename = tablename;
        this.persistenceUnitProvider = pup;
        this.idx = idx;
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, LogBuilder.instanceDescription(persistenceUnitProvider) + "-" + tablename);
    }

    @Override
    public JsonObject get(int id) {
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "load", id);
        return persistenceUnitProvider.simpleQuery(tablename, "SELECT * from " + tablename + " where id=" + id);
    }

    @Override
    public final JsonArray get() {
        return idx == null
                ? persistenceUnitProvider.multiQuery(tablename, "SELECT * from " + tablename)
                : persistenceUnitProvider.multiQuery(tablename, "SELECT * from " + tablename + " ORDER BY " + idx);
    }

    @Override
    public final JsonArray find() {
        return idx == null
                ? persistenceUnitProvider.query("SELECT id from " + tablename)
                : persistenceUnitProvider.query("SELECT id from " + tablename + " ORDER BY " + idx);
    }

    @Override
    public final JsonArray get(String parametername, JsonValue parametervalue) {
        return idx == null
                ? persistenceUnitProvider.multiQuery(tablename, "SELECT * from " + tablename + " where " + parametername + "={P}", parametervalue)
                : persistenceUnitProvider.multiQuery(tablename, "SELECT * from " + tablename + " where " + parametername + "={P} ORDER BY " + idx, parametervalue);
    }

    @Override
    public final JsonArray find(String parametername, JsonValue parametervalue) {
        return idx == null
                ? persistenceUnitProvider.query("SELECT id from " + tablename + " where " + parametername + "={P}", parametervalue)
                : persistenceUnitProvider.query("SELECT id from " + tablename + " where " + parametername + "={P} ORDER BY " + idx, parametervalue);
    }

    @Override
    public final JsonObject getOne(String parametername, JsonValue parametervalue) throws IOException {
        JsonArray get = persistenceUnitProvider.multiQuery(tablename, "SELECT * from " + tablename + " where " + parametername + "={P}", parametervalue);
        if (get.size() != 1) {
            throw new IOException("Single row expected");
        }
        return get.getJsonObject(0);
    }

    @Override
    public final JsonValue findOne(String parametername, JsonValue parametervalue) throws IOException {
        JsonArray find = persistenceUnitProvider.query("SELECT id from " + tablename + " where " + parametername + "={P}", parametervalue);
        if (find.size() != 1) {
            throw new IOException("Single row expected");
        }
        return find.get(0);
    }

    @Override
    public final int findNextIdx() throws IOException {
        if (idx == null) {
            throw new IOException("findNextIdx() should not be called if the entity is not ordered");
        }
        return persistenceUnitProvider.simpleIntQuery("SELECT max(" + idx + ")+1 as nextidx from " + tablename, "nextidx");
    }

    @Override
    public final int insert(JsonObject values) {
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "insert", values);
        persistenceUnitProvider.execute("INSERT INTO " + tablename + " ({$KEYLIST}) VALUES ({$VALUELIST})", values);
        int id = persistenceUnitProvider.simpleIntQuery("SELECT LAST_INSERT_ID() as id", "id");
        LogBuilder.writeExitingLog("nbpcglib.localSQLPersistenceUnitProvider", this, "insert", id);
        return id;
    }

    @Override
    public final void update(int id, JsonObject diff) {
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "update", id, diff);
        persistenceUnitProvider.execute("UPDATE " + tablename + " SET {$KEYVALUELIST} WHERE id=" + id, diff);
        LogBuilder.writeExitingLog("nbpcglib.localSQLPersistenceUnitProvider", this, "update");
    }

    @Override
    public final void delete(int id) {
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "delete", id);
        persistenceUnitProvider.execute("DELETE from " + tablename + " WHERE id = {P}", JsonUtil.createJsonValue(id));
    }
}
