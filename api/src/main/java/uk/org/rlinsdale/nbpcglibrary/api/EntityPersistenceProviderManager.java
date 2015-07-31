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

    private final static Map<String, PersistenceUnitProvider> persistenceUnitProviders = new HashMap<>();
    private final static Map<String, Properties> dbproperties = new HashMap<>();

    private static final String DEFAULTUSER = "nbplatform";
    private static final String DEFAULTPASSWORD = "netbeans";

    /**
     * Setup EntityPersistenceProviders and associated PersistenceUnitProviders.
     *
     * @param in the configuration properties file
     * @throws IOException if problems with property file content
     */
    public static void init(InputStream in) throws IOException {
        Properties props = new Properties();
        props.load(in);
        init(props);
    }

    /**
     * Setup EntityPersistenceProviders and associated PersistenceUnitProviders.
     *
     * @param props the configuration properties
     * @throws IOException if dbkey missing from properties or requested Persistence Unit Provider does not exist
     */
    public static void init(Properties props) throws IOException {
        String dbkey = props.getProperty("key", "");
        if ("".equals(dbkey)) {
            throw new IOException("Persistence Properties - key not defined");
        }
        if (!props.containsKey("user")) {
            props.setProperty("user", DEFAULTUSER);
        }
        if (!props.containsKey("password")) {
            props.setProperty("password", DEFAULTPASSWORD);
        }
        dbproperties.put(dbkey, props);
        String puptype = props.getProperty("persistenceunitprovidertype");
        Collection<? extends PersistenceUnitProviderFactory> pupfactories = Lookup.getDefault().lookupResult(PersistenceUnitProviderFactory.class).allInstances();
        for (PersistenceUnitProviderFactory pupfactory : pupfactories) {
            if (pupfactory.getType().equals(puptype)) {
                PersistenceUnitProvider pup = pupfactory.createPersistenceUnitProvider(props);
                persistenceUnitProviders.put(dbkey, pup);
                return;
            }
        }
        throw new LogicException("Unknown PersistenceUnitProvider requested in Persistence Properties");
    }

    /**
     * Get the required Persistence Unit Provider. The provider for a particular
     * dbkey is a singleton and is cached in this module.
     *
     * @param dbkey the database key
     * @return the persistence unit provider
     * cannot be found.
     */
    private static PersistenceUnitProvider getPersistenceUnitProvider(String dbkey) {
        PersistenceUnitProvider pup = persistenceUnitProviders.get(dbkey);
        if (pup != null) {
            return pup;
        }
        throw new LogicException("Unknown PersistenceUnitProvider type used in Persistence Properties");
    }

    /**
     * Get an Entity Persistence Provider.
     *
     * @param dbkey the EntityPersistenceUnitProvider key.
     * @param entityname the entity name
     * @return the EntityPersistenceProvider
     * cannot be found.
     */
    public static EntityPersistenceProvider getEntityPersistenceProvider(String dbkey, String entityname) {
        Properties props = dbproperties.get(dbkey);
        if (props == null) {
            throw new LogicException("Properties for " + dbkey + " are not available");
        }
        String puptype = props.getProperty("entitypersistenceprovidertype");
        Collection<? extends EntityPersistenceProviderFactory> eppfactories = Lookup.getDefault().lookupResult(EntityPersistenceProviderFactory.class).allInstances();
        for (EntityPersistenceProviderFactory eppfactory : eppfactories) {
            if (eppfactory.getType().equals(puptype)) {
                try {
                    return eppfactory.createEntityPersistenceProvider(entityname, props, getPersistenceUnitProvider(dbkey));
                } catch (IOException ex) {
                    throw new LogicException("getEntityPersistenceProvide() failed: " + ex.getMessage());
                }
            }
        }
        throw new LogicException("Unknown EntityPersistenceProvider type used in Persistence Properties");
    }
    
    /**
     * Get an Entity Persistence Provider for an ordered entity.
     *
     * @param dbkey the EntityPersistenceUnitProvider key.
     * @param entityname the entity name
     * @param idx the index column name
     * @return the EntityPersistenceProvider
     * cannot be found.
     */
    public static EntityPersistenceProvider getEntityPersistenceProvider(String dbkey, String entityname, String idx) {
        Properties props = dbproperties.get(dbkey);
        if (props == null) {
            throw new LogicException("Properties for " + dbkey + " are not available");
        }
        String puptype = props.getProperty("entitypersistenceprovidertype");
        Collection<? extends EntityPersistenceProviderFactory> eppfactories = Lookup.getDefault().lookupResult(EntityPersistenceProviderFactory.class).allInstances();
        for (EntityPersistenceProviderFactory eppfactory : eppfactories) {
            if (eppfactory.getType().equals(puptype)) {
                try {
                    return eppfactory.createEntityPersistenceProvider(entityname, props, getPersistenceUnitProvider(dbkey), idx);
                } catch (IOException ex) {
                    throw new LogicException("getEntityPersistenceProvide() failed: " + ex.getMessage());
                }
            }
        }
        throw new LogicException("Unknown EntityPersistenceProvider type used in Persistence Properties");
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
