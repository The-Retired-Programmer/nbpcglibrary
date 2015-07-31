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
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.org.rlinsdale.nbpcglibrary.api.EntityFields;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProvider;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProviderManager;
import uk.org.rlinsdale.nbpcglibrary.api.LogicException;

/**
 * The test package for the LocalMySqlEntityPersistenceProvider
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class LocalMySQLEntityPersistenceProviderTest {

    private static EntityPersistenceProvider<Integer> instance;

    /**
     * Constructor
     */
    public LocalMySQLEntityPersistenceProviderTest() {
    }

    /**
     * Setup class
     *
     * @throws IOException if problems
     */
    @BeforeClass
    public static void setUpClass() throws IOException {

        Properties p = new Properties();
        p.setProperty("key", "authentication2");
        p.setProperty("connection", "jdbc:mysql://localhost:3306/authentication2");
        p.setProperty("entitypersistenceprovidertype", "local-mysql");
        p.setProperty("persistenceunitprovidertype", "mysql");
        p.setProperty("user", "developer");
        p.setProperty("password", "dev");
        EntityPersistenceProviderManager.init(p);
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
        String expResult = "LocalMySQLAutoIDEntityPersistenceProvider[LocalMySQLPersistenceUnitProvider-Application]";
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
        EntityFields result = instance.get(id);
        assertEquals("RL12", result.get("updatedby"));
        assertEquals("RL12", result.get("createdby"));
        assertEquals("20150422213408", result.get("updatedon"));
        assertEquals("20150422213408", result.get("createdon"));
        assertEquals(id, result.get("id"));
        assertEquals("JsonApp60", result.get("application"));
    }

    /**
     * Test of find method, of class LocalSQLEntityPersistenceProvider.
     *
     * @throws Exception if problems
     */
    @Test
    public void testFind() throws Exception {
        System.out.println("find");
        List<Integer> result = instance.find();
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
        List<EntityFields> result = instance.get("application", "JsonApp83");
        assertEquals(1, result.size());
        assertEquals(46, result.get(0).get("id"));
    }

    /**
     * Test of getOne method, of class LocalSQLEntityPersistenceProvider.
     *
     * @throws Exception if problems
     */
    @Test
    public void testGetOne() throws Exception {
        System.out.println("getOne");
        EntityFields result = instance.getOne("application", "JsonApp83");
        assertEquals(46, result.get("id"));
    }

    /**
     * Test of findNextIdx method, of class LocalSQLEntityPersistenceProvider.
     *
     * @throws Exception if problems
     */
    @Test(expected = LogicException.class)
    public void testFindNextIdx() throws Exception {
        System.out.println("findNextIdx");
        int result = instance.findNextIdx();
        fail("Exception should have been thrown in this case.");
    }
}
