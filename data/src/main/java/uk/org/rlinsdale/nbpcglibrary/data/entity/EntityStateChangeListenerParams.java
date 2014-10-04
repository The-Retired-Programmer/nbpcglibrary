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
package uk.org.rlinsdale.nbpcglibrary.data.entity;

import uk.org.rlinsdale.nbpcglibrary.common.IntWithDescription;
import uk.org.rlinsdale.nbpcglibrary.common.ListenerParams;

/**
 * The Parameter Class for a EntityStateChange listener.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class EntityStateChangeListenerParams implements ListenerParams {

    /**
     * State Id - Init. entity created but not populated.
     */
    public final static IntWithDescription INIT = new IntWithDescription(1, "Init");

    /**
     * State Id - New. entity created and data initialised.
     */
    public final static IntWithDescription NEW = new IntWithDescription(2, "New");

    /**
     * State Id - NewEditing. new entity which has subsequently been modified.
     */
    public final static IntWithDescription NEWEDITING = new IntWithDescription(3, "NewEditing");

    /**
     * State Id - Removed. entity removed.
     */
    public final static IntWithDescription REMOVED = new IntWithDescription(4, "Removed");

    /**
     * State Id - DbEntity. entity is a copy of the entity in entity storage.
     */
    public final static IntWithDescription DBENTITY = new IntWithDescription(5, "DbEntity");

    /**
     * State Id - DbEntityEditiong. DBentity which has subsequently been
     * modified.
     */
    public final static IntWithDescription DBENTITYEDITING = new IntWithDescription(6, "DbEntityEditing");

    /**
     * State Id - Deleted. entity deleted from entity storage.
     */
    public final static IntWithDescription DELETED = new IntWithDescription(7, "Deleted");
    // transitions within the state model

    /**
     * State Change Id - Create. entity created
     */
    public final static IntWithDescription CREATE = new IntWithDescription(1, "Create");

    /**
     * State change Id - Load. entity loaded with data.
     */
    public final static IntWithDescription LOAD = new IntWithDescription(2, "Load");

    /**
     * State change Id - Edit. entity has been edited
     */
    public final static IntWithDescription EDIT = new IntWithDescription(3, "Edit");

    /**
     * State change Id - Save. entity has been saved.
     */
    public final static IntWithDescription SAVE = new IntWithDescription(4, "Save");

    /**
     * State change Id - Reset. entity has been reset.
     */
    public final static IntWithDescription RESET = new IntWithDescription(5, "Reset");

    /**
     * State change Id - Delete. entity has been deleted.
     */
    public final static IntWithDescription DELETE = new IntWithDescription(6, "Delete");

    /**
     * State change Id - Remove. entity has been removed.
     */
    public final static IntWithDescription REMOVE = new IntWithDescription(7, "Remove");
    //
    private final IntWithDescription transition;
    private final IntWithDescription oldState;
    private final IntWithDescription newState;

    /**
     * Constructor.
     *
     * @param transition the State change Id
     * @param oldState the previous state Id
     * @param newState the new state Id
     */
    public EntityStateChangeListenerParams(IntWithDescription transition, IntWithDescription oldState, IntWithDescription newState) {
        this.transition = transition;
        this.oldState = oldState;
        this.newState = newState;
    }

    /**
     * Get the transition Id.
     *
     * @return the transition Id
     */
    public IntWithDescription getTransition() {
        return transition;
    }

    /**
     * Get the old state Id.
     *
     * @return the old state Id
     */
    public IntWithDescription getOldState() {
        return oldState;
    }

    /**
     * Get the new state Id.
     *
     * @return the new state Id
     */
    public IntWithDescription getNewState() {
        return newState;
    }

    @Override
    public int hashCode() {
        return transition.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof EntityStateChangeListenerParams) {
            return this.transition == ((EntityStateChangeListenerParams) obj).transition;
        }
        if (obj instanceof IntWithDescription) {
            return this.transition == (IntWithDescription) obj;
        }
        return false;
    }

    @Override
    public String toString() {
        return transition + "(" + oldState + "->" + newState + ")";
    }
}
