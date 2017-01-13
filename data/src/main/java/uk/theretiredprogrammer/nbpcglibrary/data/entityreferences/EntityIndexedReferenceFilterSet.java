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
package uk.theretiredprogrammer.nbpcglibrary.data.entityreferences;

import java.util.List;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityManager;

/**
 * Manages the list of Entities - implements a re-orderable entity lists
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <K> the primary key class
 * @param <E> the Entity Class
 * @param <P> the parent entity
 */
public class EntityIndexedReferenceFilterSet<K, E extends Entity<K, E, P, ?>, P extends CoreEntity> extends EntityIndexedReferenceSet<K, E, P> {

    private final String columnname;
    private final Object columnvalue;

    /**
     * Constructor.
     *
     * @param name the set name (for reporting)
     * @param columnname the column name to be used in filter
     * @param columnvalue the value to be used in the filter
     * @param emclass the associated entity manager class
     */
    public EntityIndexedReferenceFilterSet(String name, String columnname, Object columnvalue, Class<? extends EntityManager> emclass) {
        super(name, emclass);
        this.columnvalue = columnvalue;
        this.columnname = columnname;
    }

    @Override
    protected List<K> getPrimaryKeySet() {
        return epp.find(columnname, columnvalue);
    }
}
