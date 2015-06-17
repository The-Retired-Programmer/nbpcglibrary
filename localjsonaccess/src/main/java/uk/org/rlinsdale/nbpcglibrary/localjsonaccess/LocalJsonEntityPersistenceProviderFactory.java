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
package uk.org.rlinsdale.nbpcglibrary.localjsonaccess;

import java.io.IOException;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterLog;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProvider;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProviderFactory;

/**
 * A Factory to create EntityPersistenceProviders for local Json datasources.
 * 
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
@RegisterLog("nbpcglib.localJsonPersistenceUnitProvider")
@ServiceProvider(service = EntityPersistenceProviderFactory.class)
public class LocalJsonEntityPersistenceProviderFactory implements EntityPersistenceProviderFactory<Integer ,LocalJsonPersistenceUnitProvider, LocalJsonPersistenceUnitProviderFactory> {

    @Override
    public String getType() {
        return "local-json";
    }
    
    @Override
    public Class<LocalJsonPersistenceUnitProviderFactory> getPersistenceUnitProviderFactoryClass() {
        return LocalJsonPersistenceUnitProviderFactory.class;
    }

    @Override
    public EntityPersistenceProvider createEntityPersistenceProvider(String entityname, Properties p, LocalJsonPersistenceUnitProvider pup) throws IOException {
            LocalJsonAutoIDEntityPersistenceProvider epp = new LocalJsonAutoIDEntityPersistenceProvider();
            epp.init(entityname, p, pup);
            return epp;
    }
}
