/*
 * Copyright (C) 2015 Richard Linsdale.
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

import uk.org.rlinsdale.nbpcglibrary.api.EntityFields;
import uk.org.rlinsdale.nbpcglibrary.api.Timestamp;
import uk.org.rlinsdale.nbpcglibrary.common.Settings;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class LocalJsonAutoIDEntityPersistenceProvider extends LocalJsonEntityPersistenceProvider<Integer> {

    @Override
    public Integer getPK(EntityFields ef) {
        return (Integer) ef.get("id");
    }

    @Override
    public void autoGenPrimaryKeyHook(EntityFields ef) {
        autoGenPrimaryKeyAction(ef); // create a generated primary key
    }

    @Override
    public void addTimestampInfo(EntityFields ef) {
        String user = Settings.get("Usercode", "????");
        String when = (new Timestamp()).toSQLString();
        ef.put("createdby", user);
        ef.put("createdon", when);
        ef.put("updatedby", user);
        ef.put("updatedon", when);
    }

    @Override
    public void updateTimestampInfo(EntityFields ef) {
        String user = Settings.get("Usercode", "????");
        String when = (new Timestamp()).toSQLString();
        ef.put("updatedby", user);
        ef.put("updatedon", when);
    }
}
