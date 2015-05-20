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

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.spi.actions.AbstractSavable;
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterLog;
import uk.org.rlinsdale.nbpcglibrary.common.Event;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;
import uk.org.rlinsdale.nbpcglibrary.common.SimpleEventParams;
import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProvider;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;
import uk.org.rlinsdale.nbpcglibrary.data.LibraryOnStop;
import uk.org.rlinsdale.nbpcglibrary.data.dbfields.DBFields;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.*;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams.CommonEntityField;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams.CommonEntityField.ALL;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams.CommonEntityField.ID;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.CREATE;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.EDIT;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.LOAD;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.REMOVE;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.RESET;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.SAVE;
import uk.org.rlinsdale.nbpcglibrary.data.entityreferences.IdChangeEventParams;
import uk.org.rlinsdale.nbpcglibrary.json.JsonConversionException;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 * The Entity Abstract Class.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the entity class
 * @param <P> the Parent Entity Class
 * @param <F> the Fields enum class
 */
@RegisterLog("nbpcglibrary.data")
public abstract class Entity<E extends Entity, P extends CoreEntity, F> extends CoreEntity {

    private final Event<EntityStateChangeEventParams> stateEvent;
    private final Event<EntityFieldChangeEventParams<F>> fieldEvent;
    private Event<SimpleEventParams> titleChangeEvent;
    private Event<SimpleEventParams> nameChangeEvent;
    private EntityState state = INIT;
    private int id;
    private final DBFields dbfields;
    private final EntityPersistenceProvider dataAccess;
    private final String entityname;

    private final EntityPersistenceProvider entityPersistenceManager;
    private final Event<IdChangeEventParams> idChangeEvent;
    private final EntityManager<E, P> em;
    private final EntityStateChangeListener entitystatechangelistener;
    private final EntitySavable savable = new EntitySavable();

    /**
     * Constructor.
     *
     * @param entityname the entity name
     * @param icon name of the icon graphic
     * @param id the entity Id
     * @param em the entity manager for this entity class
     * @param dbfields the entity fields
     */
    public Entity(String entityname, String icon, int id, EntityManager<E, P> em, DBFields dbfields) {
        this(entityname, icon, id, em, em.getEntityPersistenceProvider(), dbfields);
    }

    /**
     * Constructor.
     *
     * @param entityname the entity name
     * @param icon name of the icon graphic
     * @param id the entity Id
     * @param em the entity manager for this entity class
     * @param dataAccess the Data Access object for this entity class
     * @param dbfields the entity fields
     */
    @SuppressWarnings("LeakingThisInConstructor")
    protected Entity(String entityname, String icon, int id, EntityManager<E, P> em, EntityPersistenceProvider dataAccess, DBFields dbfields) {
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
        //
        this.em = em;
        this.entityPersistenceManager = dataAccess;
        idChangeEvent = new Event<>(entityname);
        entitystatechangelistener = new EntityStateChangeListener("entity:" + instanceDescription());
        addStateListener(entitystatechangelistener);
        // and as this is new it is saveable (too early to rely on listener)
        savable.add();
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
     * Fire Listeners if name changes.
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
     * Fire Listeners if title changes.
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
                entitySaveState();
                dbfields.saveState();
                state = NEWEDITING;
                fireStateChange(EDIT, oldState, state);
                return;
            case DBENTITY:
                entitySaveState();
                dbfields.saveState();
                state = DBENTITYEDITING;
                fireStateChange(EDIT, oldState, state);
                return;
            default:
                throw new LogicException("Should not be trying to edit an entity in " + state + " state");
        }
    }

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
            entityRestoreState();
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
                entityLoad(data);
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
     * Add an Id Listener to this entity.
     *
     * @param listener the listener
     */
    public final void addIdListener(Listener<IdChangeEventParams> listener) {
        idChangeEvent.addListener(listener);
    }

    /**
     * Remove an Id listener from this entity.
     *
     * @param listener the listener
     */
    public final void removeIdListener(Listener<IdChangeEventParams> listener) {
        idChangeEvent.removeListener(listener);
    }

    /**
     * Add an Id Listener to this entity.
     *
     * @param listener the listener
     * @param mode the indicators of listener action (on current thread or on
     * event queue; priority/normal)
     */
    public final void addIdListener(Listener<IdChangeEventParams> listener, Event.ListenerMode mode) {
        idChangeEvent.addListener(listener, mode);
    }

    final void updateId(int id) {
        setId(id);
        fireFieldChange(ID);
    }

    /**
     * Save this entity to entity storage.
     *
     * @return true if save is successful
     */
    public boolean save() {
        try {
            JsonObjectBuilder job = Json.createObjectBuilder();
            EntityState oldState = getState();
            switch (oldState) {
                case DBENTITY:
                    return true; //we don't need to do anything as this is a straight copy of a db entity
                case REMOVED:
                    return false;
                case NEW:
                case NEWEDITING:
                    if (!checkRules()) {
                        return false;
                    }
                    entityValues(job);
                    dbfields.values(job);
                    em.persistTransient((E) this, entityPersistenceManager.insert(job.build()));
                    idChangeEvent.fire(new IdChangeEventParams());
                    setState(DBENTITY);
                    break;
                case DBENTITYEDITING:
                    if (!checkRules()) {
                        return false;
                    }
                    entityDiffs(job);
                    dbfields.diffs(job);
                    JsonObject jo = job.build();
                    if (!jo.isEmpty()) {
                        entityPersistenceManager.update(getId(), jo);
                    }
                    setState(DBENTITY);
                    break;
                default:
                    if (!checkRules()) {
                        return false;
                    }
            }
            fireStateChange(SAVE, oldState, DBENTITY);
            return true;
        } catch (IOException | LogicException ex) {
            return false;
        }
    }

    /**
     * Delete this Entity.
     *
     * @throws IOException if problem obtaining / parsing data
     */
    public final void remove() throws IOException {
        EntityState oldState = getState();
        switch (oldState) {
            case NEW:
            case NEWEDITING:
                entityRemove();
                em.removeFromTransientCache((E) this);
                setState(REMOVED);
                fireStateChange(REMOVE, oldState, REMOVED);
                return;
            case DBENTITY:
            case DBENTITYEDITING:
                entityRemove();
                entityPersistenceManager.delete(getId());
                em.removeFromCache((E) this);
                setState(REMOVED);
                fireStateChange(REMOVE, oldState, REMOVED);
                return;
            default:
                throw new LogicException("Should not be trying to remove an entity in " + oldState + " state");
        }
    }

    /**
     * Copy entity fields into this entity.
     *
     * @param e the copy source entity
     * @throws IOException if entity in illegal state.
     */
    public final void copy(E e) throws IOException {
        EntityState oldState = getState();
        if (oldState == NEW || oldState == NEWEDITING) {
            entityCopy(e);
            dbfields.copy(e);
            ensureEditing();
            fireFieldChange(ALL);
            return;
        }
        throw new IOException("Should not be trying to copy an entity in " + oldState + " state");
    }

    private class EntityStateChangeListener extends Listener<EntityStateChangeEventParams> {

        public EntityStateChangeListener(String name) {
            super(name);
        }

        @Override
        public void action(EntityStateChangeEventParams p) {
            switch (p.getTransition()) {
                case EDIT:
                    savable.add();
                    break;
                case LOAD:
                    savable.remove();
                    break;
                case SAVE:
                    break;
                case REMOVE:
                    savable.remove();
                    break;
                case RESET:
                    savable.remove();
            }
        }
    }

    @Override
    public Image getIcon() {
        return checkRules() ? super.getIcon() : getIconWithError();
    }

    private class EntitySavable<E, P, F> extends AbstractSavable implements Icon, HasInstanceDescription {

        private Icon icon;

        public void add() {
            if (getLookup().lookup(EntitySavable.class) == null) {
                register();
                addLookupContent(this);
            }
            icon = null;
        }

        public void remove() {
            removeLookupContent(this);
            unregister();
        }

        @Override
        public String instanceDescription() {
            return LogBuilder.instanceDescription(this, entityname);
        }

        @Override
        protected void handleSave() throws IOException {
            LogBuilder.writeLog("nbpcglibrary.data", this, "handleSave");
            if (Entity.this.save()) {
                removeLookupContent(this);
            } else {
                EventQueue.invokeLater(new ReRegister());
                LibraryOnStop.incRegisterOutstanding();
            }
        }

        private class ReRegister implements Runnable {

            @Override
            public void run() {
                EntitySavable.this.register();
                LibraryOnStop.decRegisterOutstanding();
            }
        }

        @Override
        protected String findDisplayName() {
            return getDisplayTitle();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof EntitySavable) {
                return entity() == ((EntitySavable) obj).entity();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return entity().hashCode();
        }

        Entity entity() {
            return Entity.this;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            new ImageIcon(getIcon()).paintIcon(c, g, x, y);
        }

        @Override
        public int getIconWidth() {
            if (icon == null) {
                icon = new ImageIcon(getIcon());
            }
            return icon.getIconWidth();
        }

        @Override
        public int getIconHeight() {
            if (icon == null) {
                icon = new ImageIcon(getIcon());
            }
            return icon.getIconHeight();
        }
    }

    /**
     * Locally save the state of this entity (so that entity can be
     * reset/cancelled).
     */
    abstract protected void entitySaveState();

    /**
     * Load Json format data into the entity fields.
     *
     * @param data the data to be inserted
     * @throws IOException if bad data provided
     */
    abstract protected void entityLoad(JsonObject data) throws IOException;

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

    /**
     * Add all field values to the given JsonObject.
     *
     * @param job a JsonObjectBuilder into which field names (keys) and field
     * values are to be inserted.
     * @throws IOException if problem obtaining / parsing data
     */
    abstract protected void entityValues(JsonObjectBuilder job) throws IOException;

    /**
     * Add any modified field values to the given JsonObject.
     *
     * @param job a JsonObjectBuilder into which field names (keys) and field
     * values are to be inserted.
     * @throws IOException if problem obtaining / parsing data
     */
    abstract protected void entityDiffs(JsonObjectBuilder job) throws IOException;

    /**
     * Complete any entity specific removal actions prior to entity deletion.
     * Basic use case: remove any linkage to parent entities.
     *
     * @throws IOException if problem occurs while completing this action
     */
    abstract protected void entityRemove() throws IOException;

    /**
     * Field Copy actions - copy entity fields into this entity.
     *
     * @param from the copy source entity
     */
    abstract protected void entityCopy(E from);
}
