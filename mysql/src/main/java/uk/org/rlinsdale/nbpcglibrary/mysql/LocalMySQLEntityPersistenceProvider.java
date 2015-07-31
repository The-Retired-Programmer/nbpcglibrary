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
package uk.org.rlinsdale.nbpcglibrary.mysql;

import uk.org.rlinsdale.nbpcglibrary.api.LogicException;
import uk.org.rlinsdale.nbpcglibrary.localdatabaseaccess.LocalSQLEntityPersistenceProvider;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> the primary key class
 */
public abstract class LocalMySQLEntityPersistenceProvider<K> extends LocalSQLEntityPersistenceProvider<K> {
    
    @Override
    protected String format(Object value) {
        if (value == null ) {
            return "NULL";
        }
        if (value instanceof String){
            return "'" + ((String) value).replace("\\", "\\\\").replace("'", "\\'").replace("\n","\\n") + "'";
        }
        if (value instanceof Boolean){
            return ((Boolean)value).toString();
        }
        if (value instanceof Integer){
            return ((Integer) value).toString();
        }
        if (value instanceof Long){
            return ((Long) value).toString();
        }
        throw new LogicException("Unknown Object type in LocalMySQLEntityPersistenceProvider:format()");
    }
}
