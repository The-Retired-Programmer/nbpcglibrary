/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package linsdale.nbpcg.supportlib;

/**
 * Manages the storage of Database Connection parameters.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public final class DbConnectionParameters {

    /**
     * The key to be used to reference this parameter set
     */
    public final String key;

    /**
     * The descriptive name
     */
    public final String description;

    /**
     * The connection string
     */
    public final String connection;

    /**
     * The database type
     */
    public final String type;

    /**
     * The database username
     */
    public final String user;

    /**
     * The database user's password
     */
    public final String password;

    /**
     * Constructor.
     *
     * @param key the key to be used to reference this parameter set
     * @param description the descriptive name
     * @param connection the connection string
     * @param type the database type
     * @param user the database username
     * @param password the database user's password
     */
    public DbConnectionParameters(String key, String description, String connection, String type, String user, String password) {
        this.key = key;
        this.description = description;
        this.connection = connection;
        this.type = type;
        this.user = user;
        this.password = password;
    }
}
