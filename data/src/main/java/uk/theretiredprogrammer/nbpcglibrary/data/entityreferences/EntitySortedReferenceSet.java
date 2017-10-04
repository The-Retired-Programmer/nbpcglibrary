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
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
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
public class EntitySortedReferenceSet<R extends IdTimestampBaseEntity, E extends Entity, P extends CoreEntity, F> extends EntityReferenceSet<R, E, P> {

    private final Comparator<E> comparator;
    private boolean unsorted = true;
    private final ChildListener childListener;

    /**
     * Constructor.
     *
     * @param entitycreator a creator function for the Entity
     * @param restclass class of the rest client for this entity
     * @param comparator the comparator to be used to sort the list
     */
    public EntitySortedReferenceSet(Function<R,E> entitycreator, Class<? extends Rest<R>> restclass, Comparator<E> comparator) {
        super(entitycreator, restclass);
        childListener = new ChildListener();
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
            childList.add(new EntityReference<>(entitycreator, restclass, e));
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

    private class ChildListener extends Listener {

        @Override
        public void action(Object p) {
            sort();
        }
    }
}
