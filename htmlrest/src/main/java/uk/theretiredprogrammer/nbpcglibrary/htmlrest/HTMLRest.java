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

import uk.theretiredprogrammer.nbpcglibrary.api.Rest;
import java.util.Map;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *  The abstract class implementing the core functions of the Rest interface for
 * a Rest service connected via an HTML interface (ie the typical Rest service).
 * 
 * @author richard linsdale (richard @ theretiredprogrammer.uk)
 * @param <E> the entity class being transferred
 */
public abstract class HTMLRest<E> implements Rest<E> {

    private final Client client = ClientBuilder.newClient();
    private final Class<E> responseEntityClass;
    private final String jwtoken;
    private final String locationroot;

    /**
     * Constructor
     * 
     * @param locationroot the root section of the location uri
     * @param responseEntityClass the class of the entity being transferred (same as generic E)
     * @param jwtoken the authorisation token for the authenticated user
     */
    public HTMLRest(String locationroot, Class<E> responseEntityClass, String jwtoken) {
        this.locationroot = locationroot;
        this.responseEntityClass = responseEntityClass;
        this.jwtoken = jwtoken;
    }
    
    @Override
    public boolean open() {
        return true;
    }
    
    @Override
    public boolean close() {
        return true;
    }
    

    @Override
    public E get(int id) {
        try {
            Response response = client
                    .target(locationroot + "/" + Integer.toString(id))
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("authorization", "bearer " + jwtoken)
                    .get();
            return response.getStatus() == 200
                    ? response.readEntity(responseEntityClass)
                    : null;
        } catch (ProcessingException ex) {
            return null;
        }
    }

    /**
     * Get the response for a getAll request.
     * 
     * Allows the caller to subsequently extract the enitity list from response.
     * 
     * @return the response object returned from this function if successful, else null
     */
    public Response getAllResponse() {
        try {
            Response response = client
                    .target(locationroot)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("authorization", "bearer " + jwtoken)
                    .get();
            if (response.getStatus() == 200) {
                return response;
            } else {
                return null;
            }
        } catch (ProcessingException ex) {
            return null;
        }
    }

    @Override
    public E create(E entity) {
        try {
            Response response = client
                    .target(locationroot)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Content-Type", "application/json")
                    .header("authorization", "bearer " + jwtoken)
                    .post(Entity.json(entity), Response.class);
            return response.getStatus() == 201
                    ? response.readEntity(responseEntityClass)
                    : null;
        } catch (ProcessingException ex) {
            return null;
        }
    }

    @Override
    public E update(int id, E entity) {
        try {
            Response response = client
                    .target(locationroot + "/" + Integer.toString(id))
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Content-Type", "application/json")
                    .header("authorization", "bearer " + jwtoken)
                    .put(Entity.json(entity), Response.class);
            return response.getStatus() == 200
                    ? response.readEntity(responseEntityClass)
                    : null;
        } catch (ProcessingException ex) {
            return null;
        }
    }
    
    @Override
    public E patch(int id, Map<String, Object> updates) {
        // temporary until javaee8
        throw new RuntimeException("Patch not implemented over HTML Rest");
    }
    

    @Override
    public boolean delete(int id) {
        try {
            Response response = client
                    .target(locationroot + "/" + Integer.toString(id))
                    .request(MediaType.TEXT_PLAIN_TYPE)
                    .header("authorization", "bearer " + jwtoken)
                    .delete();
            return response.getStatus() == 204;
        } catch (ProcessingException ex) {
            return false;
        }
    }
}
