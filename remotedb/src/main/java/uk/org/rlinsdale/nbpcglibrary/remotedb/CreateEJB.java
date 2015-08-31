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
 * Create a EJB to handle create requests.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the remote entity type
 * @param <K> the primary key class
 */
public abstract class CreateEJB<T extends BasicEntity, K> implements CommonCommandEJB<T> {

    @Override
    public void processCommand(Class<T> clazz, JsonObject parameters, JsonGenerator replyGenerator) {
        JsonObject fields = parameters.getJsonObject("entity");
        T newentity = getNewEntity();
        for (Map.Entry<String, JsonValue> kv : fields.entrySet()) {
            String name = kv.getKey();
            try {
                newentity.setField(name, kv.getValue());
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
        newentity.setFieldsPreInsert(user);
        try {
            this.getEntityManager().persist(newentity);
            this.getEntityManager().flush();
        } catch (Exception e) {
            replyGenerator.write("success", false)
                    .write("message", "persist operation failed");
            String message = e.getMessage();
            if (message != null) {
                replyGenerator.write("exceptionmessage", message);
            }
            return;
        }
        replyGenerator.write("success", true);
        newentity.writePKwithkey(replyGenerator);
        newentity.writeAllFields(replyGenerator, "entity");
    }

    /**
     * Create a new entity
     *
     * @return the entity
     */
    protected abstract T getNewEntity();

}
