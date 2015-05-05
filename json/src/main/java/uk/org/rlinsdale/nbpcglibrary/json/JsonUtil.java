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
package uk.org.rlinsdale.nbpcglibrary.json;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import static javax.json.JsonValue.ValueType.*;
import javax.json.stream.JsonGenerator;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class JsonUtil {

    /**
     *
     * @param values
     * @param key
     * @return
     * @throws JsonConversionException
     */
    public static JsonValue getObjectKeyValue(JsonObject values, String key) throws JsonConversionException {
        return values.get(key);
    }

    /**
     *
     * @param values
     * @param key
     * @return
     * @throws JsonConversionException
     */
    public static String getObjectKeyStringValue(JsonObject values, String key) throws JsonConversionException {
        return getStringValue(values.get(key));
    }

    /**
     *
     * @param values
     * @param key
     * @return
     * @throws JsonConversionException
     */
    public static Integer getObjectKeyIntegerValue(JsonObject values, String key) throws JsonConversionException {
        return getIntegerValue(values.get(key));
    }

    /**
     *
     * @param values
     * @param key
     * @return
     * @throws JsonConversionException
     */
    public static Boolean getObjectKeyBooleanValue(JsonObject values, String key) throws JsonConversionException {
        return getBooleanValue(values.get(key));
    }

    /**
     *
     * @param values
     * @param key
     * @return
     * @throws JsonConversionException
     */
    public static JsonArray getObjectKeyArrayValue(JsonObject values, String key) throws JsonConversionException {
        return getArrayValue(values.get(key));
    }

    /**
     *
     * @param array
     * @return
     * @throws JsonConversionException
     */
    public static JsonObject getObjectFromArray(JsonArray array) throws JsonConversionException {
        return getObjectFromArray(array, 0);

    }

    /**
     *
     * @param array
     * @param index
     * @return
     * @throws JsonConversionException
     */
    public static JsonObject getObjectFromArray(JsonArray array, int index) throws JsonConversionException {
        JsonValue job = array.get(index);
        if (job.getValueType() == OBJECT) {
            return (JsonObject) job;
        }
        throw new JsonConversionException();
    }

    /**
     *
     * @param value
     * @return
     * @throws JsonConversionException
     */
    public static Object getValue(JsonValue value) throws JsonConversionException {
        switch (value.getValueType()) {
            case STRING:
                return ((JsonString) value).getString();
            case NUMBER:
                return ((JsonNumber) value).intValue();
            case TRUE:
                return true;
            case FALSE:
                return false;
            case NULL:
                return null;
            default:
                throw new JsonConversionException();
        }
    }

    /**
     *
     * @param value
     * @return
     * @throws JsonConversionException
     */
    public static String getStringValue(JsonValue value) throws JsonConversionException {
        if (value.getValueType() == STRING) {
            return ((JsonString) value).getString();
        }
        throw new JsonConversionException();
    }

    /**
     *
     * @param generator
     * @param key
     * @param value
     */
    public static void writeStringValue(JsonGenerator generator, String key, String value) {
        generator.write(key, value);
    }

    /**
     *
     * @param generator
     * @param value
     */
    public static void writeStringValue(JsonGenerator generator, String value) {
        generator.write(value);
    }

    /**
     *
     * @param value
     * @return
     * @throws JsonConversionException
     */
    public static Integer getIntegerValue(JsonValue value) throws JsonConversionException {
        if (value.getValueType() == NUMBER) {
            return ((JsonNumber) value).intValue();
        }
        throw new JsonConversionException();
    }

    /**
     *
     * @param generator
     * @param key
     * @param value
     */
    public static void writeIntegerValue(JsonGenerator generator, String key, int value) {
        generator.write(key, value);
    }

    /**
     *
     * @param generator
     * @param value
     */
    public static void writeIntegerValue(JsonGenerator generator, int value) {
        generator.write(value);
    }

    /**
     *
     * @param value
     * @return
     * @throws JsonConversionException
     */
    public static Boolean getBooleanValue(JsonValue value) throws JsonConversionException {
        switch (value.getValueType()) {
            case TRUE:
                return true;
            case FALSE:
                return false;
            default:
                throw new JsonConversionException();
        }
    }

    /**
     *
     * @param generator
     * @param key
     * @param value
     */
    public static void writeBooleanValue(JsonGenerator generator, String key, boolean value) {
        generator.write(key, value ? JsonValue.TRUE : JsonValue.FALSE);
    }

    /**
     *
     * @param generator
     * @param value
     */
    public static void writeBooleanValue(JsonGenerator generator, boolean value) {
        generator.write(value ? JsonValue.TRUE : JsonValue.FALSE);
    }

    /**
     *
     * @param value
     * @return
     * @throws JsonConversionException
     */
    public static Integer getReferenceValue(JsonValue value) throws JsonConversionException {
        switch (value.getValueType()) {
            case NULL:
                return null;
            case NUMBER:
                return ((JsonNumber) value).intValue();
            default:
                throw new JsonConversionException();
        }
    }

    /**
     *
     * @param generator
     * @param key
     * @param value
     */
    public static void writeReferenceValue(JsonGenerator generator, String key, Integer value) {
        if (value == null) {
            generator.write(key, JsonValue.NULL);
        } else {
            generator.write(key, value);
        }
    }

    public static void writeReferenceValue(JsonGenerator generator, Integer value) {
        if (value == null) {
            generator.write(JsonValue.NULL);
        } else {
            generator.write(value);
        }
    }

    /**
     *
     * @param value
     * @return
     * @throws JsonConversionException
     */
    public static JsonArray getArrayValue(JsonValue value) throws JsonConversionException {
        if (value.getValueType() == ARRAY) {
            return ((JsonArray) value);
        }
        throw new JsonConversionException();
    }

    // horrible bit of code!! - but is does work

    /**
     *
     * @param string
     * @return
     */
        public static JsonValue createJsonValue(String string) {
        return Json.createObjectBuilder()
                .add("value", string)
                .build()
                .getJsonString("value");
    }
    
    // horrible bit of code!! - but is does work

    /**
     *
     * @param value
     * @return
     */
        public static JsonValue createJsonValue(int value) {
        return Json.createObjectBuilder()
                .add("value", value)
                .build()
                .getJsonNumber("value");
    }

}
