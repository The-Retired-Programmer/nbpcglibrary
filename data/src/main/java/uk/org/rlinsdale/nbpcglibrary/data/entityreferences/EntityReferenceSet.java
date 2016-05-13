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
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProvider;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManager;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.SetChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.Event;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;
import org.openide.util.Lookup;
import uk.org.rlinsdale.nbpcglibrary.common.Event.ListenerMode;
import uk.org.rlinsdale.nbpcglibrary.data.entity.CoreEntity;

/**
 * Manages the list of Entities. The list of Entities is lazy loaded when
 * required.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> the primary Key class
 * @param <E> the eEntity Class
 * @param <P> the parent Entity class
 */
public class EntityReferenceSet<K, E extends Entity<K,E,P,?>, P extends CoreEntity> {

    /**
     * The Entity Manager associated with the entities
     */
    protected final EntityManager<K, E, P> em;
    /**
     * The Entity Persistence Provider for this entity set
     */
    protected final EntityPersistenceProvider<K> epp;
    /**
     * The list of Entity References
     */
    protected final List<EntityReference<K, E, P>> childList;

    /**
     * The name of the Set (for reporting purposes)
     */
    protected final String name;
    private final Event<SetChangeEventParams> setChangeEvent;

    /**
     * Constructor.
     *
     * @param name the set name (for reporting)
     * @param emclass the associated entity manager class
     */
    public EntityReferenceSet(String name,  Class<? extends EntityManager> emclass) {
        this.name = name;
        setChangeEvent = new Event<>(name);
        em = Lookup.getDefault().lookup(emclass);
        this.epp = em.getEntityPersistenceProvider();
        childList = new ArrayList<>();
    }
    
    /**
     * Load the set with all required entity references.
     */
    public void load(){
        getPrimaryKeySet().stream().forEach((ref) -> {
            childList.add(new EntityReference<>(name, ref, em));
        });
    }
    
    /**
     * Get the set of primary keys for this entity set.
     * @return  the set of primary keys
     */
    protected List<K> getPrimaryKeySet() {
        return epp.find();
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
        setChangeEvent.fire(new SetChangeEventParams());
    }

    /**
     * Restore state (to the entity storage state).
     */
    public void restoreState() {
        childList.clear();
        load();
        fireSetChange();
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
    public void add(E e)  {
        childList.add(new EntityReference<>(name, e, em));
        fireSetChange();
    }

    /**
     * Add a new entity to the entity set at a defined position.
     *
     * @param index the position to insert the new entity
     * @param e the new entity
     */
    protected final void add(int index, E e)  {
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
        K pk = e.getPK();
        for (EntityReference<K,E, P> ref : childList) {
            if (ref.getPK().equals(pk)) {
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
            super("Too many defined");
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
            super("Too few defined");
            this.min = min;
        }

        @Override
        public boolean ruleCheck() {
            return count() >= min;
        }
    }
}
