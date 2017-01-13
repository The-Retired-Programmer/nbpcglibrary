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
import java.util.Properties;

/**
 * Creates a EntityPersistenceProvider.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <K> the Primary Key Class
 * @param <D> the type of the PersistenceUnitProvider
 * @param <F> the type of the PersistenceUnitProviderFactory
 *
 */
public interface EntityPersistenceProviderFactory<K, D extends PersistenceUnitProvider, F extends PersistenceUnitProviderFactory<D>> {

    /**
     * Get the type of EntityPersistenceProvider which this Factory creates.
     *
     * @return the type string
     */
    public String getType();

    /**
     * Create an EntityPersistenceProvider.
     *
     * @param entityname the entity name to be managed
     * @param p the properties which define the provider configuration
     * @param persistenceUnitProvider the PersistenceUnitProvider to be used
     * @return the EntityPersistenceProvider
     * @throws IOException in cases of problems of creating the provider
     */
    public EntityPersistenceProvider<K> createEntityPersistenceProvider(String entityname, Properties p, D persistenceUnitProvider) throws IOException;
    
    /**
     * Create an EntityPersistenceProvider.
     *
     * @param entityname the entity name to be managed
     * @param p the properties which define the provider configuration
     * @param persistenceUnitProvider the PersistenceUnitProvider to be used
     * @param idx the index column name (for ordered entities)
     * @return the EntityPersistenceProvider
     * @throws IOException in cases of problems of creating the provider
     */
    public EntityPersistenceProvider<K> createEntityPersistenceProvider(String entityname, Properties p, D persistenceUnitProvider, String idx) throws IOException;
}
