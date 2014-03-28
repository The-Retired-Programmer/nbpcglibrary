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
import linsdale.nbpcg.datasupportlib.dataaccess.DataAccessRO;
import linsdale.nbpcg.datasupportlib.entity.EntityManagerRO;
import linsdale.nbpcg.datasupportlib.entity.EntityRO;
import linsdale.nbpcg.datasupportlib.entity.SetChangeListenerParams;
import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.Listener;
import linsdale.nbpcg.supportlib.Listening;
import linsdale.nbpcg.supportlib.Rule;
import org.openide.util.Lookup;

/**
 * Manages the list of child Entities. The list of Entities is lazy loaded when
 * required.
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
public class EntityReferenceSet<E extends EntityRO> {

    protected final EntityManagerRO<E> em;
    private final DataAccessRO dataAccess;
    protected List<EntityReference<E>> childList;
    private final String columnname;
    private final int columnvalue;
    protected final String name;
    private final Listening<SetChangeListenerParams> setListening;
    private final IntWithDescription field;

    public EntityReferenceSet(String name, IntWithDescription field, String columnname, int columnvalue, Class<? extends EntityManagerRO> emclass) {
        this(emclass, name, field, columnname, columnvalue);
        if (columnvalue > 0) {
            createChildList(em, dataAccess.find(columnname, columnvalue));
        } else {
            childList = new ArrayList<>();
        }
    }

    public EntityReferenceSet(String name, IntWithDescription field, Class<? extends EntityManagerRO> emclass) {
        this(emclass, name, field, null, 0);
        createChildList(em, dataAccess.find());
    }
    
    private EntityReferenceSet(Class<? extends EntityManagerRO> emclass, String name, IntWithDescription field, String columnname, int columnvalue) {
        this.name = name;
        this.field = field;
        setListening = new Listening<>(name + "/set");
        em = Lookup.getDefault().lookup(emclass);
        this.columnvalue = columnvalue;
        this.columnname = columnname;
        this.dataAccess = em.getDataAccess();
    }

    private void createChildList(EntityManagerRO<E> em, List<Integer> refs) {
        childList = new ArrayList<>();
        for (int id : refs) {
            childList.add(new EntityReference<>(name, id, em));
        }
    }
    
    public final void addSetListener(Listener<SetChangeListenerParams> listener) {
        setListening.addListener(listener);
    }
    
    public final void addSetListener(Listener<SetChangeListenerParams> listener, int flags) {
        setListening.addListener(listener, flags);
    }

    public final void removeSetListener(Listener<SetChangeListenerParams> listener) {
        setListening.removeListener(listener);
    }
    
    protected void fireSetChange() {
        setListening.fire(new SetChangeListenerParams(field));
    }

    public void restoreState() {
        // TODO - we are not removing any new entities which are not cached - so we might have a leak here.
        createChildList(em, columnname == null ? dataAccess.find() : dataAccess.find(columnname, columnvalue));
    }

    /**
     * Get the count of child entities.
     *
     * return the count of child entities
     * @return 
     */
    public final int count() {
        return childList.size();
    }

    /**
     * Get the list of Child Entities
     *
     * @return List of child entities
     */
    public List<E> get() {
        List<E> el = new ArrayList<>();
        for (EntityReference<E> ref : childList) {
            el.add(ref.get());
        }
        return el;
    }

    /**
     * Add a child entity to the child entity list
     *
     * @param e the Child Entity
     */
    public void add(E e) {
        childList.add(new EntityReference<>(name, e, em));
        fireSetChange();
    }

    protected final void add(int index, E e) {
        childList.add(index, new EntityReference<>(name, e, em));
        fireSetChange();
    }

    /**
     * Remove an entity from the child entity List
     *
     * @param e the Child Entity
     * @return 
     */
    public boolean remove(E e) {
        return removeById(e.getId());
    }

    private boolean removeById(int id) {
        for (EntityReference<E> ref : childList) {
            if (ref.getId() == id) {
                if (childList.remove(ref)) {
                    fireSetChange();
                    return true;
                }
            }
        }
        return false;
    }
    
    public Rule getMaxRule(int max) {
        return new MaxRule(max);
    }

    public Rule addMinRule(int min) {
        return new MinRule(min);
    }

    private class MaxRule extends Rule {

        private final int max;

        public MaxRule(int max) {
            super("Too many " + name + "s defined");
            this.max = max;
        }

        @Override
        public boolean ruleCheck() {
            return count() <= max;
        }
    }

    private class MinRule extends Rule {

        private final int min;

        public MinRule(int min) {
            super("Too few " + name + "s defined");
            this.min = min;
        }

        @Override
        public boolean ruleCheck() {
            return count() >= min;
        }
    }
}
