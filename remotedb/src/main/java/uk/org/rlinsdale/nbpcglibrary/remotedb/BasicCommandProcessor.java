/*
 * Copyright (C) 2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package uk.org.rlinsdale.nbpcglibrary.remotedb;

import java.util.List;
import java.util.Map;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import uk.org.rlinsdale.nbpcglibrary.json.JsonConversionException;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 * Command Processor for incoming command(s), process commands and return
 * response(s)
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> Primary Key Class
 * @param <T> the type of entity class
 */
public abstract class BasicCommandProcessor<K, T extends BasicEntity> {

    /**
     * the Class of the entity being processed
     */
    protected final Class<T> clazz;

    /**
     * Constructor
     *
     * @param clazz the entity class
     */
    public BasicCommandProcessor(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Get the entity manager for this entity class
     *
     * @return the entity manager
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Create a new entity
     *
     * @return the entity
     */
    protected abstract T getNewEntity();

    /**
     * Get the name of the Primary Key field
     *
     * @return the name of the Primary Key
     */
    protected abstract String getPKname();

    /**
     * Query the data using a field/value as the filter
     *
     * @param field the field name
     * @param value the filter value
     * @return a set of matching entities
     */
    protected abstract List<T> queryByName(String field, Object value);

    /**
     * Query the data and get all entities
     *
     * @return a set of all the entities
     * @throws JsonConversionException if parsing/creation problems
     */
    protected abstract List<T> queryAll() throws JsonConversionException;

    /**
     * Command processor - processes a single command.
     *
     * @param command the command.
     * @param generator the JsonGenerator to be used to build the response
     */
    public void processCommand(JsonObject command, JsonGenerator generator) {
        String action = command.getString("action", "");
        generator.write("action", action);
        switch (action) {
            case "create":
                create(generator, command);
                break;
            case "update":
                update(generator, command);
                break;
            case "delete":
                delete(generator, command);
                break;
            case "get":
                get(generator, command);
                break;
            case "getbyfield":
                getbyfield(generator, command);
                break;
            case "getall":
                getall(generator);
                break;
            case "findall":
                findall(generator);
                break;
            case "findbyfield":
                findbyfield(generator, command);
                break;
            case "":
                generator.write("success", false)
                        .write("message", "No action defined");
            default:
                generator.write("success", false)
                        .write("message", "Unknown action requested");
        }
    }

    private void create(JsonGenerator generator, JsonObject command) {
        JsonObject fields = command.getJsonObject("entity");
        T newentity = getNewEntity();
        for (Map.Entry<String, JsonValue> kv : fields.entrySet()) {
            String name = kv.getKey();
            try {
                newentity.setField(name, kv.getValue());
            } catch (JsonConversionException ex) {
                generator.write("success", false)
                        .write("message", name + " is not an entity field");
                return;
            }
        }
        if (!createInsertFieldsHook(generator, command, newentity)) {
            return;
        }
        try {
            this.getEntityManager().persist(newentity);
            this.getEntityManager().flush();
        } catch (Exception e) {
            generator.write("success", false)
                    .write("message", "persist operation failed");
            String message = e.getMessage();
            if (message != null) {
                generator.write("exceptionmessage", message);
            }
            return;
        }
        generator.write("success", true);
        newentity.writePKwithkey(generator);
        newentity.writeAllFields(generator, "entity");
    }

    /**
     * An extension point for use by sub-types, to make any changes to the
     * response after the entity has been created and populated, but prior to
     * persisting it.
     *
     * @param generator the JsonGenerator to be used to build the response
     * @param command the command.
     * @param entity the entity
     * @return true if the insert is to continue, false to abort the insert
     */
    protected boolean createInsertFieldsHook(JsonGenerator generator, JsonObject command, T entity) {
        return true;
    }

    private void update(JsonGenerator generator, JsonObject command) {
        //services.getEntityManager().merge(entity);
        K pkey = getPK(command);
        if (pkey == null) {
            generator.write("success", false)
                    .write("message", "pkey undefined");
        } else {
            JsonObject fields = command.getJsonObject("entity");
            try {
                T entity = getHook(pkey);
                if (entity != null) {
                    for (Map.Entry<String, JsonValue> kv : fields.entrySet()) {
                        String name = kv.getKey();
                        try {
                            entity.setField(name, kv.getValue());
                        } catch (JsonConversionException ex) {
                            generator.write("success", false)
                                    .write("message", name + " is not an entity field");
                            return;
                        }
                    }
                    if (!createUpdateFieldsHook(generator, command, entity)) {
                        return;
                    }
                    generator.write("success", true);
                    writePK(generator, pkey);
                    entity.writeAllFields(generator, "entity");
                } else {
                    generator.write("success", false)
                            .write("message", "entity does not exist");
                    writePK(generator, pkey);
                }
            } catch (Exception e) {
                generator.write("success", false)
                        .write("message", "update operation failed -" + e.getClass().getSimpleName());
                String message = e.getMessage();
                if (message != null) {
                    generator.write("exceptionmessage", message);
                }
            }
        }
    }
    
    /**
     * An extension point for use by sub-types, to make any changes to the
     * response after the entity has been updated.
     *
     * @param generator the JsonGenerator to be used to build the response
     * @param command the command.
     * @param entity the entity
     * @return true if the update is to continue, false to abort the update
     */
    protected boolean createUpdateFieldsHook(JsonGenerator generator, JsonObject command, T entity) {
        return true;
    }
    
    private void delete(JsonGenerator generator, JsonObject command) {
        K pkey = getPK(command);
        if (pkey == null) {
            generator.write("success", false)
                    .write("message", "pkey undefined");
        } else {
            deleteHook(generator, pkey);
        }
    }

    /**
     * Get Primary Key value
     *
     * @param command the received command object
     * @return the primary key (java object)
     */
    protected abstract K getPK(JsonObject command);

    /**
     * An extension point for use by subtypes, to override the default delete
     * action.
     *
     * @param generator the JsonGenerator to be used to build the response
     * @param pkey the primary key value
     */
    protected void deleteHook(JsonGenerator generator, K pkey) {
        try {
            T entity = getHook(pkey);
            if (entity != null) {
                System.out.println("entity found");
                this.getEntityManager().remove(entity);
                System.out.println("entity removed");
                generator.write("success", true);
                writePK(generator, pkey);
            } else {
                generator.write("success", false)
                        .write("message", "entity does not exist");
                writePK(generator, pkey);
            }
        } catch (IllegalArgumentException | TransactionRequiredException | JsonConversionException e) {
            generator.write("success", false)
                    .write("message", "delete operation failed -" + e.getClass().getSimpleName());
            String message = e.getMessage();
            if (message != null) {
                generator.write("exceptionmessage", message);
            }
        }
    }

    /**
     * Write the Primary key to the json response generator
     *
     * @param generator the generator
     * @param pkey the primary key
     */
    protected abstract void writePK(JsonGenerator generator, K pkey);

    private void get(JsonGenerator generator, JsonObject command) {
        K pkey = getPK(command);
        if (pkey == null) {
            generator.write("success", false)
                    .write("message", "pkey is undefined");
        } else {
            try {
                T entity = getHook(pkey);
                if (entity == null) {
                    generator.write("success", false)
                            .write("message", "entity does not exist");
                            writePK(generator, pkey);
                } else {
                    generator.write("success", true);
                    writePK(generator, pkey);
                    entity.writeAllFields(generator, "entity");
                }
            } catch (Exception e) {
                generator.write("success", false)
                        .write("message", "get operation failed");
                String message = e.getMessage();
                if (message != null) {
                    generator.write("exceptionmessage", message);
                }
            }
        }
    }

    /**
     * An extension point for use by subclasses, to override the default get
     * action.
     *
     * @param pkey the entity Id (primary key value)
     * @return the entity
     * @throws JsonConversionException if problems
     */
    protected T getHook(K pkey) throws JsonConversionException {
        return this.getEntityManager().find(clazz, pkey);
    }

    private void getbyfield(JsonGenerator generator, JsonObject command) {
        String field = command.getString("field");
        if (field == null) {
            generator.write("success", false)
                    .write("message", "field undefined");
        } else {
            JsonValue value = command.get("value");
            if (value == null) {
                generator.write("success", false)
                        .write("message", "value undefined");
            } else {
                try {
                    List<T> entities = queryByName(field, JsonUtil.getValue(value));
                    generator.write("success", true).write(field, value).write("count", entities.size());
                    generator.writeStartArray("entities");
                    entities.stream().forEach((entity) -> {
                        entity.writeAllFields(generator, null);
                    });
                    generator.writeEnd();
                } catch (Exception e) {
                    generator.write("success", false)
                            .write("message", "getbyfield operation failed");
                    String message = e.getMessage();
                    if (message != null) {
                        generator.write("exceptionmessage", message);
                    }
                }
            }
        }
    }

    private void getall(JsonGenerator generator) {
        try {
            List<T> entities = queryAll();
            generator.write("success", true).write("count", entities.size());
            generator.writeStartArray("entities");
            entities.stream().forEach((entity) -> {
                entity.writeAllFields(generator, null);
            });
            generator.writeEnd();
        } catch (Exception e) {
            generator.write("success", false)
                    .write("message", "getall operation failed");
            String message = e.getMessage();
            if (message != null) {
                generator.write("exceptionmessage", message);
            }
        }
    }

    private void findbyfield(JsonGenerator generator, JsonObject command) {
        String field = command.getString("field");
        if (field == null) {
            generator.write("success", false)
                    .write("message", "field undefined");
        } else {
            JsonValue value = command.get("value");
            if (value == null) {
                generator.write("success", false)
                        .write("message", "value undefined");
            } else {
                try {
                    List<T> entities = queryByName(field, JsonUtil.getValue(value));
                    generator.write("success", true).write(field, value).write("count", entities.size());
                    generator.writeStartArray("pkeys");
                    entities.stream().forEach((entity) -> {
                        entity.writePK(generator);
                    });
                    generator.writeEnd();
                } catch (Exception e) {
                    generator.write("success", false)
                            .write("message", "findbyfield operation failed");
                    String message = e.getMessage();
                    if (message != null) {
                        generator.write("exceptionmessage", message);
                    }
                }
            }
        }
    }

    private void findall(JsonGenerator generator) {
        List<T> entities;
        try {
            entities = queryAll();
        } catch (Exception e) {
            generator.write("success", false)
                    .write("message", "findall operation failed");
            String message = e.getMessage();
            if (message != null) {
                generator.write("exceptionmessage", message);
            }
            return;
        }
        generator.write("success", true);
        generator.write("count", entities.size());
        generator.writeStartArray("pkeys");
        entities.stream().forEach((entity) -> {
            entity.writePK(generator);
        });
        generator.writeEnd();
    }
}
