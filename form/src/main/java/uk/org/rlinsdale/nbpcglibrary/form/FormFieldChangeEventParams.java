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
package uk.org.rlinsdale.nbpcglibrary.form;

import uk.org.rlinsdale.nbpcglibrary.common.IntWithDescription;
import uk.org.rlinsdale.nbpcglibrary.common.EventParams;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * The listener parameters which are passed when the listener is fired for a
 * Form Field Change.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FormFieldChangeEventParams implements EventParams {

    private final IntWithDescription field;

    /**
     * Constructor
     *
     * @param field the Field ID
     */
    public FormFieldChangeEventParams(IntWithDescription field) {
        this.field = field;
    }
    
    @Override
    public String classDescription() {
        return LogBuilder.classDescription(this, field.toString());
    }

    /**
     * Get the field Id
     *
     * @return the field Id
     */
    public IntWithDescription get() {
        return field;
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
        if (obj instanceof FormFieldChangeEventParams) {
            return this.field == ((FormFieldChangeEventParams) obj).field;
        }
        if (obj instanceof IntWithDescription) {
            return this.field == (IntWithDescription) obj;
        }
        return false;
    }

    @Override
    public String toString() {
        return field + " change";
    }
}
