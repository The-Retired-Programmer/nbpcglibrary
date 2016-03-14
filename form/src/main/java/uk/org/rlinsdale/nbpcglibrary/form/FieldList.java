/*
 * Copyright (C) 2014-2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 * A General purpose Field for displaying and editing a value which is a simple
 * textual string.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FieldList extends ArrayList<Field> {

    /**
     * Get the list of components which must be displayed.
     * @return the list of components
     */
    public List<JComponent> getComponents() {
        List<JComponent> c = new ArrayList<>();
        this.stream().forEach((field) -> {
            field.getComponents().stream().forEach(component -> {
                c.add((JComponent) component);
            });
        });
        return c;
    }

    /**
     * Update of the fields in this fieldlist from their respective source objects
     */
    protected final void updateRowFieldsFromSource() {
        this.stream().forEach((f) -> {
            f.updateFieldFromSource();
        });
    }
    
    /**
     * Update the source objects from their respective fields in this fieldlist
     */
    protected final void updateRowSourcesFromFields() {
         this.stream().forEach((f) -> {
            f.updateSourceFromField();
        });
     }

     /**
     * Check if all rules in the collection's rule set and each individual field
     * are valid.
     *
     * @return true if all rules are valid
     */
    public final boolean checkRules() {
        boolean valid = true;
        for (Field f : this) {
            if (!f.checkRules()) {
                valid = false;
            }
        }
        return valid;
    }
}
