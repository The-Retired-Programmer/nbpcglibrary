/*
 * Copyright 2015-2017 Richard Linsdale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.nbpcglibrary.lifecycle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Application Properties Instance.
 *
 * These are the Application configuration properties.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
class ApplicationProperties {

    private static ApplicationProperties instance;
    private Properties properties = new Properties();

    /**
     * Setup a new Applications Properties Instance.
     *
     * @param in the input stream from which the application properties are to
     * be read
     * @return the application properties instance
     * @throws ApplicationPropertiesException if problems reading/parse properties
     */
    static ApplicationProperties set(InputStream in) throws ApplicationPropertiesException {
        return instance = new ApplicationProperties(in);
    }

    /**
     * Get the currently defined Applications Properties Instance.
     *
     * @return the application properties instance
     */
    static ApplicationProperties getDefault() {
        return instance;
    }

    private ApplicationProperties(InputStream in) throws ApplicationPropertiesException {
        try {
            properties.load(in);
        } catch (IOException ex) {
            throw new ApplicationPropertiesException(ex);
        }
    }

    /**
     * Get the Application Property value for a specified property.
     *
     * @param name the property name
     * @return the property value (or and empty string if the property name has
     * not been defined)
     */
    String get(String name) {
        return get(name, "");
    }

    /**
     * Get the Application Property value for a specified property.
     *
     * @param name the property name
     * @param missing the value to be return if the property name has not been
     * defined
     * @return the property value
     */
    String get(String name, String missing) {
        return properties.getProperty(name, missing);
    }
}
