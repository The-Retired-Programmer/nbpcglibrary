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
package uk.theretiredprogrammer.nbpcglibrary.localjsonaccess;

import uk.theretiredprogrammer.nbpcglibrary.api.Rest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author richard linsdale (richard @ theretiredprogrammer.uk)
 */
public class JsonFileRest<E> implements Rest<E> {

//    private final Class<S> responseEntityClass;
//    private final MediaType mediaType;

//    public JsonFileRest(Class<S> responseEntityClass, MediaType mediaType) {
//        this.responseEntityClass = responseEntityClass;
//        this.mediaType = mediaType;
//    }
//
//    public class JsonFileResponse implements Rest.Response<S> {
//
//        private final S entity;
//        private final MultivaluedMap<String, String> headers;
//        private final ResponseCode responsecode;
//
//        public JsonFileResponse(ResponseCode responsecode,
//                MultivaluedMap<String, String> headers, S entity) {
//            this.responsecode = responsecode;
//            this.headers = headers;
//            this.entity = entity;
//        }
//
//        public JsonFileResponse(ResponseCode responsecode) {
//            this(responsecode, null, null);
//        }
//
//        @Override
//        public S getEntity() {
//            return entity;
//        }
//
//        @Override
//        public MultivaluedMap<String, String> headers() {
//            return headers;
//        }
//
//        @Override
//        public ResponseCode getResponseCode() {
//            return responsecode;
//        }
//    }
//
//    @Override
//    public Rest.Response<S> get(String url) {
//        if ( !url.startsWith("jsonobj://")) {
//            return new JsonFileResponse(FAILED);
//        }
//        try {
//            String fn = url.substring(7);
//            File f  = new File(fn);
//            
//            MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
//            headers.add("Location", "jsonobj://"+f.getCanonicalPath());
//            
//            return new JsonFileResponse(responseMapper(OK,
//                    headers, response.readEntity(responseEntityClass));
//        } catch (IOException ex) {
//            return new JsonFileResponse(FAILED);
//        }
//    }
//
//    @Override
//    public Rest.Response<S> get(String url, MultivaluedMap<String, String> headers) {
//        try {
//            Invocation.Builder builder = client.target(url).request(mediaType);
//            headers.entrySet().stream()
//                    .forEach((e) -> builder.header(e.getKey(), e.getValue()));
//            if (jwtoken != null) {
//                builder.header("Authorization", "Bearer " + jwtoken);
//            }
//            javax.ws.rs.core.Response response = builder.get();
//            return new JsonFileResponse(responseMapper(response.getStatus()),
//                    response.getStringHeaders(), response.readEntity(responseEntityClass));
//        } catch (ProcessingException ex) {
//            return new JsonFileResponse(FAILED);
//        }
//    }
//    
//    @Override
//    public Rest.Response<S> post(String url, Q entity) {
//        try {
//            Invocation.Builder builder = client.target(url).request(mediaType);
//            builder.header("Content-Type", "application/json");
//            if (jwtoken != null) {
//                builder.header("Authorization", "Bearer " + jwtoken);
//            }
//            javax.ws.rs.core.Response response = builder.post(Entity.json(entity),
//                    javax.ws.rs.core.Response.class);
//            return new JsonFileResponse(responseMapper(response.getStatus()),
//                    response.getStringHeaders(), response.readEntity(responseEntityClass));
//        } catch (ProcessingException ex) {
//            return new JsonFileResponse(FAILED);
//        }
//    }
//
//    @Override
//    public Rest.Response<S> post(String url, MultivaluedMap<String, String> headers, Q entity) {
//        try {
//            Invocation.Builder builder = client.target(url).request(mediaType);
//            builder.header("Content-Type", "application/json");
//            headers.entrySet().stream()
//                    .forEach((e) -> builder.header(e.getKey(), e.getValue()));
//            if (jwtoken != null) {
//                builder.header("Authorization", "Bearer " + jwtoken);
//            }
//            javax.ws.rs.core.Response response = builder.post(Entity.json(entity),
//                    javax.ws.rs.core.Response.class);
//            return new JsonFileResponse(responseMapper(response.getStatus()),
//                    response.getStringHeaders(), response.readEntity(responseEntityClass));
//        } catch (ProcessingException ex) {
//            return new JsonFileResponse(FAILED);
//        }
//    }
//
//    @Override
//    public Rest.Response<S> put(String url, Q entity) {
//        try {
//            Invocation.Builder builder = client.target(url).request(mediaType);
//            builder.header("Content-Type", "application/json");
//            if (jwtoken != null) {
//                builder.header("Authorization", "Bearer " + jwtoken);
//            }
//            javax.ws.rs.core.Response response = builder.post(Entity.json(entity),
//                    javax.ws.rs.core.Response.class);
//            return new JsonFileResponse(responseMapper(response.getStatus()),
//                    response.getStringHeaders(), response.readEntity(responseEntityClass));
//        } catch (ProcessingException ex) {
//            return new JsonFileResponse(FAILED);
//        }
//    }
//    
//    @Override
//    public Rest.Response<S> put(String url, MultivaluedMap<String, String> headers, Q entity) {
//        try {
//            Invocation.Builder builder = client.target(url).request(mediaType);
//            builder.header("Content-Type", "application/json");
//            headers.entrySet().stream()
//                    .forEach((e) -> builder.header(e.getKey(), e.getValue()));
//            if (jwtoken != null) {
//                builder.header("Authorization", "Bearer " + jwtoken);
//            }
//            javax.ws.rs.core.Response response = builder.post(Entity.json(entity),
//                    javax.ws.rs.core.Response.class);
//            return new JsonFileResponse(responseMapper(response.getStatus()),
//                    response.getStringHeaders(), response.readEntity(responseEntityClass));
//        } catch (ProcessingException ex) {
//            return new JsonFileResponse(FAILED);
//        }
//    }
//
//    private ResponseCode responseMapper(int statuscode) {
//        Status status = Status.fromStatusCode(statuscode);
//        switch (status) {
//            case OK:
//                return ResponseCode.OK;
//            case CREATED:
//                return ResponseCode.CREATED;
//            case UNAUTHORIZED:
//                return ResponseCode.NOTAUTH;
//            default:
//                return ResponseCode.FAILED;
//        }
//    }

    @Override
    public E get(String location) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<E> getAll(String location) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public E create(String location, E entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public E update(String location, E entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public E patch(String location, Map<String, Object> updates) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(String location) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
