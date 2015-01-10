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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterLog;
import uk.org.rlinsdale.nbpcglibrary.common.Event;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;
import uk.org.rlinsdale.nbpcglibrary.data.dataaccess.DataAccessRO;
import uk.org.rlinsdale.nbpcglibrary.data.dataservice.ResultSetLoader;
import uk.org.rlinsdale.nbpcglibrary.data.dbfields.DBFieldsRO;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.DBENTITY;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.DBENTITYEDITING;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.INIT;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.NEW;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.NEWEDITING;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.CREATE;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.EDIT;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.LOAD;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.RESET;
import uk.org.rlinsdale.nbpcglibrary.data.entity.FieldChangeEventParams.CommonEntityField;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.FieldChangeEventParams.CommonEntityField.ALL;

/**
 * The Basic Read-Only (uneditable) Entity Abstract Class.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <F> the Fields enum class
 */
@RegisterLog("nbpcglibrary.data")
public abstract class EntityRO<F> extends Entity {
    
    private final Event<EntityStateChangeEventParams> stateEvent;
    private final Event<FieldChangeEventParams<F>> fieldEvent;
    private EntityState state = INIT;
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
        stateEvent = new Event<>(entityname + "/state");
        fieldEvent = new Event<>(entityname + "/field");
        EntityState oldState = state;
        state = NEW;
        fireStateChange(CREATE, oldState, state);
    }

    final void setState(EntityState state) {
        this.state = state;
    }

    final EntityState getState() {
        return state;
    }

    /**
     * Add a State Listener to this entity.
     *
     * @param listener the listener
     */
    public final void addStateListener(Listener<EntityStateChangeEventParams> listener) {
        stateEvent.addListener(listener);
    }

    /**
     * Remove a State listener from this entity.
     *
     * @param listener the listener
     */
    public final void removeStateListener(Listener<EntityStateChangeEventParams> listener) {
        stateEvent.removeListener(listener);
    }

    /**
     * Add a Field Listener to this entity.
     *
     * @param listener the listener
     */
    public final void addFieldListener(Listener<FieldChangeEventParams<F>> listener) {
        fieldEvent.addListener(listener);
    }

    /**
     * Remove a Field Listener from this entity.
     *
     * @param listener the listener
     */
    public final void removeFieldListener(Listener<FieldChangeEventParams<F>> listener) {
        fieldEvent.removeListener(listener);
    }

    /**
     * Fire actions on all field change listeners.
     *
     * @param field the field Id
     */
    protected final void fireFieldChange(F field) {
        fireFieldChange(field, true);
    }
    
    /**
     * Fire actions on all field change listeners.
     *
     * @param field the field Id
     */
    protected final void fireFieldChange(CommonEntityField field) {
        fireCommonFieldChange(field, true);
    }

    /**
     * Fire actions on all field change listeners.
     *
     * @param field the field Id
     * @param formatOK true if the field is formatted correctly
     */
    protected final void fireFieldChange(F field, boolean formatOK) {
        updateEntityRegistration();
        fieldEvent.fire(new FieldChangeEventParams<>(field, null, formatOK));
    }
    
    /**
     * Fire actions on all field change listeners.
     *
     * @param field the field Id
     * @param formatOK true if the field is formatted correctly
     */
    protected final void fireCommonFieldChange(CommonEntityField field, boolean formatOK) {
        updateEntityRegistration();
        fieldEvent.fire(new FieldChangeEventParams<>(null, field, formatOK));
    }

    /**
     * Fire actions on all StateChange listeners.
     *
     * @param transition state transition Id
     * @param oldState the previous state
     * @param newState the new state
     */
    protected final void fireStateChange(EntityStateChange transition, EntityState oldState, EntityState newState) {
        stateEvent.fire(new EntityStateChangeEventParams(transition, oldState, newState));
    }

    /**
     * Fire actions on Field Change listeners at load.
     *
     * @param field the field Id
     */
    protected final void fireFieldChangeAtLoad(CommonEntityField field) {
        updateEntityRegistrationAtLoad();
        fieldEvent.fire(new FieldChangeEventParams<>(null, field, true));
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
        return state == NEW || state == NEWEDITING;
    }

    /**
     * Test if state is Editing (newediting or dbentityediting).
     *
     * @return true if editing
     */
    public final boolean isEditing() {
        return state == NEWEDITING || state == DBENTITYEDITING;
    }

    /**
     * Switch into an editing state, updating state and firing the necessary
     * statechange listeners. If already in state then this does nothing.
     */
    protected final void ensureEditing() {
        EntityState oldState = state;
        switch (state) {
            case NEWEDITING:
            case DBENTITYEDITING:
                return;
            case NEW:
                _saveState();
                dbfields.saveState();
                state = NEWEDITING;
                fireStateChange(EDIT, oldState, state);
                return;
            case DBENTITY:
                _saveState();
                dbfields.saveState();
                state = DBENTITYEDITING;
                fireStateChange(EDIT, oldState, state);
                return;
            default:
                throw new LogicException("Should not be trying to edit an entity in " + state + " state");
        }
    }

    /**
     * Locally save the state of this entity (so that entity can be
     * reset/cancelled).
     */
    abstract protected void _saveState();

    @Override
    public final void cancelEdit() {
        EntityState oldState = state;
        boolean wasEditing = false;
        switch (state) {
            case NEWEDITING:
                state = NEW;
                wasEditing = true;
                break;
            case DBENTITYEDITING:
                state = DBENTITY;
                wasEditing = true;
                break;
        }
        if (wasEditing) {
            _restoreState();
            dbfields.restoreState();
            fireFieldChange(ALL);
            fireStateChange(RESET, oldState, state);
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
        fireFieldChangeAtLoad(ALL);
    }

    private class EntityROLoader implements ResultSetLoader {

        @Override
        public void load(ResultSet rs) {
            EntityState oldState = getState();
            if (oldState == NEW) {
                try {
                    setId(rs.getInt("id"));
                    dbfields.load(rs);
                    _load(rs);
                } catch (SQLException ex) {
                    LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName("EntityROLoader", "load", rs)
                            .addException(ex).write();
                }
                setState(DBENTITY);
                fireStateChange(LOAD, oldState, DBENTITY);
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
