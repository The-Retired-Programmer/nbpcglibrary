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
package linsdale.nbpcg.datasupportlib.entity;

import linsdale.nbpcg.datasupportlib.dataservice.ResultSetLoader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import linsdale.nbpcg.annotations.RegisterLog;
import linsdale.nbpcg.datasupportlib.dataaccess.DataAccessRO;
import linsdale.nbpcg.datasupportlib.dbfields.DBFieldsRO;
import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.Listener;
import linsdale.nbpcg.supportlib.Listening;
import linsdale.nbpcg.supportlib.Log;
import linsdale.nbpcg.supportlib.LogicException;

/**
 * The Basic Read-Only (uneditable) Entity Abstract Class.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
@RegisterLog("linsdale.nbpcg.datasupportlib")
public abstract class EntityRO extends Entity {

    private final Listening<EntityStateChangeListenerParams> stateListening;
    private final Listening<FieldChangeListenerParams> fieldListening;
    private IntWithDescription state = EntityStateChangeListenerParams.INIT;
    private int id;
    private final DBFieldsRO dbfields;
    private final DataAccessRO dataAccess;

    /**
     * Constructor.
     *
     * @param entityname the entity name
     * @param id the entity Id
     * @param em the entity manager for this entity class
     * @param dbfields the entity fields
     */
    public EntityRO(String entityname, int id, EntityManagerRO em, DBFieldsRO dbfields) {
        this(entityname, id, em.getDataAccess(), dbfields);
    }

    /**
     * Constructor.
     *
     * @param entityname the entity name
     * @param id the entity Id
     * @param dataAccess the Data Access object for this entity class
     * @param dbfields the entity fields
     */
    protected EntityRO(String entityname, int id, DataAccessRO dataAccess, DBFieldsRO dbfields) {
        super(entityname);
        this.id = id;
        this.dataAccess = dataAccess;
        this.dbfields = dbfields;
        stateListening = new Listening<>(entityname + "/state");
        fieldListening = new Listening<>(entityname + "/field");
        IntWithDescription oldState = state;
        state = EntityStateChangeListenerParams.NEW;
        fireStateChange(EntityStateChangeListenerParams.CREATE, oldState, state);
    }

    final void setState(IntWithDescription state) {
        this.state = state;
    }

    final IntWithDescription getState() {
        return state;
    }

    /**
     * Add a State Listener to this entity.
     *
     * @param listener the listener
     */
    public final void addStateListener(Listener<EntityStateChangeListenerParams> listener) {
        stateListening.addListener(listener);
    }

    /**
     * Remove a State listener from this entity.
     *
     * @param listener the listener
     */
    public final void removeStateListener(Listener<EntityStateChangeListenerParams> listener) {
        stateListening.removeListener(listener);
    }

    /**
     * Add a Field Listener to this entity.
     *
     * @param listener the listener
     */
    public final void addFieldListener(Listener<FieldChangeListenerParams> listener) {
        fieldListening.addListener(listener);
    }

    /**
     * Remove a Field Listener from this entity.
     *
     * @param listener the listener
     */
    public final void removeFieldListener(Listener<FieldChangeListenerParams> listener) {
        fieldListening.removeListener(listener);
    }

    /**
     * Fire actions on all field change listeners.
     *
     * @param field the field Id
     */
    protected final void fireFieldChange(IntWithDescription field) {
        fireFieldChange(field, true);
    }

    /**
     * Fire actions on all field change listeners.
     *
     * @param field the field Id
     * @param formatOK true if the field is formatted correctly
     */
    protected final void fireFieldChange(IntWithDescription field, boolean formatOK) {
        updateEntityRegistration();
        fieldListening.fire(new FieldChangeListenerParams(field, formatOK));
    }

    /**
     * Fire actions on all StateChange listeners.
     *
     * @param transition state transition Id
     * @param oldState the previous state
     * @param newState the new state
     */
    protected final void fireStateChange(IntWithDescription transition, IntWithDescription oldState, IntWithDescription newState) {
        stateListening.fire(new EntityStateChangeListenerParams(transition, oldState, newState));
    }

    /**
     * Fire actions on Field Change listeners at load.
     *
     * @param field the field Id
     */
    protected final void fireFieldChangeAtLoad(IntWithDescription field) {
        updateEntityRegistrationAtLoad();
        fieldListening.fire(new FieldChangeListenerParams(field, true));
    }

    /**
     * Get the entity Id.
     *
     * @return the entity id
     */
    public final int getId() {
        return id;
    }

    final void setId(int id) {
        this.id = id;
    }

    /**
     * Test if state is New.
     *
     * @return true if new
     */
    public final boolean isNew() {
        return state == EntityStateChangeListenerParams.NEW || state == EntityStateChangeListenerParams.NEWEDITING;
    }

    /**
     * Test if state is Editing (newediting or dbentityediting).
     *
     * @return true if editing
     */
    public final boolean isEditing() {
        return state == EntityStateChangeListenerParams.NEWEDITING || state == EntityStateChangeListenerParams.DBENTITYEDITING;
    }

    /**
     * Switch into an editing state, updating state and firing the necessary
     * statechange listeners. If already in state then this does nothing.
     */
    protected final void ensureEditing() {
        if (state == EntityStateChangeListenerParams.NEWEDITING || state == EntityStateChangeListenerParams.DBENTITYEDITING) {
            return;
        }
        IntWithDescription oldState = state;
        if (state == EntityStateChangeListenerParams.NEW) {
            _saveState();
            dbfields.saveState();
            state = EntityStateChangeListenerParams.NEWEDITING;
            fireStateChange(EntityStateChangeListenerParams.EDIT, oldState, state);
            return;
        }
        if (state == EntityStateChangeListenerParams.DBENTITY) {
            _saveState();
            dbfields.saveState();
            state = EntityStateChangeListenerParams.DBENTITYEDITING;
            fireStateChange(EntityStateChangeListenerParams.EDIT, oldState, state);
            return;
        }
        throw new LogicException("Should not be trying to edit an entity in " + state + " state");
    }

    /**
     * Locally save the state of this entity (so that entity can be
     * reset/cancelled).
     */
    abstract protected void _saveState();

    @Override
    public final void cancelEdit() {
        IntWithDescription oldState = state;
        boolean wasEditing = false;
        if (state == EntityStateChangeListenerParams.NEWEDITING) {
            state = EntityStateChangeListenerParams.NEW;
            wasEditing = true;
        }
        if (state == EntityStateChangeListenerParams.DBENTITYEDITING) {
            state = EntityStateChangeListenerParams.DBENTITY;
            wasEditing = true;
        }
        if (wasEditing) {
            _restoreState();
            dbfields.restoreState();
            fireFieldChange(FieldChangeListenerParams.ALLFIELDS);
            fireStateChange(EntityStateChangeListenerParams.RESET, oldState, state);
        }
    }

    /**
     * Load Data from entity storage into this entity and fire the field change
     * at load listeners.
     *
     * @param id the entity Id
     */
    protected void load(int id) {
        dataAccess.load(id, new EntityROLoader());
        fireFieldChangeAtLoad(FieldChangeListenerParams.ALLFIELDS);
    }

    private class EntityROLoader implements ResultSetLoader {

        @Override
        public void load(ResultSet rs) {
            IntWithDescription oldState = getState();
            if (oldState == EntityStateChangeListenerParams.NEW) {
                try {
                    setId(rs.getInt("id"));
                    dbfields.load(rs);
                    _load(rs);
                } catch (SQLException ex) {
                    Log.get("linsdale.nbpcg.datasupportlib").log(Level.SEVERE, null, ex);
                }
                setState(EntityStateChangeListenerParams.DBENTITY);
                fireStateChange(EntityStateChangeListenerParams.LOAD, oldState, EntityStateChangeListenerParams.DBENTITY);
                return;
            }
            throw new LogicException("Should not be trying to load an entity in " + oldState + " state");
        }
    }

    /**
     * Load a resultset into the entity fields.
     *
     * @param rs the resultset
     * @throws SQLException if problems
     */
    abstract protected void _load(ResultSet rs) throws SQLException;
}
