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

import uk.org.rlinsdale.nbpcglibrary.common.LogHelper;
import uk.org.rlinsdale.nbpcglibrary.common.Rules;

/**
 * Abstract Entity Class - delivering all basic entity functionality.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class Entity extends Rules implements LogHelper {

    private final EntityError entityerror;
    private final EntitySave entitysave;

    /**
     * Constructor.
     *
     * @param entityname the name of the entity class (for reporting purposes)
     */
    public Entity(String entityname) {
        super();
        entityerror = new EntityError(entityname);
        entitysave = new EntitySave(entityname);
        updateEntityRegistration();
    }


    /**
     * Cancel any changes in progress and restore state as at last save state.
     */
    public void cancelEdit() {
        _restoreState();
        updateEntityRegistration();
    }

    /**
     * Update entity registration for this entity. Set entity error based on
     * check of entity rules and set entity save state based on current entity
     * state.
     */
    protected final void updateEntityRegistration() {
        if (checkRules()) {
            EntityRegistry.unregister(entityerror);
            if (this instanceof EntityRO) {
                EntityRO thisRO = (EntityRO) this;
                if (thisRO.isEditing() || thisRO.isNew()) {
                    EntityRegistry.register(entitysave);
                } else {
                    EntityRegistry.unregister(entitysave);
                }
            }
        } else {
            EntityRegistry.register(entityerror);
            if (this instanceof EntityRO) {
                EntityRegistry.unregister(entitysave);
            }
        }
    }

    /**
     * Update entity registration for this entity at load state. Set entity error based on
     * check of entity rules at load and set entity save state based on current entity
     * state.
     */
    protected final void updateEntityRegistrationAtLoad() {
        if (checkRulesAtLoad()) {
            EntityRegistry.unregister(entityerror);
        } else {
            EntityRegistry.register(entityerror);
        }
        if (this instanceof EntityRO) {
            EntityRegistry.unregister(entitysave);
        }
    }

    /**
     * Remove entity error and entity save registrations for this entity.
     */
    protected final void removeEntityRegistration() {
        EntityRegistry.unregister(entityerror);
        EntityRegistry.unregister(entitysave);
    }

    /**
     * Restore entity state.
     */
    abstract protected void _restoreState();

    private class EntityError implements EntityInError {

        private final String name;

        public EntityError(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof EntityError) && this == (((EntityError) obj));
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    private class EntitySave implements EntityNeedsSaving {

        private final String name;

        public EntitySave(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof EntitySave) && this == (((EntitySave) obj));
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
