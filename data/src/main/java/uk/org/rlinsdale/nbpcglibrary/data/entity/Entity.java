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
import uk.org.rlinsdale.nbpcglibrary.api.EntityFields;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;
import uk.org.rlinsdale.nbpcglibrary.data.LibraryOnStop;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.*;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams.CommonEntityField;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams.CommonEntityField.ALL;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.CREATE;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.EDIT;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.LOAD;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.REMOVE;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.RESET;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.SAVE;
import uk.org.rlinsdale.nbpcglibrary.data.entityreferences.PrimaryKeyChangeEventParams;

/**
 * The Entity Abstract Class.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> th etype of the primary Key
 * @param <E> the entity class
 * @param <P> the Parent Entity Class
 * @param <F> the entity field types
 */
@RegisterLog("nbpcglibrary.data")
public abstract class Entity<K, E extends Entity<K, E, P, F>, P extends CoreEntity, F> extends CoreEntity {

    private final Event<EntityStateChangeEventParams> stateEvent;
    private final Event<EntityFieldChangeEventParams<F>> fieldEvent;
    private Event<SimpleEventParams> titleChangeEvent;
    private Event<SimpleEventParams> nameChangeEvent;
    private EntityState state = INIT;
    private final String entityname;
    private final EntityPersistenceProvider<K> epp;
    private final Event<PrimaryKeyChangeEventParams> primaryKeyChangeEvent;
    private final EntityManager<K, E, P> em;
    private final EntityStateChangeListener entitystatechangelistener;
    private final EntitySavable savable = new EntitySavable();
    private boolean persistent = false;

    /**
     * Constructor.
     *
     * @param entityname the entity name
     * @param icon name of the icon graphic
     * @param em the entity manager for this entity class
     */
    public Entity(String entityname, String icon, EntityManager<K, E, P> em) {
        this(entityname, icon, em, em.getEntityPersistenceProvider());
    }

    /**
     * Constructor.
     *
     * @param entityname the entity name
     * @param icon name of the icon graphic
     * @param em the entity manager for this entity class
     * @param epp the Data Access object for this entity class
     */
    protected Entity(String entityname, String icon, EntityManager<K, E, P> em, EntityPersistenceProvider<K> epp) {
        super(entityname, icon);
        this.epp = epp;
        this.entityname = entityname;
        @SuppressWarnings({"OverridableMethodCallInConstructor", "LeakingThisInConstructor"})
        String name = LogBuilder.instanceDescription(this, getPK().toString());
        stateEvent = new Event<>("statechange:" + name);
        fieldEvent = new Event<>("fieldchange:" + name);
        nameChangeEvent = new Event<>("namechange:" + name);
        titleChangeEvent = new Event<>("titlechange:" + name);
        EntityState oldState = state;
        state = NEW;
        fireStateChange(CREATE, oldState, state);
        //
        this.em = em;
        primaryKeyChangeEvent = new Event<>(entityname);
        entitystatechangelistener = new EntityStateChangeListener("entity:" + instanceDescription());
        addStateListener(entitystatechangelistener);
        // and as this is new it is saveable (too early to rely on listener)
        savable.add();
    }

    /**
     * Test persistent status
     *
     * @return true if entity has been persisted.
     */
    public boolean isPersistent() {
        return persistent;
    }

    /**
     * Get the entity primary key.
     *
     * @return the entity primary key
     */
    public abstract K getPK();

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
                state = NEWEDITING;
                fireStateChange(EDIT, oldState, state);
                return;
            case DBENTITY:
                entitySaveState();
                state = DBENTITYEDITING;
                fireStateChange(EDIT, oldState, state);
                return;
            default:
                throw new LogicException("Should not be trying to edit an entity in " + state + " state");
        }
    }

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
            entityRestoreState();
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
     * @param pk the entity primary key
     */
    protected void load(K pk) {
        loader(epp.get(pk));
        fireFieldChangeAtLoad(ALL);
    }

    private void loader(EntityFields data) {
        LogBuilder.writeLog("nbpcglibrary.data", this, "loader", data.toString());
        EntityState oldState = getState();
        entityLoad(data);
        persistent = true;
        setState(DBENTITY);
        fireStateChange(LOAD, oldState, DBENTITY);
    }

    /**
     * Add a Primary Key Listener to this entity.
     *
     * @param listener the listener
     */
    public final void addPrimaryKeyListener(Listener<PrimaryKeyChangeEventParams> listener) {
        primaryKeyChangeEvent.addListener(listener);
    }

    /**
     * Remove a Primary Key listener from this entity.
     *
     * @param listener the listener
     */
    public final void removePrimaryKeyListener(Listener<PrimaryKeyChangeEventParams> listener) {
        primaryKeyChangeEvent.removeListener(listener);
    }

    /**
     * Add a Primary Key Listener to this entity.
     *
     * @param listener the listener
     * @param mode the indicators of listener action (on current thread or on
     * event queue; priority/normal)
     */
    public final void addPrimaryKeyListener(Listener<PrimaryKeyChangeEventParams> listener, Event.ListenerMode mode) {
        primaryKeyChangeEvent.addListener(listener, mode);
    }

    /**
     * Save this entity to entity storage.
     *
     * @return true if save is successful
     */
    public boolean save() {
        EntityFields ef = new EntityFields();
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
                entityValues(ef);
                em.removeFromCache((E) this);
                loader(epp.insert(ef));
                primaryKeyChangeEvent.fire(new PrimaryKeyChangeEventParams());
                setState(DBENTITY);
                break;
            case DBENTITYEDITING:
                if (!checkRules()) {
                    return false;
                }
                entityDiffs(ef);
                if (!ef.isEmpty()) {
                    loader(epp.update(getPK(), ef));
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
    }

    /**
     * Delete this Entity.
     */
    public final void remove() {
        EntityState oldState = getState();
        switch (oldState) {
            case NEW:
            case NEWEDITING:
                entityRemove();
                em.removeFromCache((E) this);
                setState(REMOVED);
                fireStateChange(REMOVE, oldState, REMOVED);
                return;
            case DBENTITY:
            case DBENTITYEDITING:
                entityRemove();
                epp.delete(getPK());
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
     */
    public final void copy(E e) {
        EntityState oldState = getState();
        if (oldState == NEW || oldState == NEWEDITING) {
            entityCopy(e);
            ensureEditing();
            fireFieldChange(ALL);
            return;
        }
        throw new LogicException("Should not be trying to copy an entity in " + oldState + " state");
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
     * Load the entity field data into the entity fields.
     *
     * @param data the data to be inserted
     */
    abstract protected void entityLoad(EntityFields data);

    /**
     * Get the key string which will be used in when sorting this entity
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
     * Add all field values to the given Entity Fields Object
     *
     * @param ef the entityfields object
     */
    abstract protected void entityValues(EntityFields ef);

    /**
     * Add any modified field values to the given Entity Fields Object
     *
     * @param ef the entityfields object
     */
    abstract protected void entityDiffs(EntityFields ef);

    /**
     * Complete any entity specific removal actions prior to entity deletion.
     * Basic use case: remove any linkage to parent entities.
     */
    abstract protected void entityRemove();

    /**
     * Field Copy actions - copy entity fields into this entity.
     *
     * @param from the copy source entity
     */
    abstract protected void entityCopy(E from);
}
