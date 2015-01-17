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

import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/**
 * A General purpose Field for displaying and editing a value which is a simple
 * boolean using a checkbox.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class CheckboxField extends EditableField<Boolean> {

    private final JCheckBox checkbox;
    private final CheckboxFieldBackingObject backingObject;

    /**
     * Constructor
     *
     * @param backingObject the backing object
     * @param label field label
     */
    public CheckboxField(CheckboxFieldBackingObject backingObject, String label) {
        this(backingObject, label, new JCheckBox("", false));
    }
    
    public CheckboxField(CheckboxFieldBackingObject backingObject, String label, JCheckBox checkbox) {
        super(backingObject, label, checkbox);
        this.checkbox = checkbox;
        this.backingObject = backingObject;
        setField(backingObject.get());
    }

    @Override
    protected Boolean get() {
        return checkbox.isSelected();
    }

    @Override
    void addActionListener(ActionListener listener) {
        checkbox.addActionListener(listener);
    }

    @Override
    void removeActionListener(ActionListener listener) {
        checkbox.removeActionListener(listener);
    }

    @Override
    void set(Boolean value) {
        checkbox.setSelected(value);
    }

    @Override
    void updateIfChange(Boolean value) {
        if (!value.equals(lastvaluesetinfield)) {
            lastvaluesetinfield = value;
            if (checkRules()) {
                backingObject.set(value);
            }
        }
    }
}
