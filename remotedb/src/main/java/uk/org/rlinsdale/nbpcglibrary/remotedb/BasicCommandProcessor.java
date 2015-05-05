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
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the type of entity class
 */
public abstract class BasicCommandProcessor<T extends BasicEntity> {

    /**
     *
     */
    protected final Class<T> clazz;

    /**
     *
     * @param clazz
     */
    public BasicCommandProcessor(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     *
     * @return
     */
    protected abstract EntityManager getEntityManager();

    /**
     *
     * @return
     */
    protected abstract T getNewEntity();

    /**
     *
     * @return
     */
    protected abstract String getPKname();

    /**
     *
     * @param field
     * @param value
     * @return
     * @throws JsonConversionException
     */
    protected abstract List<T> queryByName(JsonValue field, JsonValue value) throws JsonConversionException;

    /**
     *
     * @return
     * @throws JsonConversionException
     */
    protected abstract List<T> queryAll() throws JsonConversionException;

    /**
     *
     * @param command
     * @param generator
     */
    public void processCommand(JsonObject command, JsonGenerator generator) {
        String action = command.getString("action", "");
        generator.write("action", action);
        switch (action) {
            case "create":
                create(generator, command);
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
        JsonObject fields = command.getJsonObject("fields");
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
    }

    /**
     *
     * @param generator
     * @param command
     * @param entity
     * @return
     */
    protected boolean createInsertFieldsHook(JsonGenerator generator, JsonObject command, T entity) {
        return true;
    }

    /**
     *
     * @param entity
     */
    protected void update(BasicEntity entity) {
        //services.getEntityManager().merge(entity);
    }

    private void delete(JsonGenerator generator, JsonObject command) {
        String pkname = getPKname();
        JsonValue id = command.get(pkname);
        if (id == null) {
            generator.write("success", false)
                    .write("message", pkname + " undefined");
        } else {
            deleteHook(generator, pkname, id);
        }
    }

    /**
     *
     * @param generator
     * @param pkname
     * @param id
     */
    protected void deleteHook(JsonGenerator generator, String pkname, JsonValue id) {
        try {
            T entity = getHook(id);
            if (entity != null) {
                System.out.println("entity found");
                this.getEntityManager().remove(entity);
                System.out.println("entity removed");
                generator.write("success", true)
                        .write(pkname, id);
            } else {
                generator.write("success", false)
                        .write("message", "entity does not exist")
                        .write(pkname, id);
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

    private void get(JsonGenerator generator, JsonObject command) {
        String pkname = getPKname();
        JsonValue id = command.get(pkname);
        if (id == null) {
            generator.write("success", false)
                    .write("message", pkname + " undefined");
        } else {
            try {
                T entity = getHook(id);
                if (entity == null) {
                    generator.write("success", false)
                            .write("message", "entity does not exist")
                            .write(pkname, id);
                } else {
                    generator.write("success", true).write(pkname, id);
                    entity.writeAllFields(generator, "fields");
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
     *
     * @param id
     * @return
     * @throws JsonConversionException
     */
    protected T getHook(JsonValue id) throws JsonConversionException {
        return this.getEntityManager().find(clazz, JsonUtil.getValue(id));
    }

    private void getbyfield(JsonGenerator generator, JsonObject command) {
        JsonValue field = command.get("field");
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
                    List<T> entities = queryByName(field, value);
                    generator.write("success", true).write(JsonUtil.getStringValue(field), value).write("count", entities.size());
                    generator.writeStartArray("entities");
                    entities.stream().forEach((entity) -> {
                        entity.writeAllFields(generator, null);
                    });
                    generator.writeEnd();
                } catch (Exception e) {
                    generator.write("success", false)
                            .write("message", "getbyname operation failed");
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
        JsonValue field = command.get("field");
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
                    List<T> entities = queryByName(field, value);
                    generator.write("success", true).write(JsonUtil.getStringValue(field), value).write("count", entities.size());
                    generator.writeStartArray("ids");
                    entities.stream().forEach((entity) -> {
                        entity.writePK(generator);
                    });
                    generator.writeEnd();
                } catch (Exception e) {
                    generator.write("success", false)
                            .write("message", "findbyname operation failed");
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
        generator.writeStartArray("ids");
        entities.stream().forEach((entity) -> {
            entity.writePK(generator);
        });
        generator.writeEnd();
    }
}
