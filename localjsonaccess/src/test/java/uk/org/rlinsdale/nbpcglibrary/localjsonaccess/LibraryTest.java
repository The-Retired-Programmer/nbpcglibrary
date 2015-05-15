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

import java.io.IOException;
import java.util.Properties;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProviderManager;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class LibraryTest {
    
    public LibraryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
     */
    @Test
    public void testLibrary() {
        System.out.println("Test Json Access Library");
        try {
            String[] entitynames = new String[] {
                "Data"
            };
        Properties p = new Properties();
        p.put("connection","/home/richard/Documents/JsonTestDB");
        p.setProperty("key", "jsondata");
        p.setProperty("entitypersistenceprovidertype", "local-json");
        p.setProperty("persistenceunitprovidertype", "local-json");
        EntityPersistenceProviderManager.set(p, entitynames);
        LocalJsonEntityPersistenceProvider epp = (LocalJsonEntityPersistenceProvider) EntityPersistenceProviderManager.getEntityPersistenceProvider("jsondata", "Data");
        //LocalJsonPersistenceUnitProvider pup = new LocalJsonPersistenceUnitProvider(p);
//        System.out.println(pup.getName()+ " is "+(pup.isOperational()?"Operational":"Non operational"));
//        System.out.println("Instance Description: "+instance.instanceDescription());
            //
            System.out.println("Instance Description: "+epp.instanceDescription());
            System.out.println("get(): "+epp.get().toString());
            System.out.println("get(1): "+epp.get(1).toString());
            System.out.println("get(\"id\",1): "+epp.get("id",JsonUtil.createJsonValue(1)).toString());
            System.out.println("getOne(\"id\",1): "+epp.getOne("id",JsonUtil.createJsonValue(1)).toString());
            System.out.println("find(): "+epp.find().toString());
            System.out.println("find(\"id\",1): "+epp.find("id",JsonUtil.createJsonValue(1)).toString());
            System.out.println("findOne(\"id\",1): "+epp.findOne("id",JsonUtil.createJsonValue(1)).toString());
            //
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("description", "my first insert");
            int newid = epp.insert(job.build());
            System.out.println("insert(...) returns "+newid);
            System.out.println("get(): "+epp.get().toString());
            System.out.println("get(newid): "+epp.get(newid).toString());
            //
            JsonObjectBuilder job2 = Json.createObjectBuilder();
            job2.add("description", "my first insert - updated");
            epp.update(newid, job2.build());
            //
            epp.delete(newid-1);
            epp.persist();
            assert(true);
            System.out.println("Test Json Access Library completed");
            
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }
}
