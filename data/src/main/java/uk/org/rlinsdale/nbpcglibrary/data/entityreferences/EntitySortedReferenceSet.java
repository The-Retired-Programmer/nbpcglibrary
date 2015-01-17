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
import java.util.Comparator;
import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManagerRO;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRO;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;

/**
 * Manages the list of Entities - implements a sortable entity lists
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class
 * @param <F> the Fields enum class
 */
public class EntitySortedReferenceSet<E extends EntityRO, F> extends EntityReferenceSet<E, F> {

    private final Comparator<E> comparator;
    private boolean unsorted = true;
    private final ChildListener childListener;
    private final F field;

    /**
     * Constructor.
     *
     * @param name the set name (for reporting)
     * @param field field identifier
     * @param comparator the comparator to be used to sort the list
     * @param columnname the column name for use in selection equality filter
     * @param columnvalue the column value for use in the selection equality
     * filter
     * @param emclass the associated entity manager class
     */
    public EntitySortedReferenceSet(String name, F field, Comparator<E> comparator, String columnname, int columnvalue, Class<? extends EntityManagerRO> emclass) {
        super(name, field, columnname, columnvalue, emclass);
        this.field = field;
        childListener = new ChildListener(name);
        this.comparator = comparator;
    }

    /**
     * Constructor.
     *
     * @param name the set name (for reporting)
     * @param field field identifier
     * @param comparator the comparator to be used to sort the list
     * @param emclass the associated entity manager class
     */
    public EntitySortedReferenceSet(String name, F field, Comparator<E> comparator, Class<? extends EntityManagerRO> emclass) {
        super(name, field, emclass);
        this.field = field;
        childListener = new ChildListener(name);
        this.comparator = comparator;
    }

    /**
     * Sort the entity List
     */
    protected final void sort() {
        List<E> el = super.get();
        java.util.Collections.sort(el, comparator);

        childList = new ArrayList<>();
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
