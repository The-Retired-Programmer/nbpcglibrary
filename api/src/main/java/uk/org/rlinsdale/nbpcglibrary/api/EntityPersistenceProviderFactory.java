/*
 * Copyright (C) 2014-2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
import java.util.Properties;

/**
 * Creates a EntityPersistenceProvider.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
