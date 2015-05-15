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
package uk.org.rlinsdale.nbpcglibrary.localjsonaccess;

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
import uk.org.rlinsdale.nbpcglibrary.api.PersistenceUnitProvider;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * Class implementing Local access to Json Data.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
     * @throws java.io.IOException
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
     *
     *
     * @param tableobject
     * @throws java.io.IOException
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
