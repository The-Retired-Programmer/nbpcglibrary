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

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.spi.actions.AbstractSavable;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterLog;
import uk.theretiredprogrammer.nbpcglibrary.api.ApplicationLookup;
import uk.theretiredprogrammer.nbpcglibrary.common.Event;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityFields;
import uk.theretiredprogrammer.nbpcglibrary.api.IdTimestampBaseEntity;
import uk.theretiredprogrammer.nbpcglibrary.api.Rest;
import uk.theretiredprogrammer.nbpcglibrary.data.onstop.LibraryOnStop;
import static uk.theretiredprogrammer.nbpcglibrary.data.onstop.LibraryOnStop.isSavableEnabled;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState;
import static uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.*;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange;
import static uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.CREATE;
import static uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.EDIT;
import static uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.LOAD;
import static uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.REMOVE;
import static uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.RESET;
import static uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.SAVE;

/**
 * The Entity Abstract Class.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <R> the base entity class used in the rest transfer
 * @param <E> the entity class
 * @param <P> the Parent Entity Class
 * @param <F> the entity field types
 */
@RegisterLog("nbpcglibrary.data")
public abstract class Entity<R extends IdTimestampBaseEntity, E extends Entity, P extends CoreEntity, F> extends CoreEntity {

    private final Event stateEvent;
    private final Event fieldEvent;
    @SuppressWarnings("FieldMayBeFinal") 
    private Event titleChangeEvent;
    @SuppressWarnings("FieldMayBeFinal")
    private Event nameChangeEvent;
    private EntityState state = INIT;
    private final Event primaryKeyChangeEvent;
    private final EntityStateChangeListener entitystatechangelistener;
    private final EntitySavable savable = new EntitySavable();
    private final Class<? extends Rest<R>> restclass;

    /**
     * Constructor.
     *
     * @param restclass class of the rest client for this entity
     */
    public Entity(Class<? extends Rest<R>> restclass) {
        this.restclass = restclass;
        stateEvent = new Event();
        fieldEvent = new Event();
        nameChangeEvent = new Event();
        titleChangeEvent = new Event();
        EntityState oldState = state;
        state = NEW;
        fireStateChange(CREATE, oldState, state);
        //
        primaryKeyChangeEvent = new Event();
        entitystatechangelistener = new EntityStateChangeListener();
        addStateListener(entitystatechangelistener);
        // and as this is new it is saveable (too early to rely on listener)
        savable.add();
    }

    /**
     * Test persistent status
     *
     * @return true if entity has been persisted.
     */
    public abstract boolean isPersistent();

    /**
     * Get the entity primary key.
     *
     * @return the entity primary key
     */
    public abstract int getPK();

    /**
     * Get the entity order index.
     *
     * @return the order index
     */
    public int getIdx() {
        return 0;
    }

    /**
     * Set the entity order index.
     *
     * @param idx the order index
     */
    public void setIdx(int idx) {
    }

    final void setState(EntityState state) {
        this.state = state;
    }

    /**
     * Get the current entity state
     *
     * @return the entity state
     */
    protected final EntityState getState() {
        return state;
    }

    /**
     * Add a State Listener to this entity.
     *
     * @param listener the listener
     */
    public final void addStateListener(Listener listener) {
        stateEvent.addListener(listener);
    }

    /**
     * Remove a State listener from this entity.
     *
     * @param listener the listener
     */
    public final void removeStateListener(Listener listener) {
        stateEvent.removeListener(listener);
    }

    /**
     * Add a Field Listener to this entity.
     *
     * @param listener the listener
     */
    public final void addFieldListener(Listener listener) {
        fieldEvent.addListener(listener);
    }

    /**
     * Remove a Field Listener from this entity.
     *
     * @param listener the listener
     */
    public final void removeFieldListener(Listener listener) {
        fieldEvent.removeListener(listener);
    }

    /**
     * Fire actions on all field change listeners relating to a specified field.
     *
     * @param field the field Id
     */
    protected final void fireFieldChange(F field) {
        fieldEvent.fire(field);
    }

    /**
     * Fire actions on all field change listeners relating to all fields.
     */
    protected final void fireFieldChange() {
        fieldEvent.fire(null);
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
     * Add a Name listener.
     *
     * @param listener the listener
     */
    public final void addNameListener(Listener listener) {
        nameChangeEvent.addListener(listener);
    }

    /**
     * Remove a Name listener.
     *
     * @param listener the listener
     */
    public final void removeNameListener(Listener listener) {
        nameChangeEvent.removeListener(listener);
    }

    /**
     * Fire Listeners if name changes.
     */
    protected void nameListenerFire() {
        nameChangeEvent.fire(null);
    }

    /**
     * Add a Title listener.
     *
     * @param listener the listener
     */
    public final void addTitleListener(Listener listener) {
        titleChangeEvent.addListener(listener);
    }

    /**
     * Remove a Title listener.
     *
     * @param listener the listener
     */
    public final void removeTitleListener(Listener listener) {
        titleChangeEvent.removeListener(listener);
    }

    /**
     * Fire Listeners if title changes.
     */
    protected void titleListenerFire() {
        titleChangeEvent.fire(null);
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
            fireFieldChange();
            fireStateChange(RESET, oldState, state);
            nameListenerFire();
            titleListenerFire();
        }
    }

    /**
     * Load Data from entity storage into this entity and fire the field change
     * at load listeners.
     *
     * @param id the entity primary key
     */
    protected void load(int id) {
        if (isPersistent()) {
            Rest<R> rest = ApplicationLookup.getDefault().lookup(restclass);
            R baseentity = rest.get(id);
            EntityState oldState = getState();
            setBaseEntity(baseentity);
            setState(DBENTITY);
            fireStateChange(LOAD, oldState, DBENTITY);
            fireFieldChange();
        }
    }

    /**
     * Remove a Primary Key listener from this entity.
     *
     * @param listener the listener
     */
    public final void removePrimaryKeyListener(Listener listener) {
        primaryKeyChangeEvent.removeListener(listener);
    }

    /**
     * Add a Primary Key Listener to this entity.
     *
     * @param listener the listener
     * @param mode the indicators of listener action (on current thread or on
     * event queue; priority/normal)
     */
    public final void addPrimaryKeyListener(Listener listener, Event.ListenerMode mode) {
        primaryKeyChangeEvent.addListener(listener, mode);
    }

    /**
     * Save this entity to entity storage.
     *
     * @param sb Stringbuilder object used to collect any failure messages
     * @return true if save is successful
     */
    public boolean save(StringBuilder sb) {
        Rest<R> rest;
        EntityFields ef = new EntityFields();
        EntityState oldState = getState();
        switch (oldState) {
            case DBENTITY:
                return true;
            case REMOVED:
                return false;
            case NEW:
            case NEWEDITING:
                if (!checkRules(sb)) {
                    return false;
                }
                if (!entityValues(ef)) {
                    return false;
                }
                rest = ApplicationLookup.getDefault().lookup(restclass);
                setBaseEntity(rest.create(getBaseEntity()));
                int newPK = getPK();
                primaryKeyChangeEvent.fire(newPK);
                setState(DBENTITY);
                break;
            case DBENTITYEDITING:
                if (!checkRules(sb)) {
                    return false;
                }
                if (!entityDiffs(ef)) {
                    return false;
                }
                if (!ef.isEmpty()) {
                    rest = ApplicationLookup.getDefault().lookup(restclass);
                    setBaseEntity(rest.update(getPK(), getBaseEntity()));
                }
                setState(DBENTITY);
                break;
            default:
                if (!checkRules(sb)) {
                    return false;
                }
        }
        fireStateChange(SAVE, oldState, DBENTITY);
        fireFieldChange();
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
                //em.removeFromCache((E) this);
                setState(REMOVED);
                fireStateChange(REMOVE, oldState, REMOVED);
                return;
            case DBENTITY:
            case DBENTITYEDITING:
                entityRemove();
                Rest<R> rest = ApplicationLookup.getDefault().lookup(restclass);
                rest.delete(getPK());
                //em.removeFromCache((E) this);
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
            fireFieldChange();
            return;
        }
        throw new LogicException("Should not be trying to copy an entity in " + oldState + " state");
    }

    private class EntityStateChangeListener extends Listener {

        @Override
        public void action(Object p) {
            switch ( ((EntityStateChangeEventParams) p).getTransition()) {
                case EDIT:
                    savable.add();
                    break;
                case LOAD:
                    savable.remove();
                    break;
                case SAVE:
                    savable.remove();
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
        return checkRules(new StringBuilder()) ? super.getIcon() : getIconWithError();
    }

//    private class EntitySavable<E, P, F> extends AbstractSavable implements Icon, HasInstanceDescription {
    private class EntitySavable<E> extends AbstractSavable implements Icon {

        private Icon icon;
        private boolean isRegisteredAsOutstanding = false;

        public void add() {
            if (isSavableEnabled()) {
                if (getLookup().lookup(EntitySavable.class) == null) {
                    register();
                    addLookupContent(this);
                }
                icon = null;
            } else {
                if (!isRegisteredAsOutstanding) {
                    LibraryOnStop.incRegisterOutstanding();
                    isRegisteredAsOutstanding = true;
                }
            }
        }

        public void remove() {
            if (isSavableEnabled()) {
                removeLookupContent(this);
                unregister();
            } else {
                if (isRegisteredAsOutstanding) {
                    LibraryOnStop.decRegisterOutstanding();
                    isRegisteredAsOutstanding = false;
                }
            }
        }

        @Override
        protected void handleSave() throws IOException {
            StringBuilder sb = new StringBuilder();
            if (!Entity.this.save(sb)) {
                EventQueue.invokeLater(new ReRegister());
                LibraryOnStop.incRegisterOutstanding();
                // TODO - should we place the error message in a visible place
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
            return "was getDisplayname()";
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
     * Add all field values to the given Entity Fields Object
     *
     * @param ef the entityfields object
     * @return false if save processing is to terminate due to entity data not
     * being correct
     */
    abstract protected boolean entityValues(EntityFields ef);

    /**
     * Add any modified field values to the given Entity Fields Object
     *
     * @param ef the entityfields object
     * @return false if save processing is to terminate due to entity data not
     * being correct
     */
    abstract protected boolean entityDiffs(EntityFields ef);

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

    abstract protected R getBaseEntity();

    abstract protected void setBaseEntity(R entity);
}
