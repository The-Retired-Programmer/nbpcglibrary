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
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * Create a servlet to remotely handle access to a Database and all its tables.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class CPServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Received Command(s) request (POST):");
        JsonValue commands;
        try {
            try (JsonReader jsonReader = Json.createReader(request.getReader())) {
                commands = jsonReader.read();
            }
        } catch (Exception e) {
            response.setStatus(SC_BAD_REQUEST);
            return;
        }
        System.out.println(commands.toString());
        switch (commands.getValueType()) {
            case ARRAY:
                StringWriter reply = new StringWriter();
                try (JsonGenerator generator = Json.createGenerator(reply)) {
                    generator.writeStartArray();
                    System.out.println("Responses:");
                    for (JsonObject command : ((JsonArray) commands).getValuesAs(JsonObject.class)) {
                        String replyJson = processCommand(command);
                        System.out.println(replyJson);
                        generator.write(replyJson);
                    }
                    generator.writeEnd();
                }
                response.setStatus(SC_OK);
                response.setContentType("application/json;charset=utf-8");
                try {
                    PrintWriter responseBodyWriter = response.getWriter();
                    responseBodyWriter.println(reply.toString());
                } catch (IOException ex) {
                    response.setStatus(SC_INTERNAL_SERVER_ERROR);
                }
                break;
            case OBJECT:
                String replyJson = processCommand((JsonObject) commands);
                System.out.println("Response:");
                System.out.println(replyJson);
                response.setStatus(SC_OK);
                response.setContentType("application/json;charset=utf-8");
                try {
                    PrintWriter responseBodyWriter = response.getWriter();
                    responseBodyWriter.println(replyJson);
                } catch (IOException ex) {
                    response.setStatus(SC_INTERNAL_SERVER_ERROR);
                }
                break;
            default:
                response.setStatus(SC_BAD_REQUEST);
        }
    }

    private String processCommand(JsonObject command) {
        String entity = command.getString("entity", "");
        StringWriter reply = new StringWriter();
        JsonGenerator generator = Json.createGenerator(reply);
        generator.writeStartObject().write("entity", entity);
        if ("".equals(entity)) {
            generator.write("success", false).write("message", "No entity defined");
        } else {
            if (!switchCommandProcessor(entity, command, generator)) {
                generator.write("success", false).write("message", "Unknown entity requested");
            }
        }
        generator.writeEnd().close();
        return reply.toString();
    }

    /**
     * Select and call the correct Command processor for this entity class.
     *
     * @param entity the entity
     * @param command the command
     * @param generator tyhe JsonGenerator for this response
     * @return true if command processor was selected, otherwise false if entity
     * class not recognised
     */
    protected abstract boolean switchCommandProcessor(String entity, JsonObject command, JsonGenerator generator);

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * this is just a ping action
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Received Ping request (GET);");
        StringWriter reply = new StringWriter();
        JsonGenerator generator = Json.createGenerator(reply);
        generator.writeStartObject().write("success", true).write("message", "Ping response").writeEnd().close();
        String jsonReply = reply.toString();
        System.out.println("Response:");
        System.out.println(jsonReply);
        response.setStatus(SC_OK);
        response.setContentType("application/json;charset=utf-8");
        try {
            PrintWriter responseBodyWriter = response.getWriter();
            responseBodyWriter.println(jsonReply);
        } catch (IOException ex) {
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        }
    }
}
