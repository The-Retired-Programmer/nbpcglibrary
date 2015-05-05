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

import java.io.IOException;
import java.util.logging.Level;
import javax.json.JsonObject;
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterLog;
import uk.org.rlinsdale.nbpcglibrary.common.Event;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;
import uk.org.rlinsdale.nbpcglibrary.common.SimpleEventParams;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceManager;
import uk.org.rlinsdale.nbpcglibrary.data.dbfields.DBFieldsRO;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.*;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams.CommonEntityField;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams.CommonEntityField.ALL;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.CREATE;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.EDIT;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.LOAD;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.RESET;
import uk.org.rlinsdale.nbpcglibrary.json.JsonConversionException;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 * The Basic Read-Only (uneditable) Entity Abstract Class.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <F> the Fields enum class
 */
@RegisterLog("nbpcglibrary.data")
public abstract class EntityRO<F> extends Entity {

    private final Event<EntityStateChangeEventParams> stateEvent;
    private final Event<EntityFieldChangeEventParams<F>> fieldEvent;
    private Event<SimpleEventParams> titleChangeEvent;
    private Event<SimpleEventParams> nameChangeEvent;
    private EntityState state = INIT;
    private int id;
    private final DBFieldsRO dbfields;
    private final EntityPersistenceManager dataAccess;
    private final String entityname;

    /**
     * Constructor.
     *
     * @param entityname the entity name
     * @param icon name of the icon graphic
     * @param id the entity Id
     * @param em the entity manager for this entity class
     * @param dbfields the entity fields
     */
    public EntityRO(String entityname, String icon, int id, EntityManagerRO em, DBFieldsRO dbfields) {
        this(entityname, icon, id, em.getEntityPersistenceManager(), dbfields);
    }

    /**
     * Constructor.
     *
     * @param entityname the entity name
     * @param icon name of the icon graphic
     * @param id the entity Id
     * @param dataAccess the Data Access object for this entity class
     * @param dbfields the entity fields
     */
    @SuppressWarnings("LeakingThisInConstructor")
    protected EntityRO(String entityname, String icon, int id, EntityPersistenceManager dataAccess, DBFieldsRO dbfields) {
        super(entityname, icon);
        this.id = id;
        this.dataAccess = dataAccess;
        this.dbfields = dbfields;
        this.entityname = entityname;
        String name = LogBuilder.instanceDescription(this, Integer.toString(id));
        stateEvent = new Event<>("statechange:" + name);
        fieldEvent = new Event<>("fieldchange:" + name);
        nameChangeEvent = new Event<>("namechange:" + name);
        titleChangeEvent = new Event<>("titlechange:" + name);
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
    public final void addFieldListener(Listener<EntityFieldChangeEventParams<F>> listener) {
        fieldEvent.addListener(listener);
    }

    /**
     * Remove a Field Listener from this entity.
     *
     * @param listener the listener
     */
    public final void removeFieldListener(Listener<EntityFieldChangeEventParams<F>> listener) {
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
        fieldEvent.fire(new EntityFieldChangeEventParams<>(field, null, formatOK));
    }

    /**
     * Fire actions on all field change listeners.
     *
     * @param field the field Id
     * @param formatOK true if the field is formatted correctly
     */
    protected final void fireCommonFieldChange(CommonEntityField field, boolean formatOK) {
        fieldEvent.fire(new EntityFieldChangeEventParams<>(null, field, formatOK));
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
        fieldEvent.fire(new EntityFieldChangeEventParams<>(null, field, true));
    }

    /**
     * Add a Name listener.
     *
     * @param listener the listener
     */
    public final void addNameListener(Listener<SimpleEventParams> listener) {
        nameChangeEvent.addListener(listener);
    }

    /**
     * Remove a Name listener.
     *
     * @param listener the listener
     */
    public final void removeNameListener(Listener<SimpleEventParams> listener) {
        nameChangeEvent.removeListener(listener);
    }

    /**
     *
     */
    protected void nameListenerFire() {
        nameChangeEvent.fire(new SimpleEventParams());
    }

    /**
     * Add a Title listener.
     *
     * @param listener the listener
     */
    public final void addTitleListener(Listener<SimpleEventParams> listener) {
        titleChangeEvent.addListener(listener);
    }

    /**
     * Remove a Title listener.
     *
     * @param listener the listener
     */
    public final void removeTitleListener(Listener<SimpleEventParams> listener) {
        titleChangeEvent.removeListener(listener);
    }

    /**
     *
     */
    protected void titleListenerFire() {
        titleChangeEvent.fire(new SimpleEventParams());
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
    public final void cancelEdit() throws IOException {
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
            nameListenerFire();
            titleListenerFire();
        }
    }

    /**
     * Load Data from entity storage into this entity and fire the field change
     * at load listeners.
     *
     * @param id the entity Id
     */
    protected void load(int id) {
        try {
            loader(dataAccess.get(id));
        } catch (IOException ex) {
            LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName(this, "load", id)
                    .addExceptionMessage(ex).write();
        }
        fireFieldChangeAtLoad(ALL);
    }

    private void loader(JsonObject data) throws IOException {
        LogBuilder.writeLog("nbpcglibrary.data", this, "loader", data.toString());
        EntityState oldState = getState();
        if (oldState == NEW) {
            try {
                setId(JsonUtil.getObjectKeyIntegerValue(data, "id"));
                dbfields.load(data);
                _load(data);
            } catch (JsonConversionException ex) {
                LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName(this, "load", data.toString())
                        .addExceptionMessage(ex).write();
            }
            setState(DBENTITY);
            fireStateChange(LOAD, oldState, DBENTITY);
            return;
        }
        throw new IOException("Should not be trying to load an entity in " + oldState + " state");
    }

    /**
     * Load Json format data into the entity fields.
     *
     * @param data the data to be inserted
     * @throws IOException if bad data provided
     */
    abstract protected void _load(JsonObject data) throws IOException;

    /**
     * get the key string which will be used in when sorting this entity
     *
     * @return the sort key
     */
    public abstract String getSortKey();

    /**
     * get the title string which will be used to display the fully in context
     * name for the entity
     *
     * @return the title string
     */
    public abstract String getDisplayTitle();
}
