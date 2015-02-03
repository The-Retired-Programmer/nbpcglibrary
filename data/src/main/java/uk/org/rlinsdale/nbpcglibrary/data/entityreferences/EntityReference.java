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
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManagerRO;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRO;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRW;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;
import org.openide.util.Lookup;
import uk.org.rlinsdale.nbpcglibrary.common.Event;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.LogHelper;
import uk.org.rlinsdale.nbpcglibrary.common.SimpleEventParams;

/**
 * A reference to an Entity.
 *
 * Implemented as a weak reference, so that entities can be garbage collected if
 * unused for a period of time, the entity id is remembered so that the entity
 * can be reloaded if required.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> The Entity Class
 */
public class EntityReference<E extends EntityRO> implements LogHelper {

    public final static int NONE = 0;
    private final String name;
    private int id;
    private int saveId;
    private WeakReference<E> entityreference = null;
    private final EntityManagerRO<E> em;
    private final IdListener idlistener;
    private Listener<SimpleEventParams> titleListener;
    private final Event<SimpleEventParams> changeEntityEvent;

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param em the associated Entity Manager
     */
    public EntityReference(String name, EntityManagerRO<E> em) {
        this(name, NONE, null, em, null);
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param em the associated Entity Manager
     * @param titleChangeListener a listener to call if the reference entities'
     * title changes (due to either entity change or entity content change.
     */
    public EntityReference(String name, EntityManagerRO<E> em, Listener<SimpleEventParams> titleChangeListener) {
        this(name, NONE, null, em, titleChangeListener);
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param id the entity id
     * @param em the associated Entity Manager
     */
    public EntityReference(String name, int id, EntityManagerRO<E> em) {
        this(name, id, null, em, null);
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param id the entity id
     * @param em the associated Entity Mana
     * @param titleChangeListener a listener to call if the reference entities'
     * title changes (due to either entity change or entity content change.
     */
    public EntityReference(String name, int id, EntityManagerRO<E> em, Listener<SimpleEventParams> titleChangeListener) {
        this(name, id, null, em, titleChangeListener);
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param e the entity
     * @param em the associated Entity Manager
     */
    public EntityReference(String name, E e, EntityManagerRO<E> em) {
        this(name, e.getId(), e, em, null);
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param e the entity
     * @param em the associated Entity Manager
     * @param titleChangeListener a listener to call if the reference entities'
     * title changes (due to either entity change or entity content change.
     */
    public EntityReference(String name, E e, EntityManagerRO<E> em, Listener<SimpleEventParams> titleChangeListener) {
        this(name, e.getId(), e, em, titleChangeListener);
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param emclass the associated Entity Manager class
     */
    public EntityReference(String name, Class<? extends EntityManagerRO> emclass) {
        this(name, NONE, null, Lookup.getDefault().lookup(emclass), null);
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param emclass the associated Entity Manager class
     * @param titleChangeListener a listener to call if the reference entities'
     * title changes (due to either entity change or entity content change.
     */
    public EntityReference(String name, Class<? extends EntityManagerRO> emclass, Listener<SimpleEventParams> titleChangeListener) {
        this(name, NONE, null, Lookup.getDefault().lookup(emclass), titleChangeListener);
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param id the entity id
     * @param emclass the associated Entity Manager class
     */
    public EntityReference(String name, int id, Class<? extends EntityManagerRO> emclass) {
        this(name, id, null, Lookup.getDefault().lookup(emclass), null);
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param id the entity id
     * @param emclass the associated Entity Manager class
     * @param titleChangeListener a listener to call if the reference entities'
     * title changes (due to either entity change or entity content change.
     */
    public EntityReference(String name, int id, Class<? extends EntityManagerRO> emclass, Listener<SimpleEventParams> titleChangeListener) {
        this(name, id, null, Lookup.getDefault().lookup(emclass), titleChangeListener);
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param e the entity
     * @param emclass the associated Entity Manager class
     */
    public EntityReference(String name, E e, Class<? extends EntityManagerRO> emclass) {
        this(name, e.getId(), e, Lookup.getDefault().lookup(emclass), null);
    }

    /**
     * Constructor.
     *
     * @param name the name of the entity (for reporting)
     * @param e the entity
     * @param emclass the associated Entity Manager class
     * @param titleChangeListener a listener to call if the reference entities'
     * title changes (due to either entity change or entity content change.
     */
    public EntityReference(String name, E e, Class<? extends EntityManagerRO> emclass, Listener<SimpleEventParams> titleChangeListener) {
        this(name, e.getId(), e, Lookup.getDefault().lookup(emclass), titleChangeListener);
    }

    private EntityReference(String name, int id, E e, EntityManagerRO<E> em, Listener<SimpleEventParams> titleChangeListener) {
        this.name = name;
        this.em = em;
        idlistener = new IdListener(name);
        changeEntityEvent = new Event<>("EntityRefChange:" + name);
        if (titleChangeListener != null) {
            titleListener = titleChangeListener;
            changeEntityEvent.addListener(titleChangeListener);
        }
        set(id, e);
        if (id < 0) {
            if (e == null) {
                e = get();
            }
            if (e != null && e instanceof EntityRW) {
                ((EntityRW) e).addIdListener(idlistener);
            }
        }
        saveState();
    }

    @Override
    public String classDescription() {
        return LogBuilder.classDescription(this, name);
    }

    /**
     * Set the reference to "Null".
     *
     * @return true if referenced entity is different (ie Id has changed)
     */
    public boolean set() {
        return set(NONE, null);
    }

    /**
     * Set the reference by entity Id.
     *
     * @param id the entity id
     * @return true if referenced entity is different (ie Id has changed)
     */
    public boolean set(int id) {
        return set(id, null);
    }

    /**
     * Set the reference by entity.
     *
     * @param e the entity
     * @return true if referenced entity is different (ie Id has changed)
     */
    public boolean set(E e) {
        return set(e == null ? NONE : e.getId(), e);
    }

    private boolean set(int id, E e) {
        LogBuilder.writeLog("nbpcglibrary.data", this, "set", id, e);
        boolean updated = this.id != id;
        if (updated) {
            E old = get();
            if (old != null && titleListener != null) {
                old.removeTitleListener(titleListener);
            }
            this.id = id;
            this.entityreference = e == null ? null : new WeakReference<>(e);
            if (titleListener != null) {
                if (e != null) {
                    e.addTitleListener(titleListener);
                }
                changeEntityEvent.fire(new SimpleEventParams());
            }
        }
        return updated;
    }

    private class IdListener extends Listener<IdChangeEventParams> {

        public IdListener(String name) {
            super(name);
        }

        @Override
        public void action(IdChangeEventParams p) {
            if (entityreference != null) {
                E e = entityreference.get();
                if (e != null) {
                    id = e.getId();
                    ((EntityRW) e).removeIdListener(idlistener);
                    return;
                }
            }
            throw new LogicException("Can't find entity in EntityReference:IdListener()");
        }
    }

    /**
     * Get the Entity Referenced.
     *
     * @return the Entity
     */
    public final E get() {
        if (entityreference != null) {
            E e = entityreference.get();
            if (e != null) {
                return e;
            }
        }
        if (id == NONE) {
            return null;
        }
        E e = em.get(id);
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
     * Get The Entity Id.
     *
     * @return the entity Id
     */
    public final int getId() {
        return id;
    }

    /**
     * Test if this entity has been changed (ie Id is dirty). This could occur
     * when an entity has been inserted into an entity store and received a new
     * valid Id.
     *
     * @return true if changed
     */
    public boolean isDirty() {
        return id != saveId;
    }

    /**
     * Save the State of this entity.
     */
    public final void saveState() {
        saveId = id;
    }

    /**
     * Restore the state of this entity (from the last saved State).
     */
    public final void restoreState() {
        if (isDirty()) {
            E dirtyE = get();
            if (dirtyE != null) {
                dirtyE.removeTitleListener(titleListener);
            }
            entityreference = null;
            id = saveId;
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
            super("No " + name + " defined");
        }

        @Override
        public boolean ruleCheck() {
            return id != NONE;
        }
    }
}
