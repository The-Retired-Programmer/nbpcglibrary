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

import java.util.Properties;

/**
 * A Factory to creates instances of PersistenceUnitProviders.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <D> the class of the persistenceUnitProvider created
 *
 */
public interface PersistenceUnitProviderFactory<D extends PersistenceUnitProvider> {

    /**
     * Get the type of persistenceUnitProvider which this Factory creates.
     *
     * @return the type string
     */
    public String getType();

    /**
     * Create a PersistenceUnitProvider.
     *
     * @param p the properties which define the provider configuration
     * @return the PersistenceUnitProvider
     */
    public D createPersistenceUnitProvider(Properties p);
}
