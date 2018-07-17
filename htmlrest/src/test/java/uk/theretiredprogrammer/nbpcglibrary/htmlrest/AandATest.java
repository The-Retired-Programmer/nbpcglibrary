/*
 * Copyright 2017-2018 richard.
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

import org.junit.Test;

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
        String appkey = "acm.role";
        String url = "http://localhost:8080";
        String u = "richard@rlinsdale.uk";
        String p = "password";
        AandA auth = new AandA(url, appkey);
        assert(auth.authenticate(u, p));
    }
    
    /**
     * Test of authenticate method, of class AandA.
     */
    @Test
    public void testAuthenticateFail() {
        System.out.println("authenticate fail");
        String appkey = "acm.role";
        String url = "http://localhost:8080";
        String u = "richard@rlinsdale.uk";
        String p = "wordpass";
        AandA auth = new AandA(url, appkey);
        assert(!auth.authenticate(u, p));
    }
    
     /**
     * Test of authenticate method, of class AandA.
     */
    @Test
    public void testAuthenticateAuthoriseFailure() {
        System.out.println("authenticate - authorise failure");
        String appkey = "acm.xxx";
        String url = "http://localhost:8080";
        String u = "richard@rlinsdale.uk";
        String p = "password";
        AandA auth = new AandA(url, appkey);
        assert(!auth.authenticate(u, p));
    }
    
     /**
     * Test of authenticate method, of class AandA.
     */
    @Test
    public void testAuthenticateConnectionFailure() {
        System.out.println("authenticate - connection failure");
        String appkey = "acm.role";
        String url = "http://localhost:9090";
        String u = "richard@rlinsdale.uk";
        String p = "password";
        AandA auth = new AandA(url, appkey);
        assert(!auth.authenticate(u, p));
    }
    
     /**
     * Test of authenticate method, of class AandA.
     */
    @Test
    public void testAuthenticateURLUndefined() {
        System.out.println("authenticate - url undefined");
        String appkey = "acm.role";
        String url = null;
        String u = "richard@rlinsdale.uk";
        String p = "password";
        AandA auth = new AandA(url, appkey);
        assert(!auth.authenticate(u, p));
    }

    /**
     * Test of getStringClaim method, of class AandA.
     */
    @Test
    public void testGetRole() {
        System.out.println("getRole");
        String appkey = "acm.role";
        String url = "http://localhost:8080";
        String u = "richard@rlinsdale.uk";
        String p = "password";
        AandA auth = new AandA(url, appkey);
        assert(auth.authenticate(u, p));
        assert(auth.getRole().equals("admin"));
    }
    
}
