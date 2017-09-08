/*
 * Copyright 2017 richard.
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
package uk.theretiredprogrammer.nbpcglibrary.lifecycle;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author richard
 */
public class JacksonImplementationTest {
    
    
    public JacksonImplementationTest() {
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

    @Test
    public void testToJson() {
        System.out.println("toJson");
        
        JacksonImplementation impl = new JacksonImplementation();
        
        JacksonImplementationEntity app = new JacksonImplementationEntity(12,"photogallery","rl12");
        
        String res = impl.toJson(app);
        
        String exp = "{\"name\":\"photogallery\",\"key\":12,\"createdBy\":\"rl12\"}";
        
        assertEquals(exp, res);
    }
    
    @Test
    public void testFromJson() {
        System.out.println("fromJson");
        JacksonImplementation impl = new JacksonImplementation();
        JacksonImplementationEntity app = impl.fromJson("{\"name\":\"photogallery\",\"key\":12,\"createdBy\":\"rl12\"}");
        assertEquals("photogallery", app.getName());
        assertEquals(12, app.getKey());
        assertEquals("rl12", app.getCreatedBy());
    }
    
}
