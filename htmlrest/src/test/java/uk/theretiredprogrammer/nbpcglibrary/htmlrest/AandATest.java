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
package uk.theretiredprogrammer.nbpcglibrary.htmlrest;

import uk.theretiredprogrammer.nbpcglibrary.htmlrest.AandA;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author richard
 */
public class AandATest {
    
    public AandATest() {
    }
    
    /**
     * Test of authenticate method, of class AandA.
     */
    @Test
    public void testAuthenticate() {
        System.out.println("authenticate");
        String appkey = "trp.bk.";
        String url = "http://localhost:8080";
        String u = "richard@rlinsdale.uk";
        String p = "password";
        assert(AandA.authenticate(appkey, url, u, p));
    }
    
    /**
     * Test of authenticate method, of class AandA.
     */
    @Test
    public void testAuthenticateFail() {
        System.out.println("authenticate fail");
        String appkey = "trp.bk.";
        String url = "http://localhost:8080";
        String u = "richard@rlinsdale.uk";
        String p = "wordpass";
        assert(!AandA.authenticate(appkey, url, u, p));
    }
    
     /**
     * Test of authenticate method, of class AandA.
     */
    @Test
    public void testAuthenticateAuthoriseFailure() {
        System.out.println("authenticate - authorise failure");
        String appkey = "trp.xxx.";
        String url = "http://localhost:8080";
        String u = "richard@rlinsdale.uk";
        String p = "password";
        assert(!AandA.authenticate(appkey, url, u, p));
    }
    
     /**
     * Test of authenticate method, of class AandA.
     */
    @Test
    public void testAuthenticateConnectionFailure() {
        System.out.println("authenticate - connection failure");
        String appkey = "trp.bk.";
        String url = "http://localhost:9090";
        String u = "richard@rlinsdale.uk";
        String p = "password";
        assert(!AandA.authenticate(appkey, url, u, p));
    }
    
     /**
     * Test of authenticate method, of class AandA.
     */
    @Test
    public void testAuthenticateURLUndefined() {
        System.out.println("authenticate - url undefined");
        String appkey = "trp.bk.";
        String url = null;
        String u = "richard@rlinsdale.uk";
        String p = "password";
        assert(!AandA.authenticate(appkey, url, u, p));
    }

    /**
     * Test of getStringClaim method, of class AandA.
     */
    @Test
    public void testGetAuthorityMissing() {
        System.out.println("getAuthorityMissing");
        String appkey = "trp.bk.";
        String url = "http://localhost:8080";
        String u = "richard@rlinsdale.uk";
        String p = "password";
        assert(AandA.authenticate(appkey, url, u, p));
        assertNull(AandA.getAuthority("undefinedkey"));
    }
    
    /**
     * Test of getStringClaim method, of class AandA.
     */
    @Test
    public void testGetAuthority() {
        System.out.println("getAuthority");
        String appkey = "trp.bk.";
        String url = "http://localhost:8080";
        String u = "richard@rlinsdale.uk";
        String p = "password";
        assert(AandA.authenticate(appkey, url, u, p));
        assertEquals("admin", AandA.getAuthority("client"));
    }
}