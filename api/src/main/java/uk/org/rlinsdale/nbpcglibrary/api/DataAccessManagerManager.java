/*
 * Copyright (C) 2014-2015 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.openide.util.Lookup;

/**
 * DataAccessManager Manager.
 *
 * Cache of required DataAccessManagers
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class DataAccessManagerManager {

    private final static Map<String, DataAccessManager> dataAccessManagers = new HashMap<>();

    private static final String DEFAULTUSER = "nbplatform";
    private static final String DEFAULTPASSWORD = "netbeans";
    
    /**
     * Setup DataAccessManagerFactory Properties.
     *
     * @param in the configuration properties file
     * @throws java.io.IOException
     */
    public static void set(InputStream in) throws IOException {
        Properties props = new Properties();
        props.load(in);
        set(props);
    }
        
    /**
     * Setup DataAccessManagerFactory Properties.
     * @param props the configuration properties
     * @throws java.io.IOException
     */
    public static void set(Properties props) throws IOException {
        String key = props.getProperty("key", "");
        if ("".equals(key)) {
            throw new DataAccessManagerPropertiesException("Persistence Properties - key not defined");
        }
        if (!props.containsKey("user")) {
            props.setProperty("user", DEFAULTUSER);
        }
        if (!props.containsKey("password")) {
            props.setProperty("password", DEFAULTPASSWORD);
        }
        //
        Collection<? extends DataAccessManagerFactory> factories = Lookup.getDefault().lookupResult(DataAccessManagerFactory.class).allInstances();
        for (DataAccessManagerFactory factory : factories) {
            if (factory.getType().equals(props.getProperty("dataaccessmanagertype"))) {
                dataAccessManagers.put(key, factory.createDataAccessManager(props));
                return;
            }
        }
        throw new DataAccessManagerPropertiesException("Unknown type used in Persistence Properties");
    }
    
    /**
     *  Get all DataAccessManagers
     * @return 
     */
    public static Collection<? extends DataAccessManager> getAllDataAccessManagers() {
        return dataAccessManagers.values();
    }

    /**
     * Get a DataAccessManager.
     *
     * @param props the configuration properties
     * @return the DataAccessManager
     */
    public static DataAccessManager getDataAccessManager(Properties props) {
        return dataAccessManagers.get(props.getProperty("key"));
    }
}
