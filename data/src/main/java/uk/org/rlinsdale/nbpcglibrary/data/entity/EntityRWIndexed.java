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

import uk.org.rlinsdale.nbpcglibrary.data.dataaccess.DataAccessRW;
import uk.org.rlinsdale.nbpcglibrary.data.dbfields.DBFieldsRWIndexed;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.NEW;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityState.NEWEDITING;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityFieldChangeEventParams.CommonEntityField.IDX;

/**
 * The abstract class defining an editable Entity, with a index (orderable)
 * field.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the entity class
 * @param <P> the Parent Entity Class
 * @param <F> the entity field enum class
 */
public abstract class EntityRWIndexed<E extends EntityRWIndexed, P extends Entity, F> extends EntityRW<E, P, F> {

    private final DBFieldsRWIndexed<E> dbfields;
    private final DataAccessRW dataAccess;

    /**
     * Constructor.
     *
     * @param entityname the entity name
     * @param icon the icon name
     * @param id the entity Id
     * @param em the entity manager for this entity class
     * @param dbfields the entity fields
     */
    public EntityRWIndexed(String entityname, String icon, int id, EntityManagerRW<E,P> em, DBFieldsRWIndexed<E> dbfields) {
        super(entityname, icon, id, em, dbfields);
        this.dbfields = dbfields;
        this.dataAccess = em.getDataAccess();
    }

    @Override
    public final boolean save() {
        EntityState oldState = getState();
        if (oldState == NEW || oldState == NEWEDITING) {
            if (dbfields.getIndex() == Integer.MAX_VALUE) {
                dbfields.setIndex(dataAccess.getNextIdx());
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
        return dbfields.getIndex();
    }

    /**
     * Set the Index Field value.
     *
     * @param i the index value
     */
    public final void setIndex(int i) {
        ensureEditing();
        dbfields.setIndex(i);
        fireFieldChange(IDX);
    }
}
