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
 * EntityPersistenceManager Manager.
 *
 * Cache of required EntityPersistenceManagers
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class EntityPersistenceManagerManager {

    private final static Map<String, EntityPersistenceManager> EntityPersistenceManagers = new HashMap<>();

    private static final String DEFAULTUSER = "nbplatform";
    private static final String DEFAULTPASSWORD = "netbeans";

    /**
     * Setup EntityPersistenceManager Properties.
     *
     * @param in the configuration properties file
     * @param entitynames list of all entity names
     * @throws java.io.IOException
     */
    public static void set(InputStream in, String[] entitynames) throws IOException {
        Properties props = new Properties();
        props.load(in);
        set(props, entitynames);
    }

    /**
     * Setup EntityPersistenceManager Properties.
     *
     * @param props the configuration properties
     * @param entitynames list of all entity names
     * @throws java.io.IOException
     */
    public static void set(Properties props, String[] entitynames) throws IOException {
        String key = props.getProperty("key", "");
        if ("".equals(key)) {
            throw new EntityPersistenceManagerPropertiesException("Persistence Properties - key not defined");
        }
        if (!props.containsKey("user")) {
            props.setProperty("user", DEFAULTUSER);
        }
        if (!props.containsKey("password")) {
            props.setProperty("password", DEFAULTPASSWORD);
        }
        Collection<? extends EntityPersistenceManagerFactory> factories = Lookup.getDefault().lookupResult(EntityPersistenceManagerFactory.class).allInstances();
        for (EntityPersistenceManagerFactory factory : factories) {
            if (factory.getType().equals(props.getProperty("entitypersistencemanagertype"))) {
                Collection<? extends DataAccessManagerFactory> damfactories = Lookup.getDefault().lookupResult(factory.getDataAccessFactoryClass()).allInstances();
                for (DataAccessManagerFactory damfactory : damfactories) {
                    if (damfactory.getType().equals(props.getProperty("dataaccessmanagertype"))) {
                       DataAccessManager dam = damfactory.createDataAccessManager(props);
                        for (String entityname : entitynames) {
                            String ekey = key + "_" + entityname;
                            EntityPersistenceManagers.put(ekey, factory.createEntityPersistenceManager(entityname, props, dam));
                        }
                        return;
                    }
                }
                throw new DataAccessManagerPropertiesException("Unknown data access type used in Persistence Properties");
            }
        }
        throw new EntityPersistenceManagerPropertiesException("Unknown entity persistence type used in Persistence Properties");
    }

    /**
     * Get an EntityPersistenceManager.
     *
     * @param key the EntityPersistenceManager key.
     * @param entityname the entity name
     * @return the DataAccessManager
     */
    public static EntityPersistenceManager getEntityPersistenceManager(String key, String entityname) {
        return EntityPersistenceManagers.get(key + "_" + entityname);
    }
}
