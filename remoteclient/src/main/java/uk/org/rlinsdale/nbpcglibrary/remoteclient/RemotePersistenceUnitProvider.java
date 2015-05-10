/*
 * Copyright (C) 2014-2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.io.IOException;
import java.util.Properties;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import uk.org.rlinsdale.nbpcglibrary.api.PersistenceUnitProvider;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.json.JsonConversionException;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 * Abstract Class implementing ersistenceUnitProvide over an Http based
 * protocol.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class RemotePersistenceUnitProvider implements PersistenceUnitProvider {

    private final CloseableHttpClient httpclient;
    private final String url;
    private final boolean operational;

    /**
     * Constructor.
     *
     * @param p the connection properties
     */
    public RemotePersistenceUnitProvider(Properties p) {
        url = p.getProperty("connection", "");
        httpclient = HttpClients.createDefault();
        operational = pingUrl();
    }

    /**
     * Execute Single Command - send a single command to executed by remote data
     * source.
     *
     * @param request the command object
     * @return the response object
     * @throws IOException if problems with parsing command data or problems
     * executing the command
     */
    public synchronized JsonObject executeSingleCommand(JsonObject request) throws IOException {
        JsonStructure res = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(request.toString(), APPLICATION_JSON));
        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity responsebody = response.getEntity();
                try (JsonReader jsonReader = Json.createReader(responsebody.getContent())) {
                    res = jsonReader.read();
                }
                EntityUtils.consume(responsebody);
            }
        }
        if (res instanceof JsonObject) {
            return (JsonObject) res;
        } else {
            throw new JsonConversionException();
        }
    }

    /**
     * Execute Multiple Commands - send multiple commands in a single message to
     * be executed by remote data source.
     *
     * @param request the set of command objects
     * @return the set of response objects
     * @throws IOException if problems with parsing command data or problems
     * executing the command
     */
    public synchronized JsonArray executeMultipleCommands(JsonArray request) throws IOException {
        JsonStructure res = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(request.toString(), APPLICATION_JSON));
        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity responsebody = response.getEntity();
                try (JsonReader jsonReader = Json.createReader(responsebody.getContent())) {
                    res = jsonReader.read();
                }
                EntityUtils.consume(responsebody);
            }
        }
        if (res instanceof JsonArray) {
            return (JsonArray) res;
        } else {
            throw new JsonConversionException();
        }
    }

    private boolean pingUrl() {
        try {
            JsonStructure response = get();
            if (response instanceof JsonObject) {
                JsonObject job = (JsonObject) response;
                return JsonUtil.getObjectKeyBooleanValue(job, "success") && "Ping response".equals(JsonUtil.getObjectKeyStringValue(job, "message"));
            }
        } catch (IOException ex) {
        }
        return false;
    }

    private synchronized JsonStructure get() throws IOException {
        JsonStructure res = null;
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity responsebody = response.getEntity();
                try (JsonReader jsonReader = Json.createReader(responsebody.getContent())) {
                    res = jsonReader.read();
                }
                EntityUtils.consume(responsebody);
            }
        }
        return res;
    }

    @Override
    public boolean isOperational() {
        return operational;
    }

    @Override
    public String getName() {
        return url;
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, getName());
    }
}
