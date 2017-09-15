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
package uk.theretiredprogrammer.nbpcglibrary.data.entityreferences;

import java.lang.ref.WeakReference;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityManager;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import uk.theretiredprogrammer.nbpcglibrary.common.Rule;
import uk.theretiredprogrammer.nbpcglibrary.common.Event;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.api.HasInstanceDescription;
import static uk.theretiredprogrammer.nbpcglibrary.common.Event.ListenerMode.IMMEDIATE;
import uk.theretiredprogrammer.nbpcglibrary.common.SimpleEventParams;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;

/**
 * A reference to an Entity.
 *
 * Implemented as a weak reference, so that entities can be garbage collected if
 * unused for a period of time, the entity primary key is remembered so that the
 * entity can be reloaded if required.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <K> the primary Key class for the entity
 * @param <E> The Entity Class
 * @param <P> The Parent Entity Class
 */
public class EntityReference<E extends Entity, P extends CoreEntity> implements HasInstanceDescription {

    private final String name;
    private int pk;
    private int savePK;
    private WeakReference<E> entityreference = null;
    private final EntityManager<E, P> em;
    private final PrimaryKeyListener pkListener;
    private Listener<SimpleEventParams> titleListener = null;

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param em the associated Entity Manager
     */
    public EntityReference(String name, EntityManager<E, P> em) {
        this.name = name;
        this.em = em;
        this.pk = 0;
        this.entityreference = null;
        pkListener = null;
        saveState();
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param em the associated Entity Manager
     * @param listener a listener to receive title change actions
     */
    public EntityReference(String name, EntityManager<E, P> em, Listener<SimpleEventParams> listener) {
        this(name, em);
        titleListener = listener;
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param pk the primary key
     * @param em the associated Entity Manager
     */
    public EntityReference(String name, int pk, EntityManager<E, P> em) {
        this.name = name;
        this.em = em;
        this.pk = pk;
        this.entityreference = null;
        pkListener = null;
        saveState();
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param pk the primary key
     * @param em the associated Entity Manager
     * @param listener a listener to receive title change actions
     */
    public EntityReference(String name, int pk, EntityManager<E, P> em, Listener<SimpleEventParams> listener) {
        this(name, pk, em);
        titleListener = listener;
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param e the entity
     * @param em the associated Entity Manager
     */
    public EntityReference(String name, E e, EntityManager<E, P> em) {
        this.name = name;
        this.em = em;
        pkListener = new PrimaryKeyListener(name);
        this.pk = e.getPK();
        if (!e.isPersistent()) {
            e.addPrimaryKeyListener(pkListener, IMMEDIATE);
        }
        saveState();
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param e the entity
     * @param em the associated Entity Manager
     * @param listener a listener to receive title change actions
     */
    public EntityReference(String name, E e, EntityManager<E, P> em, Listener<SimpleEventParams> listener) {
        this(name, e, em);
        titleListener = listener;
        e.addTitleListener(listener);
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, name);
    }

    /**
     * Set the reference to "Null" (ie value 0).
     *
     * @return true if referenced entity is different (ie primary key has
     * changed)
     */
    public boolean set() {
        if (pk != 0) {
            LogBuilder.writeLog("nbpcglibrary.data", this, "set");
            if (titleListener != null) {
                E old = getNoLoad();
                if (old != null) {
                    old.removeTitleListener(titleListener);
                    Event.fireSimpleEventParamsListener(titleListener);
                }
            }
            pk = 0;
            return true;
        }
        return false;
    }

    /**
     * Set the reference by entity primary key.
     *
     * @param pk the primary key
     * @return true if referenced entity is different (ie primary key has
     * changed)
     */
    public boolean set(int pk) {
        LogBuilder.writeLog("nbpcglibrary.data", this, "set", pk);
        if (pk == 0) {
            return set();
        }
        boolean updated = (this.pk == 0 || this.pk != pk);
        if (updated) {
            if (titleListener != null) {
                E old = getNoLoad();
                if (old != null) {
                    old.removeTitleListener(titleListener);
                }
                this.pk = pk;
                E e = em.get(pk);
                this.entityreference = e == null ? null : new WeakReference<>(e);
                if (titleListener != null) {
                    if (e != null) {
                        e.addTitleListener(titleListener);
                    }
                    Event.fireSimpleEventParamsListener(titleListener);
                }
            } else {
                this.pk = pk;
                this.entityreference = null;
            }
        }
        return updated;
    }
    
    /**
     * Set the reference by entity.
     *
     * @param e the entity
     * @return true if referenced entity is different (ie primary key has
     * changed)
     */
    public boolean set(E e) {
        LogBuilder.writeLog("nbpcglibrary.data", this, "set", e.instanceDescription());
        int epk = e.getPK();
        boolean updated = (this.pk == 0 || this.pk != epk);
        if (updated) {
            if (titleListener != null) {
                E old = getNoLoad();
                if (old != null) {
                    old.removeTitleListener(titleListener);
                }
                this.pk = epk;
                this.entityreference = new WeakReference<>(e);
                if (titleListener != null) {
                        e.addTitleListener(titleListener);
                    Event.fireSimpleEventParamsListener(titleListener);
                }
            } else {
                this.pk = epk;
                this.entityreference = new WeakReference<>(e);
            }
            if (!e.isPersistent()){
                 e.addPrimaryKeyListener(pkListener, IMMEDIATE);
            }
        }
        return updated;
    }

    private class PrimaryKeyListener extends Listener<PrimaryKeyChangeEventParams<Integer>> {

        public PrimaryKeyListener(String name) {
            super(name);
        }

        @Override
        public void action(PrimaryKeyChangeEventParams<Integer> p) {
            pk = p.getNewPKey();
            saveState();
        }
    }

    /**
     * Get the Entity Referenced.
     *
     * @return theEntity
     */
    public final E get() {
        if (entityreference != null) {
            E e = entityreference.get();
            if (e != null) {
                return e;
            }
        }
        if (pk == 0) {
            return null;
        }
        E e = em.get(pk);
        this.entityreference = e == null ? null : new WeakReference<>(e);
        if (e != null) {
            e.addTitleListener(titleListener);
        }
        return e;
    }

    /**
     * Get the Entity Referenced if it is actually directly referenced, but
     * don't load it from db it not.
     *
     * @return the Entity
     */
    public final E getNoLoad() {
        return entityreference == null ? null : entityreference.get();
    }

    /**
     * Get The Entity primary key.
     *
     * @return the entity primary key
     */
    public final int getPK() {
        return pk;
    }

    /**
     * Test if this entity has been changed (ie Id is dirty). This could occur
     * when an entity has been inserted into an entity store and received a new
     * valid Id.
     * 
     * @return true if changed
     */
    public boolean isDirty() {
        return pk != savePK;
    }

    /**
     * Save the State of this entity.
     */
    public final void saveState() {
        savePK = pk;
    }

    /**
     * Restore the state of this entity (from the last saved State).
     *
     */
    public final void restoreState() {
        if (isDirty()) {
            E dirtyE = get();
            if (dirtyE != null) {
                dirtyE.removeTitleListener(titleListener);
            }
            entityreference = null;
            pk = savePK;
        }
    }

    /**
     * Get a rule to test if this reference is valid.
     *
     * @return the rule
     */
    public Rule getDefinedRule() {
        return new DefinedRule();
    }

    private class DefinedRule extends Rule {

        public DefinedRule() {
            super("Not defined");
        }

        @Override
        public boolean ruleCheck() {
            return pk != 0;
        }
    }
}
