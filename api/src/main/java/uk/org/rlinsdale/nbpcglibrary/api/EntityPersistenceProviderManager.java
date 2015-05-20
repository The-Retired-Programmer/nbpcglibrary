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
 * EntityPersistenceProvider Manager.
 *
 * Store of all required EntityPersistenceProviders and their associated
 * PersistenceUnitProviders
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class EntityPersistenceProviderManager {

    private final static Map<String, EntityPersistenceProvider> entityPersistenceProviders = new HashMap<>();
    private final static Map<String, PersistenceUnitProvider> persistenceUnitProviders = new HashMap<>();

    private static final String DEFAULTUSER = "nbplatform";
    private static final String DEFAULTPASSWORD = "netbeans";

    /**
     * Setup EntityPersistenceProviders and associated PersistenceUnitProviders.
     *
     * @param in the configuration properties file
     * @param entitynames list of all entity names in this PersistenceUnit
     * @throws IOException if problems with processing properties or creating
     * required Providers
     */
    public static void set(InputStream in, String[] entitynames) throws IOException {
        Properties props = new Properties();
        props.load(in);
        set(props, entitynames);
    }

    /**
     * Setup EntityPersistenceProviders and associated PersistenceUnitProviders.
     *
     * @param props the configuration properties
     * @param entitynames list of all entity names in this PersistenceUnit
     * @throws IOException if problems with processing properties or creating
     * required Providers
     */
    public static void set(Properties props, String[] entitynames) throws IOException {
        String key = props.getProperty("key", "");
        if ("".equals(key)) {
            throw new EntityPersistenceProviderManagerException("Persistence Properties - key not defined");
        }
        if (!props.containsKey("user")) {
            props.setProperty("user", DEFAULTUSER);
        }
        if (!props.containsKey("password")) {
            props.setProperty("password", DEFAULTPASSWORD);
        }
        Collection<? extends EntityPersistenceProviderFactory> eppfactories = Lookup.getDefault().lookupResult(EntityPersistenceProviderFactory.class).allInstances();
        for (EntityPersistenceProviderFactory eppfactory : eppfactories) {
            if (eppfactory.getType().equals(props.getProperty("entitypersistenceprovidertype"))) {
                Collection<? extends PersistenceUnitProviderFactory> pupfactories = Lookup.getDefault().lookupResult(eppfactory.getPersistenceUnitProviderFactoryClass()).allInstances();
                for (PersistenceUnitProviderFactory pupfactory : pupfactories) {
                    if (pupfactory.getType().equals(props.getProperty("persistenceunitprovidertype"))) {
                        PersistenceUnitProvider pup = pupfactory.createPersistenceUnitProvider(props);
                        for (String entityname : entitynames) {
                            String ekey = key + "_" + entityname;
                            entityPersistenceProviders.put(ekey, eppfactory.createEntityPersistenceProvider(entityname, props, pup));
                        }
                        persistenceUnitProviders.put(key, pup);
                        return;
                    }
                }
                throw new EntityPersistenceProviderManagerException("Unknown PersistenceUnitProvider type used in Persistence Properties");
            }
        }
        throw new EntityPersistenceProviderManagerException("Unknown EntityPersistenceProvider type used in Persistence Properties");
    }

    /**
     * Get an EntityPersistenceProvider.
     *
     * @param key the EntityPersistenceUnitProvider key.
     * @param entityname the entity name
     * @return the EntityPersistenceProvider
     */
    public static EntityPersistenceProvider getEntityPersistenceProvider(String key, String entityname) {
        return entityPersistenceProviders.get(key + "_" + entityname);
    }

    /**
     * Get all PersistenceUnitProviders
     *
     * @return the set of all PersistenceUnitProviders
     */
    public static Collection<? extends PersistenceUnitProvider> getAllPersistenceUnitProviders() {
        return persistenceUnitProviders.values();
    }
}
