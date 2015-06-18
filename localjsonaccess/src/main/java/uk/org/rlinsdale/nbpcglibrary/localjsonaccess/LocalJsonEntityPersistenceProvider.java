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
import uk.org.rlinsdale.nbpcglibrary.api.EntityFields;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProvider;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;
import uk.org.rlinsdale.nbpcglibrary.json.JsonConversionException;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 * EntityPersistenceProvider Class for access local Json File based persistent
 * storage
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> the Primary Key class
 */
public abstract class LocalJsonEntityPersistenceProvider<K> implements EntityPersistenceProvider<K> {

    private LocalJsonPersistenceUnitProvider pup;
    private String name;
    private int nextid;
    private int nextidx;
    private final Map<K, EntityFields> tablerecords = new HashMap<>();
    private boolean dirty = false;

    /**
     * @param tablename the entity table name in entity storage
     * @param properties the properties used for configuration
     * @param pup the PersistenceUnitProvider
     * @throws IOException if problem with reading or parsing the json data
     */
    public void init(String tablename, Properties properties, LocalJsonPersistenceUnitProvider pup) throws IOException {
        this.pup = pup;
        JsonObject tableJson = pup.load(tablename);
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

    /**
     * Get the primary key from a entity described by entityfields
     *
     * @param ef the entity fields
     * @return the primary Key
     */
    protected abstract K getPK(EntityFields ef);

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
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "get", pkey);
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
        tablerecords.put(getPK(entity), entity);
        return copy(entity);
    }

    /**
     * Hook to create an auto generated primary key if required
     *
     * @param ef the entity fields into which the new primary key can be added
     */
    protected abstract void autoGenPrimaryKeyHook(EntityFields ef);

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
