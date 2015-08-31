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
import javax.json.stream.JsonGenerator;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Servlet to handle findall requests.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the remote entity type
 */
public abstract class FindAllServlet<T extends BasicEntity> extends CommonPostServlet {

    private final String tablename;

    /**
     * Constructor
     *
     * @param tablename the table to be queried
     */
    public FindAllServlet(String tablename) {
        this.tablename = tablename;
    }

    @Override
    protected void processCommand(JsonObject parameters, JsonGenerator replyGenerator) {
        List<T> entities;
        try {
            Query query = getEntityManager().createNamedQuery(tablename + ".findAll");
            entities = query.getResultList();
        } catch (Exception e) {
            replyGenerator.write("success", false)
                    .write("message", "findall operation failed");
            String message = e.getMessage();
            if (message != null) {
                replyGenerator.write("exceptionmessage", message);
            }
            return;
        }
        replyGenerator.write("success", true);
        replyGenerator.write("count", entities.size());
        replyGenerator.writeStartArray("pkeys");
        entities.stream().forEach((entity) -> {
            entity.writePK(replyGenerator);
        });
        replyGenerator.writeEnd();
    }

    /**
     * Get the entity manager to be used with this transaction.
     *
     * @return the entity manager
     */
    protected abstract EntityManager getEntityManager();
}
