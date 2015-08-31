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
package uk.org.rlinsdale.nbpcglibrary.remotedb;

import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.persistence.EntityManager;

/**
 * The interface for an EJB to handle requests for entity updates which must be
 * persisted (to database).
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the remote entity class
 */
public interface CommonCommandEJB<T extends BasicEntity> {

    /**
     * Handles the command processing.
     *
     * @param clazz the class of the remote entity
     * @param parameters the parameters for this command
     * @param replyGenerator the reply generator - used to collect the reply
     */
    public void processCommand(Class<T> clazz, JsonObject parameters, JsonGenerator replyGenerator);

    /**
     * Get the entity manager to be used with this transaction.
     *
     * @return the entity manager
     */
    public abstract EntityManager getEntityManager();
}
