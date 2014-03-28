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
import java.util.Comparator;
import java.util.List;
import linsdale.nbpcg.datasupportlib.entity.EntityManagerRO;
import linsdale.nbpcg.datasupportlib.entity.EntityRO;
import linsdale.nbpcg.datasupportlib.entity.FieldChangeListenerParams;
import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.Listener;

/**
 * Manages the list of child Entities - extends referencedentityset to implement
 * sortable child entity lists
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
public class EntitySortedReferenceSet<E extends EntityRO> extends EntityReferenceSet<E> {

    private final Comparator<E> comparator;
    private boolean unsorted = true;
    private final ChildListener childListener;
    private final IntWithDescription field;

    public EntitySortedReferenceSet(String name, IntWithDescription field, Comparator<E> comparator, String columnname, int columnvalue, Class<? extends EntityManagerRO> emclass) {
        super(name, field, columnname, columnvalue, emclass);
        this.field = field;
        childListener = new ChildListener(name);
        this.comparator = comparator;
    }

    public EntitySortedReferenceSet(String name, IntWithDescription field, Comparator<E> comparator, Class<? extends EntityManagerRO> emclass) {
        super(name, field, emclass);
        this.field = field;
        childListener = new ChildListener(name);
        this.comparator = comparator;
    }
    
     /**
     * Sort the child entity List
     *
     *
     */
    protected final void sort() {
        List<E> el = super.get();
        java.util.Collections.sort(el, comparator);
        
        childList = new ArrayList<>();
        for (E e : el) {
            childList.add(new EntityReference<>(name, e, em));
        }
        unsorted = false;
        fireSetChange();
    }

    /**
     * Get the list of Child Entities
     *
     * @return List of child entities
     */
    @Override
    public List<E> get() {
        if (unsorted) {
            sort();
            List<E> el = super.get();
            for (E e : el) {
                e.addFieldListener(childListener);
            }
            return el;
        }
        return super.get();
    }

    /**
     * Add a child entity to the child entity list
     *
     * @param e the Child Entity
     */
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

    /**
     * Remove an entity from the child entity List
     *
     * @param e the Child Entity
     * @return 
     */
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
        for (E e : super.get()) {
            e.removeFieldListener(childListener);
        }
        super.restoreState();
        unsorted = true;
    }

    private class ChildListener extends Listener<FieldChangeListenerParams> {

        public ChildListener(String name) {
            super(name);
        }

        @Override
        public void action(FieldChangeListenerParams p) {
            sort();
        }
    }
}