/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.rlinsdale.nbpcglibrary.localjsonaccess;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 * representation of a table in memory (JsonObject)
 *
 * @author richard
 */
public class Table {

    private final String name;
    private int nextid;
    private int nextidx;
    private final Map<Integer, JsonObject> tablerecords = new HashMap<>();
    private boolean dirty = false;

    /**
     * Constructor
     * @param tableJson the Json Object representing the table
     * @throws IOException if problems parsing the data
     */
    public Table(JsonObject tableJson) throws IOException {
        System.out.println(tableJson.toString());
        this.nextid = JsonUtil.getObjectKeyIntegerValue(tableJson, "nextid");
        this.nextidx = JsonUtil.getObjectKeyIntegerValue(tableJson, "nextidx");
        this.name = JsonUtil.getObjectKeyStringValue(tableJson, "name");
        //
        JsonArray records = JsonUtil.getObjectKeyArrayValue(tableJson, "records");
        for (int i = 0; i < records.size(); i++) {
            JsonObject record = records.getJsonObject(i);
            Integer pk = JsonUtil.getObjectKeyIntegerValue(record, "id");
            tablerecords.put(pk, record);
        }
    }

    /**
     * Persist the in-memory table to Json file
     * @param pup the PersistenceUnitProvider
     * @throws IOException if problems writing or parsing the data
     */
    public void persist(LocalJsonPersistenceUnitProvider pup) throws IOException {
        if (dirty) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("name", name);
            job.add("nextid", nextid);
            job.add("nextidx", nextidx);
            JsonArrayBuilder rab = Json.createArrayBuilder();
            tablerecords.values().stream().forEach((ro) -> {
                rab.add(ro);
            }); 
            job.add("records", rab.build());
            pup.persist(job.build());
            dirty = false;
        }
    }

    /**
     * Return the table name
     * @return the table name
     */
    public String getname() {
        return name;
    }

    /**
     *Get entity data - using PK lookup
     *
     * @param id the entity Id
     * @return the JsonObject containing field values
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public JsonObject get(JsonValue id) throws IOException {
        JsonObject record = tablerecords.get(JsonUtil.getIntegerValue(id));
        if (record == null) {
            throw new IOException("Can't find requested record");
        }
        return record;
    }

    /**
     * Get the set of entity for all stored entities.
     *
     * @return the set of entities
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public final JsonArray get() throws IOException {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        tablerecords.values().stream().forEach((jo) -> {
            jab.add(jo);
        });
        return jab.build();
    }

    /**
     * Get the set of entity Ids for all stored entities.
     *
     * @return the set of entity Ids
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public final JsonArray find() throws IOException {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        tablerecords.keySet().stream().forEach((key) -> {
            jab.add(key);
        });
        return jab.build();
    }

    /**
     * Get entity data for a many (0 to many) entities - using selected by an
     * column filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the array of entity data objects
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public final JsonArray get(String parametername, JsonValue parametervalue) throws IOException {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        tablerecords.values().stream().forEach((jo) -> {
            if (jo.get(parametername).equals(parametervalue)) {
                jab.add(jo);
            }
        });
        return jab.build();
    }

    /**
     * Get the entity Id(s) for a many (0 to many) entities - using selected by an
     * column filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the set of entity Ids - using selected by an column filter.
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public final JsonArray find(String parametername, JsonValue parametervalue) throws IOException {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        tablerecords.entrySet().stream().forEach((e) -> {
            if (e.getValue().get(parametername).equals(parametervalue)) {
                jab.add(e.getKey());
            }
        });
        return jab.build();
    }

    /**
     * Get entity data for a single entity - using selected by an column filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the entity object representation
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public final JsonObject getOne(String parametername, JsonValue parametervalue) throws IOException {
        JsonArray get = get(parametername, parametervalue);
        if (get.size() != 1) {
            throw new IOException("Single row expected");
        }
        return get.getJsonObject(0);
    }

    /**
     * Get entity Id for a single entity - using selected by an column filter.
     *
     * @param parametername the filter column name
     * @param parametervalue the filter value
     * @return the entity object representation
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public final JsonValue findOne(String parametername, JsonValue parametervalue) throws IOException {
        JsonArray find = find(parametername, parametervalue);
        if (find.size() != 1) {
            throw new IOException("Single row expected");
        }
        return find.get(0);
    }

    /**
     * Get the next index value for entities which have an explicit ordering
     * column defined.
     *
     * @return the next index value
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public final int findNextIdx() throws IOException {
        dirty = true;
        return nextidx++;
    }

    /**
     * Insert a new entity (set of values) into entity storage.
     *
     * @param values the set of values
     * @return the new entity Id
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public final JsonValue insert(JsonObject values) throws IOException {
        dirty = true;
        int id = nextid++;
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", id);
        values.entrySet().stream().forEach((e) -> {
            job.add(e.getKey(), e.getValue());
        });
        tablerecords.put(id,job.build());
        return JsonUtil.createJsonValue(id);
    }

    /**
     * Update an existing entity in entity storage with a new set of values.
     *
     * @param id the new entity Id
     * @param diffs the set of values to be updated
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public final void update(JsonValue id, JsonObject diffs) throws IOException {
        dirty = true;
        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonObject record = get(id);
        record.entrySet().stream().forEach((e) -> {
            job.add(e.getKey(), e.getValue());
        });
        diffs.entrySet().stream().forEach((e) -> {
            job.add(e.getKey(), e.getValue());
        });
        tablerecords.put(JsonUtil.getIntegerValue(id),job.build());
    }

    /**
     * Delete an entity from entity storage.
     *
     * @param id the entity Id
     * @throws IOException in cases of problems when obtaining, parsing or
     * creating data
     */
    public final void delete(JsonValue id) throws IOException {
        dirty = true;
        if (tablerecords.remove(JsonUtil.getIntegerValue(id)) == null) {
            throw new IOException("Failed to delete record");
        }
    }
}
