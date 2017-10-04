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
import java.util.function.Function;
import uk.theretiredprogrammer.nbpcglibrary.api.IdTimestampBaseEntity;
import uk.theretiredprogrammer.nbpcglibrary.api.Rest;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;

/**
 * Manages the list of Entities - implements a sortable entity lists
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <R> the BasicEntity (data transfer) Class
 * @param <E> the Entity Class
 * @param <P> the parent Entity Class
 * @param <F> the fields enum type for the entity
 */
public class EntitySortedReferenceFilterSet<R extends IdTimestampBaseEntity, E extends Entity, P extends CoreEntity, F> extends EntitySortedReferenceSet<R, E, P, F> {

    private final String columnname;
    private final int columnvalue;

    /**
     * Constructor.
     *
     * @param entitycreator a creator function for the Entity
     * @param restclass class of the rest client for this entity
     * @param comparator the comparator to be used to sort the list
     * @param columnname the column name to be used in filter
     * @param columnvalue the value to be used in the filter
     */
    public EntitySortedReferenceFilterSet(Function<R,E> entitycreator, Class<? extends Rest<R>> restclass, Comparator<E> comparator, String columnname, int columnvalue) {
        super(entitycreator, restclass, comparator);
        this.columnvalue = columnvalue;
        this.columnname = columnname;
    }
    
    @Override
    protected List<R> getPrimaryKeySet(Rest<R> rest) {
        return rest.getMany(columnname, columnvalue);
    }
}
