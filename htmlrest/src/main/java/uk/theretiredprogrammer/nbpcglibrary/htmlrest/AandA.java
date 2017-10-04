/*
 * Copyright 2017 richard linsdale
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

import io.jsonwebtoken.Jwts;
import java.util.Map;
import java.util.stream.Collectors;
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

    private static final Client client = ClientBuilder.newClient();
    private static String jwtoken;
    private static Map<String, Object> appclaims;
    private static int lastAuthStatus = 0;

    public static boolean authenticate(String appkey, String url, String u, String p) {
        return authenticate(appkey, url, new AuthData(u, p));
    }

    private static boolean authenticate(String appkey, String url, AuthData authdata) {
        if (url == null) {
            lastAuthStatus = 600; // private status = bad url
            return false;
        }
        int appkeyl = appkey.length();
        try {
            Response response = client
                    .target(url + "/auth/auth")
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .header("Content-Type", "application/json")
                    .post(Entity.json(authdata), Response.class);
            lastAuthStatus = response.getStatus();
            if (lastAuthStatus != 201) {
                return false;
            }
            jwtoken = response.readEntity(String.class);
            // now check the Token
            response = client
                    .target(url + "/auth/auth")
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .header("authorization", "bearer " + jwtoken)
                    .get();
            lastAuthStatus = response.getStatus();
            if (lastAuthStatus != 200) {
                jwtoken = null;
                return false;
            }
            //parse the unsigned token to extract claims
            appclaims = Jwts.parser()
                    .parseClaimsJwt(jwtoken.substring(0, jwtoken.lastIndexOf('.') + 1))
                    .getBody()
                    .entrySet().stream()
                    .filter(e -> e.getKey().startsWith(appkey))
                    .collect(Collectors.toMap(e -> e.getKey().substring(appkeyl), e -> e.getValue()));
            if (appclaims.size() > 0 ) {
                return true;
            } else {
                lastAuthStatus = 602; // private code - not authorised
                return false;
            }
        } catch (ProcessingException ex) {
            lastAuthStatus = 601; // private code - invalid format
            return false;
        }
    }
    
    public static int getLastAuthStatus() {
        return lastAuthStatus;
    }

    public static String getAuthority(String appkey) {
        return appclaims == null
                ? null
                : (String) appclaims.get(appkey);
    }
    
    public static String getToken() {
        return jwtoken;
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
