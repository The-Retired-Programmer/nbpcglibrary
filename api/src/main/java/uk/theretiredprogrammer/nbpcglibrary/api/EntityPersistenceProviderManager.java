/*
 * Copyright 2014-2017 Richard Linsdale.
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
package uk.theretiredprogrammer.nbpcglibrary.api;

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
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
