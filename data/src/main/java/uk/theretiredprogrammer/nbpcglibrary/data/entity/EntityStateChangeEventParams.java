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
package uk.theretiredprogrammer.nbpcglibrary.data.entity;


/**
 * The Parameter Class for a EntityStateChange listener.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class EntityStateChangeEventParams  {

    /**
     * Entity State
     */
    public enum EntityState {

        /**
         * Init. Entity created but not populated (initialisation state - entity
         * is never in this state)
         */
        INIT,
        /**
         * New. entity created and data initialised.
         */
        NEW,
        /**
         * NewEditing. new entity which has subsequently been modified.
         */
        NEWEDITING,
        /**
         * Removed. entity removed.
         */
        REMOVED,
        /**
         * DbEntity. entity is a copy of the entity in entity storage.
         */
        DBENTITY,
        /**
         * DbEntityEditiong. DBentity which has subsequently been modified.
         */
        DBENTITYEDITING,
    };

    /**
     * Entity State Changes
     */
    public enum EntityStateChange {

        /**
         * Create. entity created - will not be seen by listener as is fired in
         * constructor prior to any state listeners being able to be added.
         */
        CREATE,
        /**
         * Load. entity loaded with data.
         */
        LOAD,
        /**
         * Edit. entity has been edited
         */
        EDIT,
        /**
         * Save. entity has been saved.
         */
        SAVE,
        /**
         * Reset. entity has been reset.
         */
        RESET,
        /**
         * Remove. entity has been removed.
         */
        REMOVE
    };
    //
    private final EntityStateChange transition;
    private final EntityState oldState;
    private final EntityState newState;

    /**
     * Constructor.
     *
     * @param transition the State change
     * @param oldState the previous state
     * @param newState the new state
     */
    public EntityStateChangeEventParams(EntityStateChange transition, EntityState oldState, EntityState newState) {
        this.transition = transition;
        this.oldState = oldState;
        this.newState = newState;
    }

    /**
     * Get the transition
     *
     * @return the transition
     */
    public EntityStateChange getTransition() {
        return transition;
    }

    /**
     * Get the old state
     *
     * @return the old state
     */
    public EntityState getOldState() {
        return oldState;
    }

    /**
     * Get the new state.
     *
     * @return the new state
     */
    public EntityState getNewState() {
        return newState;
    }
}
