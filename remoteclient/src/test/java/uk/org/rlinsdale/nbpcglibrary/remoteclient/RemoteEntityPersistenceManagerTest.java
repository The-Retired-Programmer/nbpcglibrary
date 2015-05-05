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
package uk.org.rlinsdale.nbpcglibrary.remoteclient;

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
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceManager;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceManagerManager;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class RemoteEntityPersistenceManagerTest {
    
    private static EntityPersistenceManager instance;
    
    public RemoteEntityPersistenceManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        try {
            String[] entitynames = new String[] {
                "Application", "Role", "Permission", "User", "UserRole", "Userpermission"
            };
            Properties p = new Properties();
            p.setProperty("key", "authentication2");
            p.setProperty("connection", "http://localhost:8080/RemoteAuthentication2");
            p.setProperty("entitypersistencemanagertype", "remote");
            p.setProperty("dataaccessmanagertype", "remote");
            EntityPersistenceManagerManager.set(p, entitynames);
            instance = EntityPersistenceManagerManager.getEntityPersistenceManager("authentication2", "Application");
        } catch (IOException ex) {
            //???? should never happen as I am defining the key properly
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of instanceDescription method, of class RemoteEntityPersistenceManager.
     */
    @Test
    public void testInstanceDescription() {
        System.out.println("instanceDescription");
        String expResult = "RemoteEntityPersistenceManager[RemoteDataAccessManager-Application]";
        String result = instance.instanceDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class RemoteEntityPersistenceManager.
     * @throws java.lang.Exception
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
     * Test of find method, of class RemoteEntityPersistenceManager.
     * @throws java.lang.Exception
     */
    @Test
    public void testFind() throws Exception {
        System.out.println("find");
        JsonArray result = instance.find();
        System.out.println(result.toString());
        assertEquals(28, result.size());
    }

    /**
     * Test of get method, of class RemoteEntityPersistenceManager.
     * @throws java.lang.Exception
     */
    @Test
    public void testGet_String_JsonValue() throws Exception {
        System.out.println("get");
        String parametername = "application";
        JsonValue parametervalue = JsonUtil.createJsonValue("JsonApp83");
        JsonArray result = instance.get(parametername, parametervalue);
        System.out.println(result.toString());
        assertEquals(1, result.size());
        JsonObject job = (JsonObject)result.get(0);
        assertEquals(46, job.getInt("id"));
    }

    /**
     * Test of getOne method, of class RemoteEntityPersistenceManager.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetOne() throws Exception {
        System.out.println("getOne");
        String parametername = "application";
        JsonValue parametervalue =JsonUtil.createJsonValue("JsonApp83");
        JsonObject result = instance.getOne(parametername, parametervalue);
        assertEquals(46, result.getInt("id"));
    }

    /**
     * Test of findNextIdx method, of class RemoteEntityPersistenceManager.
     * @throws java.lang.Exception
     */
    @Test(expected = IOException.class)
    public void testFindNextIdx() throws Exception {
        System.out.println("findNextIdx");
        int result = instance.findNextIdx();
        fail("Exception should have been thrown in this case.");
    }
    
}
