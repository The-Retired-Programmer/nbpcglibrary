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
package uk.theretiredprogrammer.nbpcglibrary.filerest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.stream.JsonParsingException;
import uk.theretiredprogrammer.nbpcglibrary.api.Rest;
import uk.theretiredprogrammer.nbpcglibrary.api.Settings;
import uk.theretiredprogrammer.nbpcglibrary.api.IdTimestampBaseEntity;

/**
 * EntityPersistenceProvider Class for access local Json File based persistent
 * storage
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the base Entity class being managed
 */
public class FileRest<E extends IdTimestampBaseEntity> implements Rest<E> {

    private String name;
    private int nextid;
    private int nextidx;
    private final Map<Integer, E> tablerecords = new HashMap<>();
    private boolean dirty = false;
    //
    private final String dblocation;
    private final String tablename;
    private final String ordercolumn;
    private File databasefolder;
    private File dbfile;
    private final Class<E> responseEntityClass;
    private final Function<E, E> copyfunction;

    public FileRest(String dblocation, String tablename, Class<E> responseEntityClass,
            Function<E, E> copyfunction) {
        this(dblocation, tablename, null, responseEntityClass, copyfunction);
    }

    public FileRest(String dblocation, String tablename, String ordercolumn,
            Class responseEntityClass, Function<E, E> copyfunction) {
        this.dblocation = dblocation;
        this.tablename = tablename;
        this.ordercolumn = ordercolumn;
        this.responseEntityClass = responseEntityClass;
        this.copyfunction = copyfunction;
    }

    @Override
    public boolean open() {
        try {
            databasefolder = new File(dblocation);
            if (!databasefolder.isDirectory()) {
                return false;
            }
            dbfile = new File(databasefolder, tablename);
            if (!dbfile.canWrite()) {
                return false;
            }
            JsonObject tableJson;
            try (JsonReader jsonReader = Json.createReader(new FileReader(dbfile))) {
                tableJson = jsonReader.readObject();
            }
            nextid = tableJson.getInt("nextid");
            nextidx = tableJson.getInt("nextidx");
            name = tableJson.getString("name");
            if (!name.equals(tablename)) {
                return false;
            }
            //
            JsonArray records = tableJson.getJsonArray("entities");
            for (JsonValue record : records) {
                JsonObject rec = (JsonObject) record;
                E e = createEntity(rec);
                if (e == null) {
                    return false;
                }
                tablerecords.put(e.getId(), e);
            }
            return true;
        } catch (JsonParsingException | IOException ex) {
            return false;
        }
    }

    @Override
    public boolean close() {
        if (dirty) {
            try {
                JsonObjectBuilder job = Json.createObjectBuilder();
                job.add("name", name);
                job.add("nextid", nextid);
                job.add("nextidx", nextidx);
                JsonArrayBuilder rab = Json.createArrayBuilder();
                for (E entity : tablerecords.values()) {
                    JsonObject jo = createJsonObject(entity);
                    if (jo == null) {
                        return false;
                    }
                    rab.add(jo);
                }
                job.add("entities", rab.build());
                try (JsonWriter jsonWriter = Json.createWriter(new FileWriter(dbfile))) {
                    jsonWriter.writeObject(job.build());
                }
                dirty = false;
                return true;
            } catch (IOException ex) {
                return false;
            }
        }
        return true;
    }

    private E createEntity(JsonObject jsonobject) {
        try {
            StringWriter sw = new StringWriter();
            JsonWriter jw = Json.createWriter(sw);
            jw.writeObject(jsonobject);
            String jsonString = sw.toString();
            ObjectMapper mapper = new ObjectMapper();
            //JSON from String to Object
            return mapper.readValue(jsonString, responseEntityClass);
        } catch (IOException ex) {
            return null;
        }
    }

    private JsonObject createJsonObject(E e) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //Object to JSON object
            String jsonentity = mapper.writeValueAsString(e);
            StringReader sr = new StringReader(jsonentity);
            JsonReader jr = Json.createReader(sr);
            return jr.readObject();
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public E get(int id) {
        return copyfunction.apply(tablerecords.get(id));
    }

    @Override
    public final List<E> getAll() {
        return tablerecords.values().stream().map(e -> copyfunction.apply(e))
                .collect(Collectors.toList());
    }

    @Override
    public List<E> getMany(String filtername, int filtervalue) {
        return tablerecords.values().stream()
                .filter(e -> e.isMatch(filtername, filtervalue))
                .map(e -> copyfunction.apply(e))
                .collect(Collectors.toList());
    }

//    @Override
//    public final List<E> get(String parametername, Object parametervalue) {
//        List<En> el = new ArrayList<>();
//        tablerecords.values().stream().forEach((ef) -> {
//            if (ef.get(parametername).equals(parametervalue)) {
//                el.add(copy(e));
//            }
//        });
//        return el;
//    }
//
//    @Override
//    public final List<K> find(String parametername, Object parametervalue) {
//        List<K> pks = new ArrayList<>();
//        tablerecords.entrySet().stream().forEach((e) -> {
//            if (e.getValue().get(parametername).equals(parametervalue)) {
//                pks.add(e.getKey());
//            }
//        });
//        return pks;
//    }
//
//    @Override
//    public final EntityFields getOne(String parametername, Object parametervalue) {
//        List<EntityFields> get = get(parametername, parametervalue);
//        if (get.size() != 1) {
//            throw new LogicException("Single row expected");
//        }
//        return get.get(0);
//    }
//
//    @Override
//    public final K findOne(String parametername, Object parametervalue) {
//        List<K> find = find(parametername, parametervalue);
//        if (find.size() != 1) {
//            throw new LogicException("Single row expected");
//        }
//        return find.get(0);
//    }
    private final int findNextIdx() {
        dirty = true;
        return nextidx++;
    }

    @Override
    public final E create(E e) {
        dirty = true;
        int id = nextid++;
        e.setId(id);
        addTimestampInfo(e);
        tablerecords.put(id, copyfunction.apply(e));
        return e;
    }

    private void addTimestampInfo(E entity) {
        String user = Settings.get("Usercode", "????");
        SimpleDateFormat datetime_ISO8601 = new SimpleDateFormat("yyyyMMddHHmmss");
        datetime_ISO8601.setLenient(false);
        String when = datetime_ISO8601.format(new Date());
        entity.setCreatedby(user);
        entity.setCreatedon(when);
        entity.setUpdatedby(user);
        entity.setUpdatedon(when);
    }

    @Override
    public final E update(int id, E e) {
        dirty = true;
        updateTimestampInfo(e);
        tablerecords.put(id, copyfunction.apply(e));
        return e;
    }

    private void updateTimestampInfo(E entity) {
        String user = Settings.get("Usercode", "????");
        SimpleDateFormat datetime_ISO8601 = new SimpleDateFormat("yyyyMMddHHmmss");
        datetime_ISO8601.setLenient(false);
        String when = datetime_ISO8601.format(new Date());
        entity.setUpdatedby(user);
        entity.setUpdatedon(when);
    }

    @Override
    public E patch(int id, Map<String, Object> updates) {
        // temporary until javaee8
        throw new RuntimeException("Patch not implemented over HTML Rest");
    }

    @Override
    public final boolean delete(int id) {
        dirty = true;
        return tablerecords.remove(id) != null;
    }
}
