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

import java.util.ArrayList;
import java.util.List;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityManager;

/**
 * Manages the list of Entities - implements a re-orderable entity lists
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the Entity Class
 * @param <P> the parent entity
 */
public class EntityIndexedReferenceSet<E extends Entity, P extends CoreEntity> extends EntityReferenceSet<E, P> {

    /**
     * Constructor.
     *
     * @param name the set name (for reporting)
     * @param emclass the associated entity manager class
     */
    public EntityIndexedReferenceSet(String name, Class<? extends EntityManager> emclass) {
        super(name, emclass);
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
        List<EntityReference<E, P>> reordered = new ArrayList<>(clsize);
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
            if (i != e.getIdx()) {
                e.setIdx(i);
            }
        }
    }
}
