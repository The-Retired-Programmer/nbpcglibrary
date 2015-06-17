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

import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import static javax.json.JsonValue.ValueType.*;
import javax.json.stream.JsonGenerator;

/**
 * A Set of methods to process Json Objects/Arrays in a typesafe manner.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class JsonUtil {

    /**
     * Extract a jsonValue from a JsonObject, which is associated with a
     * particular key.
     *
     * @param values the JsonObject
     * @param key the key
     * @return the value associated with the key
     * @throws JsonConversionException if any conversion error or type failure
     */
    public static JsonValue getObjectKeyValue(JsonObject values, String key) throws JsonConversionException {
        return values.get(key);
    }

    /**
     * Extract a string value from a JsonObject, which is associated with a
     * particular key.
     *
     * @param values the JsonObject
     * @param key the key
     * @return the value associated with the key
     * @throws JsonConversionException if any conversion error or type failure
     */
    public static String getObjectKeyStringValue(JsonObject values, String key) throws JsonConversionException {
        return getStringValue(values.get(key));
    }

    /**
     * Extract an integer value from a JsonObject, which is associated with a
     * particular key.
     *
     * @param values the JsonObject
     * @param key the key
     * @return the value associated with the key
     * @throws JsonConversionException if any conversion error or type failure
     */
    public static Integer getObjectKeyIntegerValue(JsonObject values, String key) throws JsonConversionException {
        return getIntegerValue(values.get(key));
    }

    /**
     * Extract a boolean value from a JsonObject, which is associated with a
     * particular key.
     *
     * @param values the JsonObject
     * @param key the key
     * @return the value associated with the key
     * @throws JsonConversionException if any conversion error or type failure
     */
    public static Boolean getObjectKeyBooleanValue(JsonObject values, String key) throws JsonConversionException {
        return getBooleanValue(values.get(key));
    }

    /**
     * Extract a JsonArray from a JsonObject, which is associated with a
     * particular key.
     *
     * @param values the JsonObject
     * @param key the key
     * @return the JsonArray associated with the key
     * @throws JsonConversionException if any conversion error or type failure
     */
    public static JsonArray getObjectKeyArrayValue(JsonObject values, String key) throws JsonConversionException {
        return getArrayValue(values.get(key));
    }

    /**
     * Extract the first JsonObject from a JsonArray.
     *
     * @param array the JsonArray
     * @return the JsonObject
     * @throws JsonConversionException if any conversion error or type failure
     */
    public static JsonObject getObjectFromArray(JsonArray array) throws JsonConversionException {
        return getObjectFromArray(array, 0);

    }

    /**
     * Extract the n'th JsonObject from a JsonArray.
     *
     * @param array the JsonArray
     * @param index the index (0 based)
     * @return the JsonObject
     * @throws JsonConversionException if any conversion error or type failure
     */
    public static JsonObject getObjectFromArray(JsonArray array, int index) throws JsonConversionException {
        JsonValue job = array.get(index);
        if (job.getValueType() == OBJECT) {
            return (JsonObject) job;
        }
        throw new JsonConversionException();
    }

    /**
     * Convert a JsonValue into a equivalent Java Object or datatype.
     *
     * @param value the JsonValue
     * @return the resulting java Object
     * @throws JsonConversionException if any conversion error or type failure
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
     * Insert Java Object into a JsonObject as a key/value pair.
     *
     * @param job the ObjectBuilder into which the key/value pair is inserted
     * @param key the key
     * @param value the value to be inserted
     * @throws JsonConversionException if any conversion error or type failure
     */
    public static void insertValue(JsonObjectBuilder job, String key, Object value) throws JsonConversionException {
        if (value == null) {
            job.add(key, JsonValue.NULL);
            return;
        }
        if (value instanceof String) {
            job.add(key, (String) value);
            return;
        }
        if (value instanceof Integer) {
            job.add(key, (Integer) value);
            return;
        }
        if (value instanceof Long) {
            job.add(key, (Long) value);
            return;
        }
        if (value instanceof Boolean) {
            job.add(key, (Boolean) value);
            return;
        }
        throw new JsonConversionException();
    }

    /**
     * Convert a JsonValue into a equivalent Java String.
     *
     * @param value the JsonValue (either String or Number)
     * @return the resulting java String
     * @throws JsonConversionException if any conversion error or type failure
     */
    public static String getValueToString(JsonValue value) throws JsonConversionException {
        if (value.getValueType() == STRING) {
            return ((JsonString) value).getString();
        }
        if (value.getValueType() == NUMBER) {
            return Integer.toString(((JsonNumber) value).intValue());
        }
        throw new JsonConversionException();
    }

    /**
     * Convert a JsonValue into a equivalent Java String.
     *
     * @param value the JsonValue
     * @return the resulting java String
     * @throws JsonConversionException if any conversion error or type failure
     */
    public static String getStringValue(JsonValue value) throws JsonConversionException {
        if (value.getValueType() == STRING) {
            return ((JsonString) value).getString();
        }
        throw new JsonConversionException();
    }

    /**
     * Write a String value into a JsonObject.
     *
     * @param generator the Json Generator being used to create the Json object
     * @param key the key to be used
     * @param value the value to be used
     */
    public static void writeStringValue(JsonGenerator generator, String key, String value) {
        generator.write(key, value);
    }

    /**
     * Write a String value into a JsonArray.
     *
     * @param generator the Json Generator being used to create the Json array
     * @param value the value to be inserted
     */
    public static void writeStringValue(JsonGenerator generator, String value) {
        generator.write(value);
    }

    /**
     * Convert a JsonValue into a equivalent Java Integer.
     *
     * @param value the JsonValue
     * @return the resulting java Integer
     * @throws JsonConversionException if any conversion error or type failure
     */
    public static Integer getIntegerValue(JsonValue value) throws JsonConversionException {
        if (value.getValueType() == NUMBER) {
            return ((JsonNumber) value).intValue();
        }
        throw new JsonConversionException();
    }

    /**
     * Write an integer value into a JsonObject.
     *
     * @param generator the Json Generator being used to create the Json object
     * @param key the key to be used
     * @param value the value to be used
     */
    public static void writeIntegerValue(JsonGenerator generator, String key, int value) {
        generator.write(key, value);
    }

    /**
     * Write an integer value into a JsonArray.
     *
     * @param generator the Json Generator being used to create the Json array
     * @param value the value to be inserted
     */
    public static void writeIntegerValue(JsonGenerator generator, int value) {
        generator.write(value);
    }

    /**
     * Convert a JsonValue into a equivalent Java Boolean.
     *
     * @param value the JsonValue
     * @return the resulting java Boolean
     * @throws JsonConversionException if any conversion error or type failure
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
     * Write a boolean value into a JsonObject.
     *
     * @param generator the Json Generator being used to create the Json object
     * @param key the key to be used
     * @param value the value to be used
     */
    public static void writeBooleanValue(JsonGenerator generator, String key, boolean value) {
        generator.write(key, value ? JsonValue.TRUE : JsonValue.FALSE);
    }

    /**
     * Write a boolean value into a JsonArray.
     *
     * @param generator the Json Generator being used to create the Json array
     * @param value the value to be inserted
     */
    public static void writeBooleanValue(JsonGenerator generator, boolean value) {
        generator.write(value ? JsonValue.TRUE : JsonValue.FALSE);
    }

    /**
     * Convert a JsonValue into a equivalent Reference (foriegn key) java
     * Integer / null.
     *
     * @param value the JsonValue
     * @return the resulting java Integer or null
     * @throws JsonConversionException if any conversion error or type failure
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
     * Write a reference value (integer or null) into a JsonObject.
     *
     * @param generator the Json Generator being used to create the Json object
     * @param key the key to be used
     * @param value the value to be used
     */
    public static void writeReferenceValue(JsonGenerator generator, String key, Integer value) {
        if (value == null) {
            generator.write(key, JsonValue.NULL);
        } else {
            generator.write(key, value);
        }
    }

    /**
     * Write a reference value (integer or null) into a JsonArray.
     *
     * @param generator the Json Generator being used to create the Json array
     * @param value the value to be inserted
     */
    public static void writeReferenceValue(JsonGenerator generator, Integer value) {
        if (value == null) {
            generator.write(JsonValue.NULL);
        } else {
            generator.write(value);
        }
    }

    /**
     * Convert a JsonValue into a equivalent JsonArray.
     *
     * @param value the JsonValue
     * @return the resulting JsonArray
     * @throws JsonConversionException if any conversion error or type failure
     */
    public static JsonArray getArrayValue(JsonValue value) throws JsonConversionException {
        if (value.getValueType() == ARRAY) {
            return ((JsonArray) value);
        }
        throw new JsonConversionException();
    }

    // horrible bit of code!! - but is does work
    /**
     * Create a JsonValue using a String
     *
     * @param string the String
     * @return the equivalent JsonValue
     */
    public static JsonValue createJsonValue(String string) {
        return Json.createObjectBuilder()
                .add("value", string)
                .build()
                .getJsonString("value");
    }

    // horrible bit of code!! - but is does work
    /**
     * Create a JsonValue using an integer
     *
     * @param value the integer value
     * @return the equivalent JsonValue
     */
    public static JsonValue createJsonValue(int value) {
        return Json.createObjectBuilder()
                .add("value", value)
                .build()
                .getJsonNumber("value");
    }
}
