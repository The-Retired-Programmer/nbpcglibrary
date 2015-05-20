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

import uk.org.rlinsdale.nbpcglibrary.api.EventParams;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * The Parameter Class for a FieldChange listener.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <F> the Fields enum class
 */
public class EntityFieldChangeEventParams<F> implements EventParams {
    
    /**
     * Entity field changes - common to all entities. 
     */
    public enum CommonEntityField {

        /**
         * All fields have/ may have been changed.
         */
        ALL,

        /**
         * The Id (primary key) field has changed
         */
        ID,

        /**
         * The index (ordering inndex) field has changed
         */
        IDX
    };

    private final F field;
    private final CommonEntityField commonfield;
    private final boolean formatOK;

    /**
     * Constructor.
     *
     * @param field the entity field identifier
     * @param commonfield the common entity field identifier
     * @param formatOK true if field is correctly formatted
     */
    public EntityFieldChangeEventParams(F field, CommonEntityField commonfield, boolean formatOK) {
        this.field = field;
        this.commonfield = commonfield;
        this.formatOK = formatOK;
    }

    /**
     * Get the field Id
     *
     * @return the field identifier
     */
    public F get() {
        return field;
    }
    /**
     * Get the common field Id
     *
     * @return the common field identifier
     */
    public CommonEntityField getCommon() {
        return commonfield;
    }

    /**
     * Test if the field was formatted correctly.
     *
     * @return true if field was formatted correctly
     */
    public boolean isFormatOK() {
        return formatOK;
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, (field!=null?field.toString():"--")+"/"+(commonfield!=null?commonfield.toString():"--")+ " change (data was formatted " + (formatOK ? "OK" : "badly") + ")");
    }
}
