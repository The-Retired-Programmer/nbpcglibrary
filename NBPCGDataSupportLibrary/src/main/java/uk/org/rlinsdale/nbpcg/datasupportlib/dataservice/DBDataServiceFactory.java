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
package uk.org.rlinsdale.nbpcg.datasupportlib.dataservice;

import uk.org.rlinsdale.nbpcg.supportlib.DbConnectionParameters;

/**
 * Create a DBDataService.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <S> the DBDataService class to be be created
 */
public interface DBDataServiceFactory<S extends DBDataService> {

    /**
     * Get the type of DBDataService which this Factory creates.
     *
     * @return the type string
     */
    public String getType();

    /**
     * Create a DBDataService.
     *
     * @param p the Connection parameters which define the connection
     * @return the DBDataService
     */
    public S createDataService(DbConnectionParameters p);
}
