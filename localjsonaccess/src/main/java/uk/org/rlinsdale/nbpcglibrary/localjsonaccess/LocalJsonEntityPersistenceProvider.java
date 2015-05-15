/*
 * Copyright (C) 2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package uk.org.rlinsdale.nbpcglibrary.localjsonaccess;

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
public class LocalJsonEntityPersistenceProvider implements EntityPersistenceProvider {

    private final LocalJsonPersistenceUnitProvider pup;
    private final Table table;

    /**
     * Constructor.
     *
     * @param tablename the entity table name in entity storage
     * @param properties the properties used for configuration
     * @param pup the PersistenceUnitProvider
     * @throws java.io.IOException
     */
    public LocalJsonEntityPersistenceProvider(String tablename, Properties properties, LocalJsonPersistenceUnitProvider pup) throws IOException {
        this.pup = pup;
        this.table = new Table(pup.load(tablename));
    }
    
    public void persist() throws IOException {
       table.persist(pup);
    }

    @Override
    public String instanceDescription() {        
        return LogBuilder.instanceDescription(this, pup.instanceDescription() + "," + table.getname());
    }

    @Override
    public JsonObject get(int id) throws IOException {
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "get", id);
        return table.get(JsonUtil.createJsonValue(id));
    }

    @Override
    public final JsonArray get() throws IOException {
        return table.get();
    }

    @Override
    public final JsonArray find() throws IOException {
        return table.find();
    }

    @Override
    public final JsonArray get(String parametername, JsonValue parametervalue) throws IOException {
        return table.get(parametername, parametervalue);
    }

    @Override
    public final JsonArray find(String parametername, JsonValue parametervalue) throws IOException {
        return table.find(parametername, parametervalue);
    }

    @Override
    public final JsonObject getOne(String parametername, JsonValue parametervalue) throws IOException {
        return table.getOne(parametername, parametervalue);
    }
    
    @Override
    public final JsonValue findOne(String parametername, JsonValue parametervalue) throws IOException {
        return table.findOne(parametername, parametervalue);
    }

    @Override
    public final int findNextIdx() throws IOException {
        return table.findNextIdx();
    }

    @Override
    public final int insert(JsonObject values) throws IOException {
        return JsonUtil.getIntegerValue(table.insert(values));
    }

    @Override
    public final void update(int id, JsonObject diff) throws IOException {
        table.update(JsonUtil.createJsonValue(id), diff);
    }

    @Override
    public final void delete(int id) throws IOException {
        table.delete(JsonUtil.createJsonValue(id));
    }
}
