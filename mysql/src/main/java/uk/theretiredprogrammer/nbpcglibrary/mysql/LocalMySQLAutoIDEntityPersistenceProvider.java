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

import uk.theretiredprogrammer.nbpcglibrary.api.EntityFields;
import uk.theretiredprogrammer.nbpcglibrary.api.Timestamp;
import uk.theretiredprogrammer.nbpcglibrary.common.Settings;

/**
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class LocalMySQLAutoIDEntityPersistenceProvider extends LocalMySQLEntityPersistenceProvider<Integer> {

    
    @Override
    public Integer getPK(EntityFields ef) {
        return (Integer) ef.get("id");
    }

    @Override
    public void autoGenPrimaryKeyHook(EntityFields ef) {
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
