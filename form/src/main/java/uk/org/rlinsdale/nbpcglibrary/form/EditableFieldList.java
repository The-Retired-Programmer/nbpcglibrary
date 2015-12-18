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

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * A General purpose Field for displaying and editing a value which is a simple
 * textual string.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class EditableFieldList extends ArrayList<EditableField> implements Field {

    
    public List<JComponent> getComponents() {
        List<JComponent> c = new ArrayList<>();
        this.stream().forEach((field) -> {
            field.getComponents().stream().forEach((component) -> {
                c.add(component);
            });
        });
        return c;
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this);
    }
    
    protected final void updateRowFieldsFromSource() {
        this.stream().forEach((f) -> {
            f.updateFieldFromSource();
        });
    
    }
    
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
        for (EditableField f : this) {
            if (!f.checkRules()) {
                valid = false;
            }
        }
        return valid;
    }
}
