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
package uk.theretiredprogrammer.nbpcglibrary.localjsonaccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityFields;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityPersistenceProvider;
import uk.theretiredprogrammer.nbpcglibrary.api.PersistenceUnitProvider;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;
import uk.theretiredprogrammer.nbpcglibrary.json.JsonConversionException;
import uk.theretiredprogrammer.nbpcglibrary.json.JsonUtil;

/**
 * EntityPersistenceProvider Class for access local Json File based persistent
 * storage
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <K> the Primary Key class
 */
public abstract class LocalJsonEntityPersistenceProvider<K> implements EntityPersistenceProvider<K> {

    private LocalJsonPersistenceUnitProvider pup;
    private String name;
    private String idx;
    private int nextid;
    private int nextidx;
    private final Map<K, EntityFields> tablerecords = new HashMap<>();
    private boolean dirty = false;

    @Override
    public void init(String tablename, Properties properties, PersistenceUnitProvider pup) throws IOException {
        this.idx = null;
        this.pup = (LocalJsonPersistenceUnitProvider) pup;
        JsonObject tableJson = this.pup.load(tablename);
        try {
            this.nextid = JsonUtil.getObjectKeyIntegerValue(tableJson, "nextid");
            this.nextidx = JsonUtil.getObjectKeyIntegerValue(tableJson, "nextidx");
            this.name = JsonUtil.getObjectKeyStringValue(tableJson, "name");
            //
            JsonArray records = JsonUtil.getObjectKeyArrayValue(tableJson, "entities");
            for (JsonValue record : records) {
                JsonObject rec = (JsonObject) record;
                EntityFields ef = makeEntityFields(rec);
                K pk = getPK(ef);
                tablerecords.put(pk, makeEntityFields(rec));
            }
        } catch (JsonConversionException ex) {
            throw new LogicException("Illegal Json Format data  - should never happen");
        }
    }
    
    @Override
    public void init(String tablename, String idx, Properties properties, PersistenceUnitProvider pup) throws IOException {
        this.idx = idx;
        this.init(tablename, properties, pup);
    }
    
    @Override
    public void close() {
        persist();
    }
    
    private EntityFields makeEntityFields(JsonObject record) throws JsonConversionException {
        EntityFields entity = new EntityFields();
        for (Map.Entry<String, JsonValue> field : record.entrySet()) {
            entity.put(field.getKey(), JsonUtil.getValue(field.getValue()));
        }
        return entity;
    }

    /**
     * Persist the in-memory data (to json file)
     */
    public void persist() {
        if (dirty) {
            try {
                JsonObjectBuilder job = Json.createObjectBuilder();
                job.add("name", name);
                job.add("nextid", nextid);
                job.add("nextidx", nextidx);
                JsonArrayBuilder rab = Json.createArrayBuilder();
                tablerecords.values().stream().forEach((ef) -> {
                    try {
                        rab.add(createJsonRecord(ef));
                    } catch (JsonConversionException ex) {
                        throw new LogicException("Illegal Java Object presented as field value");
                    }
                });
                job.add("entities", rab.build());
                pup.persist(job.build());
                dirty = false;
            } catch (IOException ex) {
                throw new LogicException("IO failure when persisting table");
            }
        }
    }

    private JsonObject createJsonRecord(EntityFields ef) throws JsonConversionException {
        JsonObjectBuilder job = Json.createObjectBuilder();
        for (Map.Entry<String, Object> field : ef.entrySet()) {
            JsonUtil.insertValue(job, field.getKey(), field.getValue());
        }
        return job.build();
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, pup.instanceDescription() + "," + name);
    }

    @Override
    public EntityFields get(K pkey) {
        LogBuilder.writeLog("nbpcglib.localJsonPersistenceUnitProvider", this, "get", pkey);
        return tablerecords.get(pkey);
    }

    @Override
    public final List<EntityFields> get() {
        List<EntityFields> efs = new ArrayList<>();
        tablerecords.values().stream().forEach((ef) -> {
            efs.add(copy(ef));
        });
        return efs;
    }

    @Override
    public final List<K> find() {
        List<K> pks = new ArrayList<>();
        tablerecords.keySet().stream().forEach((key) -> {
            pks.add(key);
        });
        return pks;
    }

    @Override
    public final List<EntityFields> get(String parametername, Object parametervalue) {
        List<EntityFields> efs = new ArrayList<>();
        tablerecords.values().stream().forEach((ef) -> {
            if (ef.get(parametername).equals(parametervalue)) {
                efs.add(copy(ef));
            }
        });
        return efs;
    }

    @Override
    public final List<K> find(String parametername, Object parametervalue) {
        List<K> pks = new ArrayList<>();
        tablerecords.entrySet().stream().forEach((e) -> {
            if (e.getValue().get(parametername).equals(parametervalue)) {
                pks.add(e.getKey());
            }
        });
        return pks;
    }

    @Override
    public final EntityFields getOne(String parametername, Object parametervalue) {
        List<EntityFields> get = get(parametername, parametervalue);
        if (get.size() != 1) {
            throw new LogicException("Single row expected");
        }
        return get.get(0);
    }

    @Override
    public final K findOne(String parametername, Object parametervalue) {
        List<K> find = find(parametername, parametervalue);
        if (find.size() != 1) {
            throw new LogicException("Single row expected");
        }
        return find.get(0);
    }

    @Override
    public final int findNextIdx() {
        dirty = true;
        return nextidx++;
    }

    @Override
    public final EntityFields insert(EntityFields values) {
        dirty = true;
        EntityFields entity = new EntityFields();
        entity.putAll(values);
        autoGenPrimaryKeyHook(entity);
        addTimestampInfo(entity);
        tablerecords.put(getPK(entity), entity);
        return copy(entity);
    }

    /**
     * Action to create an auto generated primary key
     *
     * @param ef the entity fields into which the new primary key is added
     */
    protected void autoGenPrimaryKeyAction(EntityFields ef) {
        int pkey = nextid++;
        ef.put("id", pkey);
    }

    @Override
    public final EntityFields update(K pkey, EntityFields diffs) {
        dirty = true;
        EntityFields entity = tablerecords.get(pkey);
        entity.putAll(diffs);
        updateTimestampInfo(entity);
        return copy(entity);
    }
    
    @Override
    public final void delete(K pkey) {
        dirty = true;
        tablerecords.remove(pkey);
    }

    private EntityFields copy(EntityFields ef) {
        EntityFields efc = new EntityFields();
        efc.putAll(ef);
        return efc;
    }
}
