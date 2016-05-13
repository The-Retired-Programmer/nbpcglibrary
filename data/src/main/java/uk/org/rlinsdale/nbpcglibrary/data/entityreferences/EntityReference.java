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
package uk.org.rlinsdale.nbpcglibrary.data.entityreferences;

import java.lang.ref.WeakReference;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManager;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;
import uk.org.rlinsdale.nbpcglibrary.common.Event;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;
import static uk.org.rlinsdale.nbpcglibrary.common.Event.ListenerMode.IMMEDIATE;
import uk.org.rlinsdale.nbpcglibrary.common.SimpleEventParams;
import uk.org.rlinsdale.nbpcglibrary.data.entity.CoreEntity;

/**
 * A reference to an Entity.
 *
 * Implemented as a weak reference, so that entities can be garbage collected if
 * unused for a period of time, the entity primary key is remembered so that the
 * entity can be reloaded if required.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> the primary Key class for the entity
 * @param <E> The Entity Class
 * @param <P> The Parent Entity Class
 */
public class EntityReference<K, E extends Entity<K, E, P, ?>, P extends CoreEntity> implements HasInstanceDescription {

    private final String name;
    private K pk;
    private K savePK;
    private WeakReference<E> entityreference = null;
    private final EntityManager<K, E, P> em;
    private final PrimaryKeyListener pkListener;
    private Listener<SimpleEventParams> titleListener = null;

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param em the associated Entity Manager
     */
    public EntityReference(String name, EntityManager<K, E, P> em) {
        this.name = name;
        this.em = em;
        this.pk = null;
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
    public EntityReference(String name, EntityManager<K, E, P> em, Listener<SimpleEventParams> listener) {
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
    public EntityReference(String name, K pk, EntityManager<K, E, P> em) {
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
    public EntityReference(String name, K pk, EntityManager<K, E, P> em, Listener<SimpleEventParams> listener) {
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
    public EntityReference(String name, E e, EntityManager<K, E, P> em) {
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
    public EntityReference(String name, E e, EntityManager<K, E, P> em, Listener<SimpleEventParams> listener) {
        this(name, e, em);
        titleListener = listener;
        e.addTitleListener(listener);
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, name);
    }

    /**
     * Set the reference to "Null".
     *
     * @return true if referenced entity is different (ie primary key has
     * changed)
     */
    public boolean set() {
        if (pk != null) {
            LogBuilder.writeLog("nbpcglibrary.data", this, "set");
            if (titleListener != null) {
                E old = getNoLoad();
                if (old != null) {
                    old.removeTitleListener(titleListener);
                    Event.fireSimpleEventParamsListener(titleListener);
                }
            }
            pk = null;
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
    public boolean set(K pk) {
        LogBuilder.writeLog("nbpcglibrary.data", this, "set", pk);
        if (pk == null) {
            return set();
        }
        boolean updated = (this.pk == null || !this.pk.equals(pk));
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
        K epk = e.getPK();
        boolean updated = (this.pk == null || !this.pk.equals(epk));
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

    private class PrimaryKeyListener extends Listener<PrimaryKeyChangeEventParams<K>> {

        public PrimaryKeyListener(String name) {
            super(name);
        }

        @Override
        public void action(PrimaryKeyChangeEventParams<K> p) {
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
        if (pk == null) {
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
    public final K getPK() {
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
            return pk != null;
        }
    }
}
