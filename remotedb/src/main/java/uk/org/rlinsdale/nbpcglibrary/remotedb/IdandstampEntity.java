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
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 * The Additional interface needed for an entity which is implements a standard
 * Id and timeStamp fields.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the entity class
 */
public abstract class IdandstampEntity<T extends IdandstampEntity> extends BasicEntity<T> {

    /**
     * Get the entity Id (primary key value)
     *
     * @return the entity Id
     */
    public abstract Integer getId();
    
    /**
     * Set the entity Id (primary key value)
     *
     * @param id the entity Id
     */
    public abstract void setId(Integer id);

    /**
     * Set the createdby field
     *
     * @param usercode the value
     */
    public abstract void setCreatedby(String usercode);

    /**
     * set the updatedby field
     *
     * @param usercode the value
     */
    public abstract void setUpdatedby(String usercode);

    /**
     * set the createdon field
     *
     * @param timestamp the value
     */
    public abstract void setCreatedon(String timestamp);

    /**
     * set the updatedon field
     *
     * @param timestamp the value
     */
    public abstract void setUpdatedon(String timestamp);
    
    
    @Override
    public void writePK(JsonGenerator generator) {
        JsonUtil.writeIntegerValue(generator, getId());
    }

    @Override
    public void writePKwithkey(JsonGenerator generator) {
        JsonUtil.writeIntegerValue(generator, "pkey", getId());
    }

    @Override
    public void setFieldsPreInsert(String user) {
        setId(0);
        setCreatedby(user);
        setUpdatedby(user);
        String when = (new Timestamp()).toSQLString();
        setCreatedon(when);
        setUpdatedon(when);
    }
    
    @Override
    public void setFieldsPreUpdate(String user) {
        setUpdatedby(user);
        String when = (new Timestamp()).toSQLString();
        setUpdatedon(when);
    }
    
    
    public static Integer getPK(JsonObject command) {
        return command.getInt("pkey");
    }

    public static void writePK(JsonGenerator generator, Integer pkey) {
        generator.write("pkey", pkey);
    }
}
