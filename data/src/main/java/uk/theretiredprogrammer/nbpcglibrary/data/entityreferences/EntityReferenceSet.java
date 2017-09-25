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
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityManager;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.SetChangeEventParams;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import uk.theretiredprogrammer.nbpcglibrary.common.Event;
import uk.theretiredprogrammer.nbpcglibrary.common.Rule;
import org.openide.util.Lookup;
import uk.theretiredprogrammer.nbpcglibrary.common.Event.ListenerMode;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;

/**
 * Manages the list of Entities. The list of Entities is lazy loaded when
 * required.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the eEntity Class
 * @param <P> the parent Entity class
 */
public class EntityReferenceSet<E extends Entity, P extends CoreEntity> {

    /**
     * The Entity Manager associated with the entities
     */
    protected final EntityManager<E, P> em;
    /**
     * The list of Entity References
     */
    protected final List<EntityReference<E, P>> childList;

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
        setChangeEvent = new Event<>();
        em = Lookup.getDefault().lookup(emclass);
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
    protected List<Integer> getPrimaryKeySet() {
//        return epp.find();
        return null;
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
        int pk = e.getPK();
        for (EntityReference<E, P> ref : childList) {
            if (ref.getPK() == pk) {
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
