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

import java.util.Properties;

/**
 * Creates a DataAccessManager.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <D> the type of the PersistenceUnitProvider
 * @param <F> the type of the PersistenceUnitProviderFactory
 *
 */
public interface EntityPersistenceProviderFactory<D extends PersistenceUnitProvider, F extends PersistenceUnitProviderFactory> {

    /**
     * Get the type of EntityPersistenceProvider which this Factory creates.
     *
     * @return the type string
     */
    public String getType();

    /**
     * Get the class of the PersistenceUnitProvider associated with this
     * EntityPersistenceProvider.
     *
     * @return the PersistenceUnitProvider class
     */
    public Class<F> getPersistenceUnitProviderFactoryClass();

    /**
     * Create an EntityPersistenceProvider.
     *
     * @param entityname the entity name to be managed
     * @param p the properties which define the provider configuration
     * @param persistenceUnitProvider the PersistenceUnitProvider to be used
     * @return the EntityPersistenceProvider
     */
    public EntityPersistenceProvider createEntityPersistenceProvider(String entityname, Properties p, D persistenceUnitProvider);
}
