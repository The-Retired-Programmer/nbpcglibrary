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
package uk.theretiredprogrammer.nbpcglibrary.lifecycle.auth;

import io.jsonwebtoken.Jwts;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author richard linsdale (richard @ theretiredprogrammer.uk)
 */
public class AandA {

    public static enum AUTHENTICATION_RESULT {
        OK,
        NOT_AUTHENTICATED,
        NOT_AUTHORISED,
        TOKEN_VALIDATION_FAILURE,
        URL_UNDEFINED,
        CONNECTION_FAILURE
    }

    public static String jwtoken;
    public static Map<String,Object> appclaims;

    public static AUTHENTICATION_RESULT authenticate(String appkey, String url, String u, String p) {
        return authenticate(appkey, url, new AuthData(u, p));
    }
    
    public static AUTHENTICATION_RESULT authenticate(String appkey, String url, AuthData authdata) {
        if (url == null) {
            return AUTHENTICATION_RESULT.URL_UNDEFINED;
        }
        int appkeyl = appkey.length();
        Client client = ClientBuilder.newClient();
        try {
            Response response = client.target(url + "/auth/auth")
                            .request(MediaType.TEXT_PLAIN)
                            .header("Content-Type", "application/json")
                            .post(Entity.entity(authdata, MediaType.APPLICATION_JSON),
                                    Response.class);
            if (response.getStatus() != Status.CREATED.getStatusCode()) {
                return AUTHENTICATION_RESULT.NOT_AUTHENTICATED;
            }
            jwtoken = response.readEntity(String.class);
            // now check the Token
            Invocation.Builder ibuilder = client.target(url + "/auth/check")
                    .request(MediaType.TEXT_PLAIN);
            ibuilder = addAuthorizationHeader(ibuilder);
            response = ibuilder.get();
            if (response.getStatus() == Status.OK.getStatusCode()) {
                //parse the unsigned token to extract claims
                appclaims = Jwts.parser()
                        .parseClaimsJwt(jwtoken.substring(0, jwtoken.lastIndexOf('.')+1))
                        .getBody()
                        .entrySet().stream()
                        .filter( e -> e.getKey().startsWith(appkey))
                        .collect(Collectors.toMap(e -> e.getKey().substring(appkeyl), e -> e.getValue()));
                return appclaims.size() > 0 ?AUTHENTICATION_RESULT.OK: AUTHENTICATION_RESULT.NOT_AUTHORISED;
            } else {
                jwtoken = null;
                return AUTHENTICATION_RESULT.TOKEN_VALIDATION_FAILURE;
            }
        } catch (ProcessingException ex) {
            jwtoken = null;
            return AUTHENTICATION_RESULT.CONNECTION_FAILURE;
        }
    }

    public static Invocation.Builder addAuthorizationHeader(Invocation.Builder ibuilder) {
        return ibuilder.header("Authorization", "Bearer " + jwtoken);
    }

    public static Object getAuthority(String appkey) {
        return appclaims.get(appkey);
    }

    public static class AuthData {

        public String u;
        public String p;

        public AuthData(String u, String p) {
            this.p = p;
            this.u = u;
        }
    }
}
