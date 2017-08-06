/*
 * Copyright 2015-2017 Richard Linsdale.
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.filesystems.FileUtil;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityFields;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityPersistenceProviderManager;

/**
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class LibraryTest {
    
    private File dbdir;

    /**
     * Constructor
     */
    public LibraryTest() {
    }

    /**
     * Setup the Class
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Tear down the class
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Setup the test
     */
    @Before
    public void setUp() {
            String tempDir = System.getProperty("java.io.tmpdir");
            dbdir = new File(tempDir, "JsonTestDatabase");
            if (!dbdir.exists()){
                dbdir.mkdir();
            }
        try {
                try (OutputStream out = new FileOutputStream(new File(dbdir,"Data"))) {
                    assert (out != null);
                    try (InputStream in = getClass().getResourceAsStream("/uk/theretiredprogrammer/nbpcglibrary/localjsonaccess/Data")) {
                        assert (in != null);
                        FileUtil.copy(in,out);
                    }
                }
        } catch (IOException ex) {
            fail("Could not copy test database - "+ ex.getMessage());
        }
    }

    /**
     * Tear down the test
     */
    @After
    public void tearDown() {
    //    new File(tmpdir, "Data").delete();
    //    tmpdir.delete();
        dbdir = null;
        
    }

    /**
     * A basic test of the functionality of the module - not a true unit test.
     */
    @Test
    public void testLibrary() {
        System.out.println("Test Json Access Library");
        try {
            Properties p = new Properties();
            p.setProperty("connection", dbdir.getAbsolutePath());
            p.setProperty("key", "jsondata");
            p.setProperty("entitypersistenceprovidertype", "local-json");
            p.setProperty("persistenceunitprovidertype", "local-json");
            EntityPersistenceProviderManager.init(p);
            LocalJsonAutoIDEntityPersistenceProvider epp = (LocalJsonAutoIDEntityPersistenceProvider) EntityPersistenceProviderManager.getEntityPersistenceProvider("jsondata", "Data");
            //
            System.out.println("Instance Description: " + epp.instanceDescription());
            System.out.println("get(): " + epp.get().toString());
            System.out.println("get(1): " + epp.get(1).toString());
            System.out.println("get(\"id\",1): " + epp.get("id", 1).toString());
            System.out.println("getOne(\"id\",1): " + epp.getOne("id", 1).toString());
            System.out.println("find(): " + epp.find().toString());
            System.out.println("find(\"id\",1): " + epp.find("id", 1).toString());
            System.out.println("findOne(\"id\",1): " + epp.findOne("id", 1).toString());
            //
            EntityFields ef = new EntityFields();
            ef.put("description", "my first insert");
            EntityFields res = epp.insert(ef);
            int newid = (Integer) res.get("id");
            System.out.println("insert(...) returns " + res);
            System.out.println("get(): " + epp.get().toString());
            System.out.println("get(newid): " + epp.get(newid).toString());
            //
            EntityFields ef2 = new EntityFields();
            ef2.put("description", "my first insert - updated");
            res = epp.update(newid, ef2);
            System.out.println("update(...) returns " + res);
            //
            epp.delete(newid - 1);
            System.out.println("after delete: " + epp.get().toString());
            epp.persist();
            assert (true);
            System.out.println("Test Json Access Library completed");

        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }
}
