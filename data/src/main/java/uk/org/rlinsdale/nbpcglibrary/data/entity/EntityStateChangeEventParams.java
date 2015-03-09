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
package uk.org.rlinsdale.nbpcglibrary.data.entity;

import uk.org.rlinsdale.nbpcglibrary.common.EventParams;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * The Parameter Class for a EntityStateChange listener.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class EntityStateChangeEventParams implements EventParams {

    /**
     * Entity State
     */
    public enum EntityState {

        /**
         * Init. entity created but not populated (initialisation state - entity
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

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, transition + "[" + oldState + ">" + newState + "]");
    }
}
