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

import java.util.Comparator;
import java.util.List;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityManager;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;

/**
 * Manages the list of Entities - implements a sortable entity lists
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the Entity Class
 * @param <P> the parent Entity Class
 * @param <F> the fields enum type for the entity
 */
public class EntitySortedReferenceFilterSet<E extends Entity, P extends CoreEntity, F> extends EntitySortedReferenceSet<E, P, F> {

    private final String columnname;
    private final Object columnvalue;

    /**
     * Constructor.
     *
     * @param name the set name (for reporting)
     * @param comparator the comparator to be used to sort the list
     * @param columnname the column name to be used in filter
     * @param columnvalue the value to be used in the filter
     * @param emclass the associated entity manager class
     */
    public EntitySortedReferenceFilterSet(String name, Comparator<E> comparator, String columnname, Object columnvalue, Class<? extends EntityManager> emclass) {
        super(name, comparator, emclass);
        this.columnvalue = columnvalue;
        this.columnname = columnname;
    }
    
    @Override
    protected List<Integer> getPrimaryKeySet() {
//        return epp.find(columnname, columnvalue);
        return null;
    }


}
