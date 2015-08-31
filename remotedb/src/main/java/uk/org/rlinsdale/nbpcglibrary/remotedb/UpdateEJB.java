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

import java.util.Map;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import uk.org.rlinsdale.nbpcglibrary.json.JsonConversionException;

/**
 * Create a EJB to handle update requests.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the remote entity type
 * @param <K> Primary Key class
 */
public abstract class UpdateEJB<T extends BasicEntity, K> implements CommonCommandEJB<T> {

    @Override
    public void processCommand(Class<T> clazz, JsonObject parameters, JsonGenerator replyGenerator) {
        K pkey = getPK(parameters);
        if (pkey == null) {
            replyGenerator.write("success", false)
                    .write("message", "pkey undefined");
        } else {
            try {
                T entity = this.getEntityManager().find(clazz, pkey);
                if (entity != null) {
                    JsonObject fields = parameters.getJsonObject("entity");
                    for (Map.Entry<String, JsonValue> kv : fields.entrySet()) {
                        String name = kv.getKey();
                        try {
                            entity.setField(name, kv.getValue());
                        } catch (JsonConversionException ex) {
                            replyGenerator.write("success", false)
                                    .write("message", name + " is not an entity field");
                            return;
                        }
                    }
                    String user = parameters.getString("user", "");
                    if ("".equals(user)) {
                        replyGenerator.write("success", false)
                                .write("message", "user undefined");
                        return;
                    }
                    entity.setFieldsPreUpdate(user);
                    replyGenerator.write("success", true);
                    writePK(replyGenerator, pkey);
                    entity.writeAllFields(replyGenerator, "entity");
                } else {
                    replyGenerator.write("success", false)
                            .write("message", "entity does not exist");
                    writePK(replyGenerator, pkey);
                }
            } catch (Exception e) {
                replyGenerator.write("success", false)
                        .write("message", "update operation failed -" + e.getClass().getSimpleName());
                String message = e.getMessage();
                if (message != null) {
                    replyGenerator.write("exceptionmessage", message);
                }
            }
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
     * Write the Primary key to the json response generator
     *
     * @param generator the generator
     * @param pkey the primary key
     */
    protected abstract void writePK(JsonGenerator generator, K pkey);
}
