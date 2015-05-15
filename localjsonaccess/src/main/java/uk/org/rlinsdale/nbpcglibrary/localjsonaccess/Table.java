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

    public String getname() {
        return name;
    }

    public JsonObject get(JsonValue id) throws IOException {
        JsonObject record = tablerecords.get(JsonUtil.getIntegerValue(id));
        if (record == null) {
            throw new IOException("Can't find requested record");
        }
        return record;
    }

    public final JsonArray get() throws IOException {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        tablerecords.values().stream().forEach((jo) -> {
            jab.add(jo);
        });
        return jab.build();
    }

    public final JsonArray find() throws IOException {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        tablerecords.keySet().stream().forEach((key) -> {
            jab.add(key);
        });
        return jab.build();
    }

    public final JsonArray get(String parametername, JsonValue parametervalue) throws IOException {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        tablerecords.values().stream().forEach((jo) -> {
            if (jo.get(parametername).equals(parametervalue)) {
                jab.add(jo);
            }
        });
        return jab.build();
    }

    public final JsonArray find(String parametername, JsonValue parametervalue) throws IOException {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        tablerecords.entrySet().stream().forEach((e) -> {
            if (e.getValue().get(parametername).equals(parametervalue)) {
                jab.add(e.getKey());
            }
        });
        return jab.build();
    }

    public final JsonObject getOne(String parametername, JsonValue parametervalue) throws IOException {
        JsonArray get = get(parametername, parametervalue);
        if (get.size() != 1) {
            throw new IOException("Single row expected");
        }
        return get.getJsonObject(0);
    }

    public final JsonValue findOne(String parametername, JsonValue parametervalue) throws IOException {
        JsonArray find = find(parametername, parametervalue);
        if (find.size() != 1) {
            throw new IOException("Single row expected");
        }
        return find.get(0);
    }

    public final int findNextIdx() throws IOException {
        dirty = true;
        return nextidx++;
    }

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

    public final void delete(JsonValue id) throws IOException {
        dirty = true;
        if (tablerecords.remove(JsonUtil.getIntegerValue(id)) == null) {
            throw new IOException("Failed to delete record");
        }
    }
}
