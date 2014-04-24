/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package linsdale.nbpcg.datasupportlib.entity;

import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.ListenerParams;

/**
 * The Parameter Class for a FieldChange listener.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FieldChangeListenerParams implements ListenerParams {

    /**
     * Common Field Id - all fields have changed
     */
    public static final IntWithDescription ALLFIELDS = new IntWithDescription(0, "ALL");

    /**
     * Common Field Id - the Id field has changed
     */
    public static final IntWithDescription IDFIELD = new IntWithDescription(1, "Id");

    /**
     * Common Field Id - the Index (ordering) field has change
     */
    public static final IntWithDescription IDXFIELD = new IntWithDescription(2, "Idx");
    private final IntWithDescription field;
    private final boolean formatOK;

    /**
     * Constructor.
     *
     * @param field the field Id
     * @param formatOK true if field is correctly formatted
     */
    public FieldChangeListenerParams(IntWithDescription field, boolean formatOK) {
        this.field = field;
        this.formatOK = formatOK;
    }

    /**
     * Get the field Id
     *
     * @return the field Id
     */
    public IntWithDescription get() {
        return field;
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
    public int hashCode() {
        return field.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof FieldChangeListenerParams) {
            return this.field == ((FieldChangeListenerParams) obj).field;
        }
        if (obj instanceof IntWithDescription) {
            return this.field == (IntWithDescription) obj;
        }
        return false;
    }

    @Override
    public String toString() {
        return field + " change (data was formatted " + (formatOK ? "OK" : "badly") + ")";
    }
}
