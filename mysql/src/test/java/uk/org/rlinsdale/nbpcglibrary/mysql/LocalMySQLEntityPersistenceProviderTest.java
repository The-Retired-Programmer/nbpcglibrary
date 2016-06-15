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
        p.setProperty("key", "testingmysqllibrary");
        p.setProperty("connection", "jdbc:mysql://localhost:3306/testingmysqllibrary");
        p.setProperty("entitypersistenceprovidertype", "local-mysql");
        p.setProperty("persistenceunitprovidertype", "mysql");
        EntityPersistenceProviderManager.init(p);
        instance = EntityPersistenceProviderManager.getEntityPersistenceProvider("testingmysqllibrary", "TestTable");
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
        String expResult = "LocalMySQLAutoIDEntityPersistenceProvider[LocalMySQLPersistenceUnitProvider-TestTable]";
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
        int id = 2;
        EntityFields result = instance.get(id);
        assertEquals("test", result.get("updatedby"));
        assertEquals("test", result.get("createdby"));
        assertEquals("20000101000000", result.get("updatedon"));
        assertEquals("20000101000000", result.get("createdon"));
        assertEquals(id, result.get("id"));
        assertEquals("app2", result.get("application"));
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
        assertEquals(3, result.size());
    }

    /**
     * Test of get method, of class LocalSQLEntityPersistenceProvider.
     *
     * @throws Exception if problems
     */
    @Test
    public void testGet_String_JsonValue() throws Exception {
        System.out.println("get");
        List<EntityFields> result = instance.get("application", "app1");
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).get("id"));
    }

    /**
     * Test of getOne method, of class LocalSQLEntityPersistenceProvider.
     *
     * @throws Exception if problems
     */
    @Test
    public void testGetOne() throws Exception {
        System.out.println("getOne");
        EntityFields result = instance.getOne("application", "app3");
        assertEquals(3, result.get("id"));
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
