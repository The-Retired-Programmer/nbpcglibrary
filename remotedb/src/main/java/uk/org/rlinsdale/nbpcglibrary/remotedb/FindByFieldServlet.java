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
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 * Servlet to handle findbyfield requests.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the remote entity type
 */
public abstract class FindByFieldServlet<T extends BasicEntity> extends CommonPostServlet {

    @Override
    protected void processCommand(JsonObject parameters, JsonGenerator replyGenerator) {
        String field = parameters.getString("field");
        if (field == null) {
            replyGenerator.write("success", false)
                    .write("message", "field undefined");
        } else {
            JsonValue value = parameters.get("value");
            if (value == null) {
                replyGenerator.write("success", false)
                        .write("message", "value undefined");
            } else {
                try {
                    List<T> entities = queryByName(field, JsonUtil.getValue(value));
                    replyGenerator.write("success", true).write(field, value).write("count", entities.size());
                    replyGenerator.writeStartArray("pkeys");
                    entities.stream().forEach((entity) -> {
                        entity.writePK(replyGenerator);
                    });
                    replyGenerator.writeEnd();
                } catch (Exception e) {
                    replyGenerator.write("success", false)
                            .write("message", "findbyfield operation failed");
                    String message = e.getMessage();
                    if (message != null) {
                        replyGenerator.write("exceptionmessage", message);
                    }
                }
            }
        }
    }

    /**
     * Query the table using a field/value as an equality filter.
     *
     * @param field the filter field
     * @param value the filter value
     * @return the list of entities which match the filter condition
     */
    protected abstract List<T> queryByName(String field, Object value);
}
