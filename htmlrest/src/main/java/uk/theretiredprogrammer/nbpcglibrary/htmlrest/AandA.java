/*
 * Copyright 2017-2018 richard linsdale
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

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author richard linsdale (richard @ theretiredprogrammer.uk)
 */
public class AandA {

    private final Client CLIENT = ClientBuilder.newClient();
    private final String url;
    private final String rolekey;
    //
    private int lastAuthStatus = 0;
    private String lastAuthMessage;
    //
    private String jwtoken;
    private String role;
    private String givenname;

    public AandA(String url, String rolekey) {
        this.url = url;
        this.rolekey = rolekey;
    }

    public boolean authenticate(String u, String p) {
        return authenticateUser(u, p) && authenticateRole() && getGiven_name();
    }

    private boolean authenticateUser(String u, String p) {
        if (url == null) {
            lastAuthStatus = 600; // private status = bad url
            return false;
        }
        AuthData authdata = new AuthData(u, p);
        String jsonAuthdata = authdata.toJson();
        try {
            Response response = CLIENT
                    .target(url + "/auth/auth")
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .header("Content-Type", "application/json")
                    .post(Entity.entity(jsonAuthdata,MediaType.APPLICATION_JSON));
            lastAuthStatus = response.getStatus();
            if (lastAuthStatus == 201) {
                jwtoken = response.readEntity(String.class);
                return true;
            } else {
                lastAuthMessage = response.readEntity(String.class);
                return false;
            }
        } catch (ProcessingException ex) {
            lastAuthStatus = 601; // private code - invalid format
        }
        return false;
    }

    private boolean authenticateRole() {
        try {
            Response response = CLIENT
                    .target(url + "/auth/auth/" + rolekey)
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .header("authorization", "bearer " + jwtoken)
                    .get();
            lastAuthStatus = response.getStatus();
            if (lastAuthStatus == 200) {
                role = response.readEntity(String.class);
                return true;
            } else {
                lastAuthMessage = response.readEntity(String.class);
                return false;
            }
        } catch (ProcessingException ex) {
            lastAuthStatus = 601; // private code - invalid format
        }
        return false;
    }

    private boolean getGiven_name() {
        try {
            Response response = CLIENT
                    .target(url + "/auth/auth/given_name")
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .header("authorization", "bearer " + jwtoken)
                    .get();
            lastAuthStatus = response.getStatus();
            if (lastAuthStatus == 200) {
                givenname = response.readEntity(String.class);
                return true;
            } else {
                lastAuthMessage = response.readEntity(String.class);
                return false;
            }
        } catch (ProcessingException ex) {
            lastAuthStatus = 601; // private code - invalid format
        }
        return false;
    }

    public int getLastAuthStatus() {
        return lastAuthStatus;
    }

    public String getLastAuthMessage() {
        return lastAuthMessage;
    }

    public String getRole() {
        return role;
    }

    public String getJWToken() {
        return jwtoken;
    }

    private class AuthData {

        public String u;
        public String p;

        public AuthData(String u, String p) {
            this.p = p;
            this.u = u;
        }

        public String toJson() {
            return "{\"u\":\"" + u + "\",\"p\":\"" + p + "\"}";
        }
    }
}
