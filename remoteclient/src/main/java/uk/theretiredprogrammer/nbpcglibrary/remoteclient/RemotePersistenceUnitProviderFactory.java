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
package uk.theretiredprogrammer.nbpcglibrary.remoteclient;

import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;
import uk.theretiredprogrammer.nbpcglibrary.api.PersistenceUnitProviderFactory;

/**
 * A Factory to create PersistenceUnitProvider for local MySQL databases.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@ServiceProvider(service = PersistenceUnitProviderFactory.class)
public class RemotePersistenceUnitProviderFactory implements PersistenceUnitProviderFactory<RemotePersistenceUnitProvider> {

    @Override
    public String getType() {
        return "remote";
    }

    @Override
    public RemotePersistenceUnitProvider createPersistenceUnitProvider(Properties p) {
        return new RemotePersistenceUnitProvider(p);
    }
}