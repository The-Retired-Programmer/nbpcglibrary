/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package linsdale.nbpcg.datasupportlib.entityreferences;

import java.util.ArrayList;
import java.util.List;
import linsdale.nbpcg.datasupportlib.entity.EntityManagerRW;
import linsdale.nbpcg.datasupportlib.entity.EntityRWIndexed;
import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.LogicException;

/**
 * Manages the list of child Entities - extends referencedsortableentityset to
 * implement reorderable child entity lists
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
public class EntityIndexedReferenceSet<E extends EntityRWIndexed> extends EntityReferenceSet<E> {

    public EntityIndexedReferenceSet(String name, IntWithDescription field, String columnname, int columnvalue, Class<? extends EntityManagerRW> emclass) {
        super(name, field, columnname, columnvalue, emclass);
    }

    public EntityIndexedReferenceSet(String name, IntWithDescription field, Class<? extends EntityManagerRW> emclass) {
        super(name, field, emclass);
    }

    /**
     * Reorder the list of child entities
     *
     * @param perm the reordered index information
     * @param offsetMin
     * @param offsetMax
     */
    public void reorder(int[] perm, int offsetMin, int offsetMax) {
        int clsize = count();
        if ((offsetMax - offsetMin +1)!= clsize) {
            throw new LogicException("EntityIndexedReferenceSet - mismatch between offset range and childList.size()");
        }
        int[] permextract = new int[clsize];
        int permin = 0;
        for (int i = 0; i < perm.length; i++) {
            int pi = perm[i];
            if ( pi >= offsetMin && pi <= offsetMax) {
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
