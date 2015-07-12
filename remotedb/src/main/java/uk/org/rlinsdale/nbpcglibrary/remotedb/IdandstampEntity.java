/*
 * Copyright (C) 2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package uk.org.rlinsdale.nbpcglibrary.remotedb;

/**
 * The Additional interface needed for an entity which is implements a standard
 * Id and timeStamp fields.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the entity class
 */
public interface IdandstampEntity<T extends IdandstampEntity> extends BasicEntity<T> {

    /**
     * Get the entity Id (primary key value)
     *
     * @return the entity Id
     */
    public Integer getId();
    
    /**
     * Set the entity Id (primary key value)
     *
     * @param id the entity Id
     */
    public void setId(Integer id);

    /**
     * Set the createdby field
     *
     * @param usercode the value
     */
    public void setCreatedby(String usercode);

    /**
     * set the updatedby field
     *
     * @param usercode the value
     */
    public void setUpdatedby(String usercode);

    /**
     * set the createdon field
     *
     * @param timestamp the value
     */
    public void setCreatedon(String timestamp);

    /**
     * set the updatedon field
     *
     * @param timestamp the value
     */
    public void setUpdatedon(String timestamp);
}
