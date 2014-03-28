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
package linsdale.nbpcg.datasupportlib.entity;

import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.ListenerParams;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class EntityStateChangeListenerParams implements ListenerParams {
    
    public final static IntWithDescription INIT = new IntWithDescription(1, "Init");
    public final static IntWithDescription NEW = new IntWithDescription(2, "New");
    public final static IntWithDescription NEWEDITING = new IntWithDescription(3, "NewEditing");
    public final static IntWithDescription REMOVED = new IntWithDescription(4, "Removed");
    public final static IntWithDescription DBENTITY = new IntWithDescription(5, "DbEntity");
    public final static IntWithDescription DBENTITYEDITING = new IntWithDescription(6, "DbEntityEditing");
    public final static IntWithDescription DELETED = new IntWithDescription(7, "Deleted");
    // transitions within the state model
    public final static IntWithDescription CREATE = new IntWithDescription(1, "Create");
    public final static IntWithDescription LOAD = new IntWithDescription(2, "Load");
    public final static IntWithDescription EDIT = new IntWithDescription(3, "Edit");
    public final static IntWithDescription SAVE = new IntWithDescription(4, "Save");
    public final static IntWithDescription RESET = new IntWithDescription(5, "Reset");
    public final static IntWithDescription DELETE = new IntWithDescription(6, "Delete");
    public final static IntWithDescription REMOVE = new IntWithDescription(7, "Remove");
    //
    private final IntWithDescription transition;
    private final IntWithDescription oldState;
    private final IntWithDescription newState;
    
    public EntityStateChangeListenerParams(IntWithDescription transition, IntWithDescription oldState, IntWithDescription newState){
        this.transition = transition;
        this.oldState = oldState;
        this.newState = newState;
    }
    public IntWithDescription getTranition() {
        return transition;
    }
    
    public IntWithDescription getOldState() {
        return oldState;
    }
    
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
    public String toString(){
        return transition+"("+oldState+"->"+newState+")";
    }
    
}
