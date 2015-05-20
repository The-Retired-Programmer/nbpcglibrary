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
package uk.org.rlinsdale.nbpcglibrary.mysql;

import java.io.IOException;
import java.util.Properties;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProvider;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProviderManager;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 * The test package for the LocalMySqlEntityPersistenceProvider
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class LocalSQLEntityPersistenceProviderTest {

    private static EntityPersistenceProvider instance;

    /**
     * Constructore
     */
    public LocalSQLEntityPersistenceProviderTest() {
    }

    /**
     * Setup class
     *
     * @throws IOException if problems
     */
    @BeforeClass
    public static void setUpClass() throws IOException {

        String[] entitynames = new String[]{
            "Application", "Role", "Permission", "User", "UserRole", "Userpermission"
        };
        Properties p = new Properties();
        p.setProperty("key", "authentication2");
        p.setProperty("connection", "jdbc:mysql://localhost:3306/authentication2");
        p.setProperty("entitypersistenceprovidertype", "localsql");
        p.setProperty("persistenceunitprovidertype", "mysql");
        p.setProperty("user", "developer");
        p.setProperty("password", "dev");
        EntityPersistenceProviderManager.set(p, entitynames);
        instance = EntityPersistenceProviderManager.getEntityPersistenceProvider("authentication2", "Application");
    }

    /**
     * tear down class
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * setup test
     */
    @Before
    public void setUp() {
    }

    /**
     * tear down test
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of instanceDescription method, of class
     * LocalSQLEntityPersistenceProvider.
     */
    @Test
    public void testInstanceDescription() {
        System.out.println("instanceDescription");
        String expResult = "LocalSQLEntityPersistenceProvider[LocalMySQLPersistenceUnitProvider-Application]";
        String result = instance.instanceDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class LocalSQLEntityPersistenceProvider.
     *
     * @throws Exception if problems
     */
    @Test
    public void testGet_int() throws Exception {
        System.out.println("get");
        int id = 48;
        JsonObject result = instance.get(id);
        assertEquals("RL12", result.getString("updatedby"));
        assertEquals("RL12", result.getString("createdby"));
        assertEquals("20150422213408", result.getString("updatedon"));
        assertEquals("20150422213408", result.getString("createdon"));
        assertEquals(48, result.getInt("id"));
        assertEquals("JsonApp60", result.getString("application"));
    }

    /**
     * Test of find method, of class LocalSQLEntityPersistenceProvider.
     *
     * @throws Exception if problems
     */
    @Test
    public void testFind() throws Exception {
        System.out.println("find");
        JsonArray result = instance.find();
        System.out.println(result.toString());
        assertEquals(28, result.size());
    }

    /**
     * Test of get method, of class LocalSQLEntityPersistenceProvider.
     *
     * @throws Exception if problems
     */
    @Test
    public void testGet_String_JsonValue() throws Exception {
        System.out.println("get");
        String parametername = "application";
        JsonValue parametervalue = JsonUtil.createJsonValue("JsonApp83");
        JsonArray result = instance.get(parametername, parametervalue);
        System.out.println(result.toString());
        assertEquals(1, result.size());
        JsonObject job = (JsonObject) result.get(0);
        assertEquals(46, job.getInt("id"));
    }

    /**
     * Test of getOne method, of class LocalSQLEntityPersistenceProvider.
     *
     * @throws Exception if problems
     */
    @Test
    public void testGetOne() throws Exception {
        System.out.println("getOne");
        String parametername = "application";
        JsonValue parametervalue = JsonUtil.createJsonValue("JsonApp83");
        JsonObject result = instance.getOne(parametername, parametervalue);
        assertEquals(46, result.getInt("id"));
    }

    /**
     * Test of findNextIdx method, of class LocalSQLEntityPersistenceProvider.
     *
     * @throws Exception if problems
     */
    @Test(expected = IOException.class)
    public void testFindNextIdx() throws Exception {
        System.out.println("findNextIdx");
        int result = instance.findNextIdx();
        fail("Exception should have been thrown in this case.");
    }
}
