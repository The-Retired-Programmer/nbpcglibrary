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

import uk.org.rlinsdale.nbpcglibrary.api.EntityPersistenceProvider;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.NEW;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.NEWEDITING;

/**
 * The abstract class defining an editable Entity, with a index (orderable)
 * field.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> the primary key type
 * @param <E> the entity class
 * @param <P> the Parent CoreEntity Class
 * @param <F> the entity field enum class
 */
public abstract class EntityIndexed<K, E extends EntityIndexed<K, E, P, F>, P extends CoreEntity, F> extends Entity<K, E, P, F> {

    private final EntityPersistenceProvider epp;

    /**
     * Constructor.
     *
     * @param entityname the entity name
     * @param icon the icon name
     * @param em the entity manager for this entity class
     */
    public EntityIndexed(String entityname, String icon, EntityManager<K, E, P> em) {
        super(entityname, icon, em);
        this.epp = em.getEntityPersistenceProvider();
    }

    /**
     * Get the index value.
     *
     * @return the index value
     */
    protected abstract int getIndexValue();

    /**
     * Set the index value.
     *
     * @param idx the index value
     */
    protected abstract void setIndexValue(int idx);

    @Override
    public final boolean save() {
        EntityState oldState = getState();
        if (oldState == NEW || oldState == NEWEDITING) {
            if (getIndexValue() == Integer.MAX_VALUE) {
                setIndexValue(epp.findNextIdx());
            }
        }
        return super.save();
    }

    /**
     * Get the Index Field value.
     *
     * @return the index value
     */
    public int getIndex() {
        return getIndexValue();
    }

    /**
     * Set the Index Field value.
     *
     * @param i the index value
     */
    public final void setIndex(int i) {
        ensureEditing();
        setIndexValue(i);
        // TODO no fire on index change
        //fireFieldChange(IDX);
    }
}
