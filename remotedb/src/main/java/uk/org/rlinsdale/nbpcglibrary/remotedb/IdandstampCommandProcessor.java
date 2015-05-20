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

import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import uk.org.rlinsdale.nbpcglibrary.api.Timestamp;

/**
 * The Command processor of an entity which includes standard Id and
 * TimeStamping fields
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> The entity class
 */
public abstract class IdandstampCommandProcessor<T extends IdandstampEntity> extends BasicCommandProcessor<T> {

    /**
     * Constructor
     *
     * @param clazz the class of the entity
     */
    public IdandstampCommandProcessor(Class<T> clazz) {
        super(clazz);
    }

    @Override
    protected String getPKname() {
        return "id";
    }

    @Override
    protected boolean createInsertFieldsHook(JsonGenerator generator, JsonObject command, T entity) {
        String user = command.getString("user", "");
        if ("".equals(user)) {
            generator.write("success", false)
                    .write("message", "user undefined");
            return false;
        }
        entity.setCreatedby(user);
        entity.setUpdatedby(user);
        String when = (new Timestamp()).toSQLString();
        entity.setCreatedon(when);
        entity.setUpdatedon(when);
        return true;
    }
}
