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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonParsingException;
import uk.theretiredprogrammer.nbpcglibrary.api.PersistenceUnitProvider;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;

/**
 * Class implementing Local access to Json Data.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class LocalJsonPersistenceUnitProvider implements PersistenceUnitProvider {

    private final String dbpath;
    private final File databasefolder;
    private File dbfile;
    private final boolean operational;

    /**
     * Constructor.
     *
     * @param p the properties for this database
     */
    public LocalJsonPersistenceUnitProvider(Properties p) {
        dbpath = (String) p.get("connection");
        databasefolder = new File(dbpath);
        operational = databasefolder.isDirectory();
    }

    /**
     * load a table into memory
     *
     * @param tablename the table name
     * @return the table data
     * @throws IOException if problems reading or parsing data
     */
    public JsonObject load(String tablename) throws IOException {
        dbfile = new File(databasefolder, tablename);
        if (!dbfile.canWrite()) {
            throw new IOException("Table file missing - " + tablename);
        }
        JsonObject jo;
        try {
            try (JsonReader jsonReader = Json.createReader(new FileReader(dbfile))) {
                jo = jsonReader.readObject();
            }
        } catch (JsonParsingException ex) {
            throw new IOException("Json Parsing Exception - " + ex.getMessage());
        }
        return jo;
    }

    /**
     * Persist in-memory table into file store (json)
     *
     * @param tableobject the table object
     * @throws IOException if problem writing the data
     */
    public void persist(JsonObject tableobject) throws IOException {
        try (JsonWriter jsonWriter = Json.createWriter(new FileWriter(dbfile))) {
            jsonWriter.writeObject(tableobject);
        }
    }

    @Override
    public boolean isOperational() {
        return operational;
    }

    @Override
    public String getName() {
        return "local-json";
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, dbpath);
    }
}
