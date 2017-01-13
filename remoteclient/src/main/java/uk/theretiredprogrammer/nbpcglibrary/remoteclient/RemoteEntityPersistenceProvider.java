/*
 * Copyright 2014-2017 Richard Linsdale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.nbpcglibrary.remoteclient;

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
import uk.theretiredprogrammer.nbpcglibrary.api.EntityFields;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityPersistenceProvider;
import uk.theretiredprogrammer.nbpcglibrary.api.PersistenceUnitProvider;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;
import uk.theretiredprogrammer.nbpcglibrary.common.Settings;
import uk.theretiredprogrammer.nbpcglibrary.json.JsonConversionException;
import uk.theretiredprogrammer.nbpcglibrary.json.JsonUtil;

/**
 * EntityPersistenceProvider Class for accessing remote entities.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <K> the Primary Key Class
 */
public abstract class RemoteEntityPersistenceProvider<K> implements EntityPersistenceProvider<K> {

    private String entityname;
    private RemotePersistenceUnitProvider persistenceUnitProvider;
    private String idx;

    @Override
    public void init(String entityname, Properties properties, PersistenceUnitProvider pup) {
        this.entityname = entityname;
        this.persistenceUnitProvider = (RemotePersistenceUnitProvider) pup;
        idx = null;
    }

    @Override
    public void init(String entityname, String idx, Properties properties, PersistenceUnitProvider pup) {
        this.entityname = entityname;
        this.persistenceUnitProvider = (RemotePersistenceUnitProvider) pup;
        this.idx = idx;
    }

    @Override
    public void close() {
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
            if (idx != null) {
                job.add("orderby", idx);
            }
            JsonObject reply = persistenceUnitProvider.executeSingleCommand(entityname, "getall", job.build());
            if (!reply.getBoolean("success")) {
                throw new LogicException("Remote get() failed: " + reply.getString("message") + "; " + reply.getString("exceptionmessage", ""));
            }
            for (JsonValue j : reply.getJsonArray("entities")) {
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
            JsonObjectBuilder job = Json.createObjectBuilder();
            addPK(job, pkey);
            JsonObject reply = persistenceUnitProvider.executeSingleCommand(entityname, "get", job.build());
            if (!reply.getBoolean("success")) {
                throw new LogicException("Remote get(pkey) failed: " + reply.getString("message") + "; " + reply.getString("exceptionmessage", ""));
            }
            return makeEntityFields(reply.getJsonObject("entity"));
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
            JsonObjectBuilder job = Json.createObjectBuilder();
            if (idx != null) {
                job.add("orderby", idx);
            }
            JsonObject reply = persistenceUnitProvider.executeSingleCommand(entityname, "findall", job.build());
            if (!reply.getBoolean("success")) {
                throw new LogicException("Remote find() failed: " + reply.getString("message") + "; " + reply.getString("exceptionmessage", ""));
            }
            reply.getJsonArray("pkeys").stream().forEach((j) -> {
                list.add(getPK(j));
            });
            return list;
        } catch (IOException ex) {
            throw new LogicException("Remote find() failed: " + ex.getMessage());
        }
    }

    /**
     * Get the primary key - given a provided Json value
     *
     * @param pkey the primary key
     * @return the primary Key as a Java Object of correct class
     */
    protected abstract K getPK(JsonValue pkey);

    @Override
    public final synchronized List<EntityFields> get(String parametername, Object parametervalue) {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "get", parametername, parametervalue.toString());
        try {
            List<EntityFields> list = new ArrayList<>();
            JsonObjectBuilder job = Json.createObjectBuilder()
                    .add("field", parametername);
            JsonUtil.insertValue(job, "value", parametervalue);
            if (idx != null) {
                job.add("orderby", idx);
            }
            JsonObject reply = persistenceUnitProvider.executeSingleCommand(entityname, "getbyfield", job.build());
            if (!reply.getBoolean("success")) {
                throw new LogicException("Remote get(field,value) failed: " + reply.getString("message") + "; " + reply.getString("exceptionmessage", ""));
            }
            for (JsonValue j : reply.getJsonArray("entities")) {
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
                    .add("field", parametername);
            JsonUtil.insertValue(job, "value", parametervalue);
            if (idx != null) {
                job.add("orderby", idx);
            }
            JsonObject reply = persistenceUnitProvider.executeSingleCommand(entityname, "findbyfield", job.build());
            if (!reply.getBoolean("success")) {
                throw new LogicException("Remote find(field, value) failed: " + reply.getString("message") + "; " + reply.getString("exceptionmessage", ""));
            }
            reply.getJsonArray("pkeys").stream().forEach((j) -> {
                list.add(getPK(j));
            });
            return list;
        } catch (IOException ex) {
            throw new LogicException("Remote find(field,value) failed: " + ex.getMessage());
        }
    }

    @Override
    public final synchronized EntityFields getOne(String parametername, Object parametervalue) {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "getOne", parametername, parametervalue.toString());
        try {
            JsonObjectBuilder job = Json.createObjectBuilder()
                    .add("field", parametername);
            JsonUtil.insertValue(job, "value", parametervalue);
            if (idx != null) {
                job.add("orderby", idx);
            }
            JsonObject reply = persistenceUnitProvider.executeSingleCommand(entityname, "getbyfield", job.build());
            if (!reply.getBoolean("success")) {
                throw new LogicException("Remote getOne(field,value) failed: " + reply.getString("message") + "; " + reply.getString("exceptionmessage", ""));
            }
            JsonArray entities = reply.getJsonArray("entities");
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
                    .add("field", parametername);
            JsonUtil.insertValue(job, "value", parametervalue);
            if (idx != null) {
                job.add("orderby", idx);
            }
            JsonArray pkeys = persistenceUnitProvider.executeSingleCommand(entityname, "findbyfield", job.build()).getJsonArray("pkeys");
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
        try {
            JsonObjectBuilder job = Json.createObjectBuilder();
            JsonObject reply = persistenceUnitProvider.executeSingleCommand(entityname, "findnextidx", job.build());
            if (!reply.getBoolean("success")) {
                throw new LogicException("Remote findNextIdx() failed: " + reply.getString("message") + "; " + reply.getString("exceptionmessage", ""));
            }
            return reply.getInt("nextidx");
        } catch (IOException ex) {
            throw new LogicException("Remote findNextIdx() failed: " + ex.getMessage());
        }
    }

    @Override
    public final synchronized EntityFields insert(EntityFields values) {
        LogBuilder.writeLog("nbpcglib.RemoteEntityPersistenceProvider", this, "insert", values.toString());
        try {
            JsonObjectBuilder job = Json.createObjectBuilder()
                    .add("user", Settings.get("Usercode", "????"));
            addEntity(job, values);
            JsonObject reply = persistenceUnitProvider.executeSingleCommand(entityname, "create", job.build());
            if (!reply.getBoolean("success")) {
                throw new LogicException("Remote insert(values) failed: " + reply.getString("message") + "; " + reply.getString("exceptionmessage", ""));
            }
            EntityFields res = makeEntityFields(reply.getJsonObject("entity"));
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
                    .add("user", Settings.get("Usercode", "????"));
            addPK(job, pkey);
            addEntity(job, diff);
            JsonObject reply = persistenceUnitProvider.executeSingleCommand(entityname, "update", job.build());
            if (!reply.getBoolean("success")) {
                throw new LogicException("Remote update(pkey,values) failed: " + reply.getString("message") + "; " + reply.getString("exceptionmessage", ""));
            }
            EntityFields res = makeEntityFields(reply.getJsonObject("entity"));
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
            JsonObjectBuilder job = Json.createObjectBuilder();
            addPK(job, pkey);
            JsonObject reply = persistenceUnitProvider.executeSingleCommand(entityname, "delete", job.build());
            if (!reply.getBoolean("success")) {
                throw new LogicException("Remote delete() failed: " + reply.getString("message") + "; " + reply.getString("exceptionmessage", ""));
            }
        } catch (IOException ex) {
            throw new LogicException("Remote delete(pkey) failed: " + ex.getMessage());
        }
    }
}
