/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.util.ArrayList;
import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManagerRW;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRWIndexed;
import uk.org.rlinsdale.nbpcglibrary.common.IntWithDescription;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;

/**
 * Manages the list of Entities - implements a re-orderable entity lists
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class
 */
public class EntityIndexedReferenceSet<E extends EntityRWIndexed> extends EntityReferenceSet<E> {

    /**
     * Constructor.
     *
     * @param name the set name (for reporting)
     * @param field the IF for this set
     * @param columnname the column name for use in selection equality filter
     * @param columnvalue the column value for use in the selection equality
     * filter
     * @param emclass the associated entity manager class
     */
    public EntityIndexedReferenceSet(String name, IntWithDescription field, String columnname, int columnvalue, Class<? extends EntityManagerRW> emclass) {
        super(name, field, columnname, columnvalue, emclass);
    }

    /**
     * Constructor.
     *
     * @param name the set name (for reporting)
     * @param field the Id for this set
     * @param emclass the associated entity manager class
     */
    public EntityIndexedReferenceSet(String name, IntWithDescription field, Class<? extends EntityManagerRW> emclass) {
        super(name, field, emclass);
    }

    /**
     * Reorder the list of child entities
     *
     * @param perm the reordered index information
     * @param offsetMin the lower limit of the reordering
     * @param offsetMax the upper limit of the reordering
     */
    public void reorder(int[] perm, int offsetMin, int offsetMax) {
        int clsize = count();
        if ((offsetMax - offsetMin + 1) != clsize) {
            throw new LogicException("EntityIndexedReferenceSet - mismatch between offset range and childList.size()");
        }
        int[] permextract = new int[clsize];
        int permin = 0;
        for (int i = 0; i < perm.length; i++) {
            int pi = perm[i];
            if (pi >= offsetMin && pi <= offsetMax) {
                permextract[permin] = pi - offsetMin;
                permin++;
            }
        }
        List<EntityReference<E>> reordered = new ArrayList<>(clsize);
        for (int i = 0; i < clsize; i++) {
            reordered.add(null);
        }
        for (int i = 0; i < clsize; i++) {
            reordered.set(permextract[i], childList.get(i));
        }
        childList.clear();
        childList.addAll(reordered);
        fireSetChange();
    }

    /**
     * Persist the reordering.
     */
    public void persistReorder() {
        List<E> el = get();
        for (int i = 0; i < el.size(); i++) {
            E e = el.get(i);
            if (i != e.getIndex()) {
                e.setIndex(i);
            }
        }
    }
}
