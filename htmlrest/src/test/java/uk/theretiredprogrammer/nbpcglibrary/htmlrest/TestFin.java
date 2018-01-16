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

import java.util.List;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.Test;

/**
 *
 * @author richard
 */
public class TestFin {
    
    /**
     * Test of Fin provider db entries .
     */
    @Test
    public void finProviderGetAll() {
        System.out.println("Fin Provider GetAll");
        String url = "http://localhost:8080";
        String u = "richard@rlinsdale.uk";
        String p = "password";
        assert (authenticate(url, u, p));
        List<Provider> res = providerGetAll(url, jwtoken);
        assert(res != null && 14 == res.size());
    }

    private static final Client CLIENT = ClientBuilder.newClient();
    private static String jwtoken;
    private static int lastAuthStatus = 0;

    private List<Provider> providerGetAll(String url, String auth) {
        if (auth != null) {
            if (url == null) {
                lastAuthStatus = 600; // private status = bad url
                return null;
            }
            try {
                // check the Token
                Response response = CLIENT
                        .target(url + "/fin/providers/")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .header("authorization", "bearer " + auth)
                        .get();
                lastAuthStatus = response.getStatus();
                if (lastAuthStatus == 200) {
                    return response.readEntity(new GenericType<List<Provider>>() {
                    });
                } else {
                    return null;
                }
            } catch (ProcessingException ex) {
                return null;
            }
        }
        return null;
    }

    public static boolean authenticate(String url, String u, String p) {
        return authenticate(url, new AuthData(u, p));
    }

    private static boolean authenticate(String url, AuthData authdata) {
        if (url == null) {
            lastAuthStatus = 600; // private status = bad url
            return false;
        }
        try {
            Response response = CLIENT
                    .target(url + "/auth/auth")
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .header("Content-Type", "application/json")
                    .post(Entity.json(authdata), Response.class);
            lastAuthStatus = response.getStatus();
            if (lastAuthStatus != 201) {
                return false;
            }
            jwtoken = response.readEntity(String.class);
            return true;
        } catch (ProcessingException ex) {
            lastAuthStatus = 601; // private code - invalid format
            return false;
        }
    }

    private static class AuthData {

        public String u;
        public String p;

        public AuthData(String u, String p) {
            this.p = p;
            this.u = u;
        }
    }
}
