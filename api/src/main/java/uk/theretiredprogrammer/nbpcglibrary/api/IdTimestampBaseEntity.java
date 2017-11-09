/*
 * Copyright 2017 Richard Linsdale (richard at theretiredprogrammer.uk).
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
package uk.theretiredprogrammer.nbpcglibrary.api;

/**
 * The IdTimestampBaseEntity.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public abstract class IdTimestampBaseEntity {
    
    private Integer id;
    private String createdby;
    private String createdon;
    private String updatedby;
    private String updatedon;
    
    private boolean dirty = false;
    
    /**
     * Constructor.
     */
    public IdTimestampBaseEntity() {
        id = 0;  // not persistent
    }
    
    /**
     * Copy all value from the given entity
     * 
     * @param dirtystate the dirty state for this entity
     * @param from the entity to copy from
     */
    public final void copy(boolean dirtystate, IdTimestampBaseEntity from) {
        id = from.id;
        createdby = from.createdby;
        createdon = from.createdon;
        updatedby = from.updatedby;
        updatedon = from.updatedon;
        dirty = dirtystate;
    }
    
    /**
     * Test if this entity is persistent (ie has existed in persistent storage.
     *
     * @return true if the entity is persistent
     */
    public boolean persistent() {
        return getId() > 0;
    }
    
    /**
     * Set Dirty state.  Marks entity as needing saving.
     * @param dirtystate the required dirty state
     */
    public final void dirty(boolean dirtystate) {
        dirty = dirtystate;
    }
    
    /**
     * Get Dirty state.
     * 
     * @return the dirty state
     */
    public final boolean dirty() {
        return dirty;
    }
    
    /**
     * Get the id.
     *
     * @return the id
     */
    public final Integer getId() {
        return id;
    }

    /**
     * Define the Id.
     *
     * @param id the id
     */
    public final void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the createdby.
     *
     * @return the createdby
     */
    public final String getCreatedby() {
        return createdby;
    }

    /**
     * Define the Createdby.
     *
     * @param createdby the createdby
     */
    public final void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    /**
     * Get the createdon.
     *
     * @return the createdon
     */
    public final String getCreatedon() {
        return createdon;
    }

    /**
     * Define the Createdon.
     *
     * @param createdon the createdon
     */
    public final void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    /**
     * Get the updatedby.
     *
     * @return the updatedby
     */
    public final String getUpdatedby() {
        return updatedby;
    }

    /**
     * Define the Updatedby.
     *
     * @param updatedby the updatedby
     */
    public final void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    /**
     * Get the updatedon.
     *
     * @return the updatedon
     */
    public final String getUpdatedon() {
        return updatedon;
    }

    /**
     * Define the Updatedon.
     *
     * @param updatedon the updatedon
     */
    public final void setUpdatedon(String updatedon) {
        this.updatedon = updatedon;
    }
    
    /**
     *  Test if required field has the required value
     * @param fieldname the name of the field being tested
     * @param fieldvalue the value to be matched in that field
     * @return true if this entity has field set to defined value
     */
    public boolean isMatch(String fieldname, int fieldvalue) {
        return false; // default action - override if method is fully supported by base entity
    }
}
