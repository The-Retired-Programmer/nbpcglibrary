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
package uk.org.rlinsdale.nbpcglibrary.data.entityreferences;

import java.util.Comparator;
import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManager;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.CoreEntity;

/**
 * Manages the list of Entities - implements a sortable entity lists
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> the primary key class for the entity
 * @param <E> the Entity Class
 * @param <P> the parent Entity Class
 * @param <F> the fields enum type for the entity
 */
public class EntitySortedReferenceFilterSet<K, E extends Entity<K, E, P, F>, P extends CoreEntity, F> extends EntitySortedReferenceSet<K, E, P, F> {

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
    protected List<K> getPrimaryKeySet() {
        return epp.find(columnname, columnvalue);
    }


}
