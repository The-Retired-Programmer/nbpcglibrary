/*
 * Copyright 2015-2017 Richard Linsdale.
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
package uk.theretiredprogrammer.nbpcglibrary.mysql;

import java.io.IOException;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterLog;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityPersistenceProvider;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityPersistenceProviderFactory;

/**
 * A Factory to create EntityPersistenceProviders for local MySQL datasources.
 * 
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@RegisterLog("nbpcglib.localJsonPersistenceUnitProvider")
@ServiceProvider(service = EntityPersistenceProviderFactory.class)
public class LocalMySQLEntityPersistenceProviderFactory implements EntityPersistenceProviderFactory<Integer ,LocalMySQLPersistenceUnitProvider, LocalMySQLPersistenceUnitProviderFactory> {

    @Override
    public String getType() {
        return "local-mysql";
    }

    @Override
    public EntityPersistenceProvider createEntityPersistenceProvider(String entityname, Properties p, LocalMySQLPersistenceUnitProvider pup) throws IOException {
            LocalMySQLAutoIDEntityPersistenceProvider epp = new LocalMySQLAutoIDEntityPersistenceProvider();
            epp.init(entityname, p, pup);
            return epp;
    }
    
    @Override
    public EntityPersistenceProvider createEntityPersistenceProvider(String entityname, Properties p, LocalMySQLPersistenceUnitProvider pup, String idx) throws IOException {
             LocalMySQLAutoIDEntityPersistenceProvider epp = new  LocalMySQLAutoIDEntityPersistenceProvider();
            epp.init(entityname, idx, p, pup);
            return epp;
    }
}
