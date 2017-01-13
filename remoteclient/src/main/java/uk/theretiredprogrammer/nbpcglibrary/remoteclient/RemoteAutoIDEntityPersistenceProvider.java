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

import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityFields;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;
import uk.theretiredprogrammer.nbpcglibrary.json.JsonConversionException;
import uk.theretiredprogrammer.nbpcglibrary.json.JsonUtil;

/**
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class RemoteAutoIDEntityPersistenceProvider extends RemoteEntityPersistenceProvider<Integer> {
    
    @Override
    protected Integer getPK(JsonValue v) {
        try {
            return JsonUtil.getIntegerValue(v);
        } catch (JsonConversionException ex) {
            throw new LogicException("getPK(jsonvalue) failed: non integer type presented");
        }
    }

    @Override
    protected void addPK(JsonObjectBuilder job, Integer pkey) {
        job.add("pkey", pkey);
    }

    @Override
    public Integer getPK(EntityFields ef) {
        return null;
    }

    @Override
    public void autoGenPrimaryKeyHook(EntityFields ef) {
    }

    @Override
    public void addTimestampInfo(EntityFields ef) {
    }

    @Override
    public void updateTimestampInfo(EntityFields ef) {
    }
}
