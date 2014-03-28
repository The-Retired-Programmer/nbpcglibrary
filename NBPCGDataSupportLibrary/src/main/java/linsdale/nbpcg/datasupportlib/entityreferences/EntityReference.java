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
package linsdale.nbpcg.datasupportlib.entityreferences;

import java.lang.ref.WeakReference;
import java.util.logging.Level;
import linsdale.nbpcg.datasupportlib.entity.EntityManagerRO;
import linsdale.nbpcg.datasupportlib.entity.EntityRO;
import linsdale.nbpcg.datasupportlib.entity.EntityRW;
import linsdale.nbpcg.supportlib.Listener;
import linsdale.nbpcg.supportlib.Log;
import linsdale.nbpcg.supportlib.LogicException;
import linsdale.nbpcg.supportlib.Rule;
import org.openide.util.Lookup;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
public class EntityReference<E extends EntityRO> {

    private final static int NONE = 0;
    private final String name;
    private int id;
    private int saveId;
    private WeakReference<E> entityreference = null;
    private final EntityManagerRO<E> em;
    private final IdListener idlistener;

    /* constructors */
    public EntityReference(String name, EntityManagerRO<E> em) {
        this(name, NONE, null, em);
    }

    public EntityReference(String name, int id, EntityManagerRO<E> em) {
        this(name, id, null, em);
    }

    public EntityReference(String name, E e, EntityManagerRO<E> em) {
        this(name, e.getId(), e, em);
    }

    private EntityReference(String name, int id, E e, EntityManagerRO<E> em) {
        idlistener = new IdListener(name);
        init(id, e);
        this.name = name;
        this.em = em;
    }

    public EntityReference(String name, Class<? extends EntityManagerRO> emclass) {
        this(name, NONE, null, emclass);
    }

    public EntityReference(String name, int id, Class<? extends EntityManagerRO> emclass) {
        this(name, id, null, emclass);
    }

    public EntityReference(String name, E e, Class<? extends EntityManagerRO> emclass) {
        this(name, e.getId(), e, emclass);
    }

    private EntityReference(String name, int id, E e, Class<? extends EntityManagerRO> emclass) {
        idlistener = new IdListener(name);
        init(id, e);
        this.name = name;
        this.em = Lookup.getDefault().lookup(emclass);
    }

    /* set the reference */
    public boolean set() {
        return set(NONE, null);
    }

    public boolean set(int id) {
        return set(id, null);
    }

    public boolean set(E e) {
        return set(e == null ? NONE : e.getId(), e);
    }

    private boolean set(int id, E e) {
        Log.get("linsdale.nbpcg.supportlib.datasupportlib").log(Level.FINEST, "Setting entity reference (id={0}; entity={1}", new Object[]{id, e == null ? "Undefined" : "Defined"});
        boolean updated = this.id != id;
        this.id = id;
        this.entityreference = e == null ? null : new WeakReference<>(e);
        saveState();
        return updated;
    }
    
    private void init(int id, E e) {
        set(id,e);
        if (id < 0) {
            if (e == null) {
                e = get();
            }
            if (e != null && e instanceof EntityRW) {
                ((EntityRW)e).addIdListener(idlistener);
            }
        }
    }
    
    private class IdListener extends Listener<IdListenerParams> {

        public IdListener(String name) {
            super(name + "/entityref/id");
        }

        @Override
        public void action(IdListenerParams p) {
            if (entityreference != null) {
                E e = entityreference.get();
                if (e != null) {
                    id = e.getId();
                    ((EntityRW)e).removeIdListener(idlistener);
                    return;
                }
            }
            throw new LogicException("Can't find entity in EntityReference:IdListener()");
        }
    }

    /* get the reference */
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
        return e;
    }

    /* get the reference id */
    public final int getId() {
        return id;
    }

    public final boolean isValidEntity() {
        return id != NONE;
    }

    public boolean isDirty() {
        return id != saveId;
    }

    public final void saveState() {
        saveId = id;
    }

    public final void restoreState() {
        if (isDirty()) {
            entityreference = null;
            id = saveId;
        }
    }

    public Rule getDefinedRule() {
        return new DefinedRule();
    }

    private class DefinedRule extends Rule {

        public DefinedRule() {
            super("No " + name + " defined");
        }

        @Override
        public boolean ruleCheck() {
            return isValidEntity();
        }
    }
}
