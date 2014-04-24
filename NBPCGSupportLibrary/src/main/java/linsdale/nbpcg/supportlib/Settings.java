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
package linsdale.nbpcg.supportlib;

import org.openide.util.NbPreferences;

/**
 * Provide access to persistent application settings(/variables)
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class Settings {

    /**
     * Get a value from settings
     *
     * @param key the key
     * @param defaultvalue the default value (used if setting has not be
     * defined)
     * @return the setting value (or default value)
     */
    public static String get(String key, String defaultvalue) {
        return NbPreferences.root().get(key, defaultvalue);
    }

    /**
     * Get a value from settings
     *
     * @param key the key
     * @return the setting value (null if setting has not been defined)
     */
    public static String get(String key) {
        return get(key, null);
    }

    /**
     * Set a value in settings
     *
     * @param key the key
     * @param value the settings value
     */
    public static void set(String key, String value) {
        NbPreferences.root().put(key, value);
    }
}
