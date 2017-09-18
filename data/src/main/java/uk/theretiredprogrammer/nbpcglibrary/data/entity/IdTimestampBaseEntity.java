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
package uk.theretiredprogrammer.nbpcglibrary.data.entity;

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

    /**
     * Constructor.
     */
    public IdTimestampBaseEntity() {
    }

    /**
     * Constructor.
     * @param from the entity to copy from
     */
    public IdTimestampBaseEntity(IdTimestampBaseEntity from) {
        id = from.id;
        createdby = from.createdby;
        createdon = from.createdon;
        updatedby = from.updatedby;
        updatedon = from.updatedon;
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
    public void setId(Integer id) {
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
    public void setCreatedby(String createdby) {
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
    public void setCreatedon(String createdon) {
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
    public void setUpdatedby(String updatedby) {
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
    public void setUpdatedon(String updatedon) {
        this.updatedon = updatedon;
    }
}
