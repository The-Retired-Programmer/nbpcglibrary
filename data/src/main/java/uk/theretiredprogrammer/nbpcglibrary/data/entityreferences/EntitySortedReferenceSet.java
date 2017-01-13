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
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityFieldChangeEventParams;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;

/**
 * Manages the list of Entities - implements a sortable entity lists
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <K> the primary key class for the entity
 * @param <E> the Entity Class
 * @param <P> the parent Entity Class
 * @param <F> the fields enum type for the entity
 */
public class EntitySortedReferenceSet<K, E extends Entity<K, E, P, F>, P extends CoreEntity, F> extends EntityReferenceSet<K, E, P> {

    private final Comparator<E> comparator;
    private boolean unsorted = true;
    private final ChildListener childListener;

    /**
     * Constructor.
     *
     * @param name the set name (for reporting)
     * @param comparator the comparator to be used to sort the list
     * @param emclass the associated entity manager class
     */
    public EntitySortedReferenceSet(String name, Comparator<E> comparator, Class<? extends EntityManager> emclass) {
        super(name, emclass);
        childListener = new ChildListener(name);
        this.comparator = comparator;
    }

    /**
     * Sort the entity List
     */
    protected final void sort() {
        List<E> el = super.get();
        java.util.Collections.sort(el, comparator);
        childList.clear();
        el.stream().forEach((e) -> {
            childList.add(new EntityReference<>(name, e, em));
        });
        unsorted = false;
        fireSetChange();
    }

    @Override
    public List<E> get() {
        if (unsorted) {
            sort();
            List<E> el = super.get();
            el.stream().forEach((e) -> {
                e.addFieldListener(childListener);
            });
            return el;
        }
        return super.get();
    }

    @Override
    public final void add(E e) {
        int index = 0;
        e.addFieldListener(childListener);
        for (E l : get()) {
            if (comparator.compare(e, l) <= 0) {
                add(index, e);
                return;
            }
            index++;
        }
        add(index, e);
    }

    @Override
    public boolean remove(E e) {
        if (super.remove(e)) {
            e.removeFieldListener(childListener);
            return true;
        }
        return false;
    }

    @Override
    public void restoreState() {
        super.get().stream().forEach((e) -> {
            e.removeFieldListener(childListener);
        });
        super.restoreState();
        unsorted = true;
    }

    private class ChildListener extends Listener<EntityFieldChangeEventParams<F>> {

        public ChildListener(String name) {
            super(name);
        }

        @Override
        public void action(EntityFieldChangeEventParams<F> p) {
            sort();
        }
    }
}
