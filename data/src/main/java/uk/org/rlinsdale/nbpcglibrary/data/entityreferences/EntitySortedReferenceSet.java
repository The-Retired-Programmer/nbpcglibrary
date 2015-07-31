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
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
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
