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
package uk.theretiredprogrammer.nbpcglibrary.mysql;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityFields;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityPersistenceProvider;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityPersistenceProviderManager;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;

/**
 * The test package for the LocalMySqlEntityPersistenceProvider
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
