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
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T>
 */
public interface IdandstampEntity<T extends IdandstampEntity> extends BasicEntity<T> {

    /**
     *
     * @return
     */
    public Integer getId();

    /**
     *
     * @param usercode
     */
    public void setCreatedby(String usercode);

    /**
     *
     * @param usercode
     */
    public void setUpdatedby(String usercode);

    /**
     *
     * @param timestamp
     */
    public void setCreatedon(String timestamp);

    /**
     *
     * @param timestamp
     */
    public void setUpdatedon(String timestamp);
}
