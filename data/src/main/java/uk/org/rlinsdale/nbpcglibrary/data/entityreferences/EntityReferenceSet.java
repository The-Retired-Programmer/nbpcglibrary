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

import java.util.ArrayList;
import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.data.dataaccess.DataAccessRO;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManagerRO;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRO;
import uk.org.rlinsdale.nbpcglibrary.data.entity.SetChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.Event;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;
import org.openide.util.Lookup;
import uk.org.rlinsdale.nbpcglibrary.common.Event.ListenerMode;

/**
 * Manages the list of Entities. The list of Entities is lazy loaded when
 * required.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class
 * @param <F> the Fields enum class
 */
public class EntityReferenceSet<E extends EntityRO, F> {

    /**
     * The Entity Manager associated with the entities
     */
    protected final EntityManagerRO<E> em;
    private final DataAccessRO dataAccess;

    /**
     * The list of Entity References (referring to the entities)
     */
    protected List<EntityReference<E>> childList;
    private final String columnname;
    private final int columnvalue;

    /**
     * The name of the Set (for reporting purposes)
     */
    protected final String name;
    private final Event<SetChangeEventParams> setChangeEvent;
    private final F field;

    /**
     * Constructor.
     *
     * @param name the set name (for reporting)
     * @param field field identifier
     * @param columnname the column name for use in selection equality filter
     * @param columnvalue the column value for use in the selection equality
     * filter
     * @param emclass the associated entity manager class
     */
    public EntityReferenceSet(String name, F field, String columnname, int columnvalue, Class<? extends EntityManagerRO> emclass) {
        this(emclass, name, field, columnname, columnvalue);
        if (columnvalue > 0) {
            createChildList(em, dataAccess.find(columnname, columnvalue));
        } else {
            childList = new ArrayList<>();
        }
    }

    /**
     * Constructor.
     *
     * @param name the set name (for reporting)
     * @param field field identifier
     * @param emclass the associated entity manager class
     */
    public EntityReferenceSet(String name, F field, Class<? extends EntityManagerRO> emclass) {
        this(emclass, name, field, null, 0);
        createChildList(em, dataAccess.find());
    }

    private EntityReferenceSet(Class<? extends EntityManagerRO> emclass, String name, F field, String columnname, int columnvalue) {
        this.name = name;
        this.field = field;
        setChangeEvent = new Event<>(name + "/set");
        em = Lookup.getDefault().lookup(emclass);
        this.columnvalue = columnvalue;
        this.columnname = columnname;
        this.dataAccess = em.getDataAccess();
    }

    private void createChildList(EntityManagerRO<E> em, List<Integer> refs) {
        childList = new ArrayList<>();
        refs.stream().forEach((id) -> {
            childList.add(new EntityReference<>(name, id, em));
        });
    }

    /**
     * Add a listener to observe changes to set membership. The listener will
     * fire on the event queue.
     *
     * @param listener the listener
     */
    public final void addSetListener(Listener<SetChangeEventParams> listener) {
        setChangeEvent.addListener(listener);
    }

    /**
     * Add a listener to observe changes to set membership. The listener will
     * fire on the current thread or on the event queue / with priority or not,
     * depending on the flags set.
     *
     * @param listener the listener
     * @param mode the listener mode
     */
    public final void addSetListener(Listener<SetChangeEventParams> listener, ListenerMode mode) {
        setChangeEvent.addListener(listener, mode);
    }

    /**
     * Remove the set listener.
     *
     * @param listener the listener
     */
    public final void removeSetListener(Listener<SetChangeEventParams> listener) {
        setChangeEvent.removeListener(listener);
    }

    /**
     * Fire all set change listeners.
     */
    protected void fireSetChange() {
        setChangeEvent.fire(new SetChangeEventParams(field));
    }

    /**
     * Restore state (to the entity storage state).
     */
    public void restoreState() {
        // TODO - we are not removing any new entities which are not cached - so we might have a leak here.
        createChildList(em, columnname == null ? dataAccess.find() : dataAccess.find(columnname, columnvalue));
    }

    /**
     * Get the count of entities in the set.
     *
     * return the count of entities
     *
     * @return the count
     */
    public final int count() {
        return childList.size();
    }

    /**
     * Get the list of Entities
     *
     * @return the list of entities
     */
    public List<E> get() {
        List<E> el = new ArrayList<>();
        childList.stream().forEach((ref) -> {
            el.add(ref.get());
        });
        return el;
    }

    /**
     * Add a new entity to the entity set
     *
     * @param e the new entity
     */
    public void add(E e) {
        childList.add(new EntityReference<>(name, e, em));
        fireSetChange();
    }

    /**
     * Add a new entity to the entity set at a defined position.
     *
     * @param index the position to insert the new entity
     * @param e the new entity
     */
    protected final void add(int index, E e) {
        childList.add(index, new EntityReference<>(name, e, em));
        fireSetChange();
    }

    /**
     * Remove an entity from the child entity List
     *
     * @param e the Child Entity
     * @return true if entity removed
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

    /**
     * Get a new rule checking that set size does not exceed an upper limit.
     *
     * @param max the upper limit
     * @return the rule
     */
    public Rule getMaxRule(int max) {
        return new MaxRule(max);
    }

    /**
     * Get a new rule checking that the set size is not less than a lower limit.
     *
     * @param min the lower limit
     * @return the rule
     */
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
