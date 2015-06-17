/*
 * Copyright (C) 2015 Richard Linsdale.
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
package uk.org.rlinsdale.nbpcglibrary.remoteclient;

import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;
import uk.org.rlinsdale.nbpcglibrary.json.JsonConversionException;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class RemoteAutoIDEntityPersistenceProvider extends RemoteEntityPersistenceProvider<Integer> {
    
    @Override
    protected Integer getPK(JsonValue v) {
        try {
            return JsonUtil.getIntegerValue(v);
        } catch (JsonConversionException ex) {
            throw new LogicException("getPK(jsonvalue) failed: non integer type presented");
        }
    }

    @Override
    protected void addPK(JsonObjectBuilder job, Integer pkey) {
        job.add("pkey", pkey);
    }
}
