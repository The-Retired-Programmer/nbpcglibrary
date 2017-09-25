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
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityManager;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;

/**
 * Manages the list of Entities. The list of Entities is lazy loaded when
 * required.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the eEntity Class
 * @param <P> the parent Entity class
 */
public class EntityReferenceFilterSet<E extends Entity, P extends CoreEntity> extends EntityReferenceSet<E,P> {

    private final String columnname;
    private final Object columnvalue;

    /**
     * Constructor.
     *
     * @param name the set name (for reporting)
     * @param columnname the column name for use in selection equality filter
     * @param columnvalue the column value for use in the selection equality
     * filter
     * @param emclass the associated entity manager class
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public EntityReferenceFilterSet(String name, String columnname, Object columnvalue, Class<? extends EntityManager> emclass) {
        super(name,emclass);
        this.columnvalue = columnvalue;
        this.columnname = columnname;
    }
    
    @Override
    protected List<Integer> getPrimaryKeySet() {
//        return epp.find(columnname, columnvalue);
        return null;
    }
}
