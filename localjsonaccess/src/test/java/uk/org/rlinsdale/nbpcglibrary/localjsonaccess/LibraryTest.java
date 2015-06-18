/*
 * Copyright (C) 2015 Richard Linsdale.
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Utilities;
import uk.org.rlinsdale.nbpcglibrary.api.EntityFields;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProviderManager;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
            InputStream in = getClass().getResourceAsStream("/uk/org/rlinsdale/nbpcglibrary/localjsonaccess/Data");
            assert (in != null);
            OutputStream out = new FileOutputStream(new File(dbdir,"Data"));
            assert (out != null);
            FileUtil.copy(in,out);
            in.close();
            out.close();
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
            String[] entitynames = new String[]{
                "Data"
            };
            Properties p = new Properties();
            p.setProperty("connection", dbdir.getAbsolutePath());
            p.setProperty("key", "jsondata");
            p.setProperty("entitypersistenceprovidertype", "local-json");
            p.setProperty("persistenceunitprovidertype", "local-json");
            EntityPersistenceProviderManager.set(p, entitynames);
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
