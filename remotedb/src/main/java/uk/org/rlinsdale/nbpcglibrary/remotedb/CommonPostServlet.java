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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * Core Post Servlet.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class CommonPostServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JsonObject parameters;
        try {
            try (JsonReader jsonReader = Json.createReader(request.getReader())) {
                parameters = (JsonObject) jsonReader.read();
            }
        } catch (Exception e) {
            response.setStatus(SC_BAD_REQUEST);
            return;
        }
        System.out.println("Received Post on " + request.getRequestURI() + ": " + parameters.toString());
        StringWriter reply = new StringWriter();
        JsonGenerator replyGenerator = Json.createGenerator(reply);
        replyGenerator.writeStartObject();
        processCommand(parameters, replyGenerator);
        replyGenerator.writeEnd().close();
        String replyString = reply.toString();
        System.out.println("Response:" + replyString);
        response.setStatus(SC_OK);
        response.setContentType("application/json;charset=utf-8");
        try {
            PrintWriter responseBodyWriter = response.getWriter();
            responseBodyWriter.println(replyString);
        } catch (IOException ex) {
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Process the command, with given parameters and build a response to be
     * returned.
     *
     * @param parameters the command parameters
     * @param replyGenerator the genenerator with which the response in built.
     */
    protected abstract void processCommand(JsonObject parameters, JsonGenerator replyGenerator);
}
