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
import java.util.function.Function;
import uk.theretiredprogrammer.nbpcglibrary.api.ApplicationLookup;
import uk.theretiredprogrammer.nbpcglibrary.api.IdTimestampBaseEntity;
import uk.theretiredprogrammer.nbpcglibrary.api.Rest;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import static uk.theretiredprogrammer.nbpcglibrary.common.Event.ListenerMode.IMMEDIATE;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;

/**
 * A reference to an Entity.
 *
 * Implemented as a weak reference, so that entities can be garbage collected if
 * unused for a period of time, the entity primary key is remembered so that the
 * entity can be reloaded if required.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <R> the BasicEntity (data transfer) Class
 * @param <E> The Entity Class
 * @param <P> The Parent Entity Class
 */
public class EntityReference<R extends IdTimestampBaseEntity, E extends Entity, P extends CoreEntity>  {

    private int pk;
    private int savePK;
    private WeakReference<E> entityreference = null;
    private final PrimaryKeyListener pkListener;
    private Listener titleListener = null;
    private final Class<? extends Rest<R>> restclass;
    private final Function<R,E> entitycreator;

    /**
     * Constructor.
     *
     * @param entitycreator a creator function for the Entity
     * @param restclass class of the rest client for this entity 
     */
    public EntityReference(Function<R,E> entitycreator, Class<? extends Rest<R>> restclass) {
        this.restclass = restclass;
        this.pk = 0;
        this.entityreference = null;
        this.entitycreator = entitycreator;
        pkListener = null;
        saveState();
    }

    /**
     * Constructor.
     *
     * @param entitycreator a creator function for the Entity
     * @param restclass class of the rest client for this entity
     * @param listener a listener to receive title change actions
     */
    public EntityReference(Function<R,E> entitycreator, Class<? extends Rest<R>> restclass, Listener listener) {
        this(entitycreator, restclass);
        titleListener = listener;
    }

    /**
     * Constructor.
     *
     * @param entitycreator a creator function for the Entity
     * @param restclass class of the rest client for this entity
     * @param pk the primary key
     */
    public EntityReference(Function<R,E> entitycreator, Class<? extends Rest<R>> restclass, int pk) {
        this.restclass = restclass;
        this.pk = pk;
        this.entityreference = null;
        this.entitycreator = entitycreator;
        pkListener = null;
        saveState();
    }

    /**
     * Constructor.
     *
     * @param entitycreator a creator function for the Entity
     * @param restclass class of the rest client for this entity
     * @param e the entity
     */
    public EntityReference(Function<R,E> entitycreator, Class<? extends Rest<R>> restclass, E e) {
        this.restclass = restclass;
        this.entitycreator = entitycreator;
        pkListener = new PrimaryKeyListener();
        this.pk = e.getPK();
        if (!e.isPersistent()) {
            e.addPrimaryKeyListener(pkListener, IMMEDIATE);
        }
        saveState();
    }


    /**
     * Set the reference to "Null" (ie value 0).
     *
     * @return true if referenced entity is different (ie primary key has
     * changed)
     */
    public boolean set() {
        if (pk != 0) {
            pk = 0;
            return true;
        }
        return false;
    }

    /**
     * Set the reference by entity.
     *
     * @param e the entity
     * @return true if referenced entity is different (ie primary key has
     * changed)
     */
    public boolean set(E e) {
        int epk = e.getPK();
        boolean updated = (this.pk == 0 || this.pk != epk);
        if (updated) {
                this.pk = epk;
                this.entityreference = new WeakReference<>(e);
            if (!e.isPersistent()){
                 e.addPrimaryKeyListener(pkListener, IMMEDIATE);
            }
        }
        return updated;
    }

    private class PrimaryKeyListener extends Listener {

        @Override
        public void action(Object p) {
            pk = (int) p;
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
        Rest<R> rest = ApplicationLookup.getDefault().lookup(restclass);
        R baseentity = rest.get(pk);
        if (baseentity == null){
            return null;
        }
        E e = entitycreator.apply(baseentity);
        this.entityreference = new WeakReference<>(e);
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
            entityreference = null;
            pk = savePK;
        }
    }
}
