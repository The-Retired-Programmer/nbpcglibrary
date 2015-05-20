/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package uk.org.rlinsdale.nbpcglibrary.api;

import java.io.IOException;

/**
 * Exception - for handling EntityPersistenceProviderManager property problems.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class EntityPersistenceProviderManagerException extends IOException {

    /**
     * Constructor.
     */
    public EntityPersistenceProviderManagerException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message the exception message text
     */
    public EntityPersistenceProviderManagerException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param ex the causal exception
     */
    public EntityPersistenceProviderManagerException(Exception ex) {
        super(ex);
    }

    /**
     * Constructor.
     *
     * @param message the exception message text
     * @param ex the causal exception
     */
    public EntityPersistenceProviderManagerException(String message, Exception ex) {
        super(message, ex);
    }
}
