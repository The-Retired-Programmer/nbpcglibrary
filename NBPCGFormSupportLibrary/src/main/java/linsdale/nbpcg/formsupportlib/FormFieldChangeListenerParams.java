/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package linsdale.nbpcg.formsupportlib;

import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.ListenerParams;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class FormFieldChangeListenerParams implements ListenerParams {

    private final IntWithDescription field;

    public FormFieldChangeListenerParams(IntWithDescription field) {
        this.field = field;
    }

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
        if (obj instanceof FormFieldChangeListenerParams) {
            return this.field == ((FormFieldChangeListenerParams) obj).field;
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
