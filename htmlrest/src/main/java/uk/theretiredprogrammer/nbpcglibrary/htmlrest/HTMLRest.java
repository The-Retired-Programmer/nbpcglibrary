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

import java.util.ArrayList;
import java.util.List;
import uk.theretiredprogrammer.nbpcglibrary.api.Rest;
import java.util.Map;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uk.theretiredprogrammer.nbpcglibrary.api.BasicEntityCache;
import uk.theretiredprogrammer.nbpcglibrary.api.IdTimestamp;

/**
 * The class implementing the core functions of the Rest interface for
 * a Rest service connected via an HTML interface (ie the typical Rest service).
 *
 * @author richard linsdale (richard @ theretiredprogrammer.uk)
 * @param <E> the entity class being transferred
 */
public class HTMLRest<E extends IdTimestamp> implements Rest<E> {

    private final Client client = ClientBuilder.newClient();
    private final Class<E> responseEntityClass;
    private final String jwtoken;
    private final String locationroot;
    private final BasicEntityCache<E> cache;
    private final GenericType<List<E>> genericType;

    /**
     * Constructor
     *
     * @param locationroot the root section of the location uri
     * @param genericType a GenerticType required when creating List of Objects
     * @param responseEntityClass the class of the entity being transferred
     * (same as generic E)
     * @param jwtoken the authorisation token for the authenticated user
     */
    public HTMLRest(String locationroot, GenericType<List<E>> genericType, Class<E> responseEntityClass, String jwtoken) {
        this.locationroot = locationroot;
        this.genericType = genericType;
        this.responseEntityClass = responseEntityClass;
        this.jwtoken = jwtoken;
        cache = new BasicEntityCache<>();
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
        E e = cache.get(id);
        if (e != null) {
            return e;
        }
        try {
            Response response = client
                    .target(locationroot + "/" + Integer.toString(id))
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("authorization", "bearer " + jwtoken)
                    .get();
            if (response.getStatus() == 200) {
                e = response.readEntity(responseEntityClass);
                cache.insert(e);
                return e;
            } else {
                return null;
            }
        } catch (ProcessingException ex) {
            return null;
        }
    }
    
    @Override
    public List<E> getAll() {
        try {
            Response response = client
                    .target(locationroot)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("authorization", "bearer " + jwtoken)
                    .get();
            if (response.getStatus() == 200) {
                List<E> el = response.readEntity(genericType);
                el.forEach((e) -> cache.insert(e));
                return el;
            } else {
                return new ArrayList<>();
            }
        } catch (ProcessingException ex) {
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<E> getMany(String filtername, int filtervalue) {
        try {
            Response response = client
                    .target(locationroot+"/"+filtername+"/"+Integer.toString(filtervalue))
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("authorization", "bearer " + jwtoken)
                    .get();
            if (response.getStatus() == 200) {
                List<E> el = response.readEntity(genericType);
                el.forEach((e) -> cache.insert(e));
                return el;
            } else {
                return new ArrayList<>();
            }
        } catch (ProcessingException ex) {
            return new ArrayList<>();
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
            if (response.getStatus() == 201) {
                E e = response.readEntity(responseEntityClass);
                cache.insert(e);
                return e;
            } else {
                return null;
            }
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
            if (response.getStatus() == 200) {
                E e = response.readEntity(responseEntityClass);
                cache.insert(e);
                return e;
            } else {
                return null;
            }
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
            if (response.getStatus() == 204) {
                cache.remove(id);
                return true;
            } else {
                return false;
            }
        } catch (ProcessingException ex) {
            return false;
        }
    }
}
