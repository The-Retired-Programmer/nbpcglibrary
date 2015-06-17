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
package uk.org.rlinsdale.nbpcglibrary.remoteclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import uk.org.rlinsdale.nbpcglibrary.api.EntityFields;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProvider;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;
import uk.org.rlinsdale.nbpcglibrary.common.Settings;
import uk.org.rlinsdale.nbpcglibrary.json.JsonConversionException;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 * EntityPersistenceProvider Class for accessing remote entities.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> the Primary Key Class
 */
public abstract class RemoteEntityPersistenceProvider<K> implements EntityPersistenceProvider<K> {

    private String entityname;
    private RemotePersistenceUnitProvider persistenceUnitProvider;
    private String idx;

    /**
     * Initialise this persistence provider.
     *
     * @param entityname the entity table name in entity storage
     * @param properties the properties used for configuration
     * @param pup the data access manager to use to access the data
     */
    public void init(String entityname, Properties properties, RemotePersistenceUnitProvider pup) {
        this.entityname = entityname;
        this.persistenceUnitProvider = pup;
        idx = null;
    }

    /**
     * Initialise this persistence provider.
     *
     * @param entityname the entity table name in entity storage
     * @param idx the index field - used to order the returned entities
     * @param properties the properties used for configuration
     * @param pup the data access manager to use to access the data
     */
    public void init(String entityname, String idx, Properties properties, RemotePersistenceUnitProvider pup) {
        this.entityname = entityname;
        this.persistenceUnitProvider = pup;
        this.idx = null;
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, LogBuilder.instanceDescription(persistenceUnitProvider) + "-" + entityname);
    }

    @Override
    public final synchronized List<EntityFields> get() {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "get");
        List<EntityFields> list = new ArrayList<>();
        try {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("action", "getall")
                    .add("entityname", entityname);
            if (idx != null) {
                job.add("orderby", idx);
            }
            for (JsonValue j : persistenceUnitProvider.executeSingleCommand(job.build()).getJsonArray("entities")) {
                list.add(makeEntityFields((JsonObject) j));
            }
            return list;
        } catch (IOException ex) {
            throw new LogicException("Remote get() failed: " + ex.getMessage());
        }
    }

    @Override
    public final synchronized EntityFields get(K pkey) {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "get", pkey);
        try {
            JsonObjectBuilder job = Json.createObjectBuilder()
                    .add("action", "get")
                    .add("entityname", entityname);
            addPK(job, pkey);
            return makeEntityFields(persistenceUnitProvider.executeSingleCommand(job.build()).getJsonObject("entity"));
        } catch (IOException ex) {
            throw new LogicException("Remote get(pkey) failed: " + ex.getMessage());
        }
    }

    /**
     * Add the primary key to the JsonObject
     *
     * @param job the JsonObjectBuilder being used to create this object
     * @param pkey the primary key value;
     */
    protected abstract void addPK(JsonObjectBuilder job, K pkey);

    @Override
    public final synchronized List<K> find() {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "find");
        try {
            List<K> list = new ArrayList<>();
            JsonObjectBuilder job = Json.createObjectBuilder()
                    .add("action", "findall")
                    .add("entityname", entityname);
            if (idx != null) {
                job.add("orderby", idx);
            }
            for (JsonValue j : persistenceUnitProvider.executeSingleCommand(job.build()).getJsonArray("pkeys")) {
                list.add(getPK(j));
            }
            return list;
        } catch (IOException ex) {
            throw new LogicException("Remote find() failed: " + ex.getMessage());
        }
    }

    /**
     * Get the primary key - given a provided Json value
     *
     * @param pkey
     * @return the primary Key as a Java Object of correct class
     */
    protected abstract K getPK(JsonValue pkey);

    @Override
    public final synchronized List<EntityFields> get(String parametername, Object parametervalue) {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "get", parametername, parametervalue.toString());
        try {
            List<EntityFields> list = new ArrayList<>();
            JsonObjectBuilder job = Json.createObjectBuilder()
                    .add("action", "getbyfield")
                    .add("entityname", entityname)
                    .add("field", parametername);
            JsonUtil.insertValue(job, "value", parametervalue);
            if (idx != null) {
                job.add("orderby", idx);
            }
            for (JsonValue j : persistenceUnitProvider.executeSingleCommand(job.build()).getJsonArray("entities")) {
                list.add(makeEntityFields((JsonObject) j));
            }
            return list;
        } catch (IOException ex) {
            throw new LogicException("Remote get(field,value) failed: " + ex.getMessage());
        }
    }

    @Override
    public final synchronized List<K> find(String parametername, Object parametervalue) {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "find", parametername, parametervalue.toString());
        List<K> list = new ArrayList<>();
        try {
            JsonObjectBuilder job = Json.createObjectBuilder()
                    .add("action", "findbyfield")
                    .add("entityname", entityname)
                    .add("field", parametername);
            JsonUtil.insertValue(job, "value", parametervalue);
            if (idx != null) {
                job.add("orderby", idx);
            }
            for (JsonValue j : persistenceUnitProvider.executeSingleCommand(job.build()).getJsonArray("pkeys")) {
                list.add(getPK(j));
            }
            return list;
        } catch (IOException ex) {
            throw new LogicException("Remote find() failed: " + ex.getMessage());
        }
    }

    @Override
    public final synchronized EntityFields getOne(String parametername, Object parametervalue) {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "getOne", parametername, parametervalue.toString());
        try {
            JsonObjectBuilder job = Json.createObjectBuilder()
                    .add("action", "getbyfield")
                    .add("entityname", entityname)
                    .add("field", parametername);
            JsonUtil.insertValue(job, "value", parametervalue);
            if (idx != null) {
                job.add("orderby", idx);
            }
            JsonArray entities = persistenceUnitProvider.executeSingleCommand(job.build()).getJsonArray("entities");
            if (entities.size() != 1) {
                throw new LogicException("Remote getOne(field,value) failed: Single row expected");
            }
            return makeEntityFields(entities.getJsonObject(0));
        } catch (IOException ex) {
            throw new LogicException("Remote getOne(field,value) failed: " + ex.getMessage());
        }
    }

    @Override
    public final synchronized K findOne(String parametername, Object parametervalue) {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "findOne", parametername, parametervalue.toString());
        List<K> list = new ArrayList<>();
        try {
            JsonObjectBuilder job = Json.createObjectBuilder()
                    .add("action", "findbyfield")
                    .add("entityname", entityname)
                    .add("field", parametername);
            JsonUtil.insertValue(job, "value", parametervalue);
            if (idx != null) {
                job.add("orderby", idx);
            }
            JsonArray pkeys = persistenceUnitProvider.executeSingleCommand(job.build()).getJsonArray("pkeys");
            if (pkeys.size() != 1) {
                throw new LogicException("Remote findOne(field,value) failed: Single row expected");
            }
            return getPK(pkeys.get(0));
        } catch (IOException ex) {
            throw new LogicException("Remote findOne(field,value) failed: " + ex.getMessage());
        }
    }

    @Override
    public final synchronized int findNextIdx() {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "findNextIdx");
        if (idx == null) {
            throw new LogicException("findNextIdx() should not be called if the entity is not ordered");
        }
        return Integer.MAX_VALUE; // temporary 
    }

    @Override
    public final synchronized EntityFields insert(EntityFields values) {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "insert", values.toString());
        try {
            JsonObjectBuilder job = Json.createObjectBuilder()
                    .add("action", "create")
                    .add("entityname", entityname)
                    .add("user", Settings.get("Usercode", "????"));
            addEntity(job, values);
            EntityFields res = makeEntityFields(persistenceUnitProvider.executeSingleCommand(job.build()).getJsonObject("entity"));
            LogBuilder.writeExitingLog("nbpcglib.RemoteEntityPersistenceProvider", this, "insert", res);
            return res;
        } catch (IOException ex) {
            throw new LogicException("Remote insert(values) failed: " + ex.getMessage());
        }
    }

    private void addEntity(JsonObjectBuilder job, EntityFields ef) throws JsonConversionException {
        JsonObjectBuilder jobe = Json.createObjectBuilder();
        for (Entry<String, Object> field : ef.entrySet()) {
            JsonUtil.insertValue(jobe, field.getKey(), field.getValue());
        }
        job.add("entity", jobe.build());
    }

    private EntityFields makeEntityFields(JsonObject record) throws JsonConversionException {
        EntityFields entity = new EntityFields();
        for (Map.Entry<String, JsonValue> field : record.entrySet()) {
            entity.put(field.getKey(), JsonUtil.getValue(field.getValue()));
        }
        return entity;
    }

    @Override
    public final synchronized EntityFields update(K pkey, EntityFields diff) {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "update", pkey, diff.toString());
        try {
            JsonObjectBuilder job = Json.createObjectBuilder()
                    .add("action", "update")
                    .add("entityname", entityname)
                    .add("user", Settings.get("Usercode", "????"));
            addPK(job, pkey);
            addEntity(job, diff);
            EntityFields res = makeEntityFields(persistenceUnitProvider.executeSingleCommand(job.build()).getJsonObject("entity"));
            LogBuilder.writeExitingLog("nbpcglib.RemoteEntityPersistenceProvider", this, "update", res);
            return res;
        } catch (IOException ex) {
            throw new LogicException("Remote update(pkey,values) failed: " + ex.getMessage());
        }
    }

    @Override
    public final synchronized void delete(K pkey) {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "delete", pkey);
        try {
            JsonObjectBuilder job = Json.createObjectBuilder()
                    .add("action", "delete")
                    .add("entityname", entityname);
            addPK(job, pkey);
            persistenceUnitProvider.executeSingleCommand(job.build());
        } catch (IOException ex) {
            throw new LogicException("Remote delete(pkey) failed: " + ex.getMessage());
        }
    }
}