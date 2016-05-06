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

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.JCheckBox;

/**
 * A Field for displaying and editing a value which is a simple boolean using a
 * checkbox.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class CheckboxField extends FieldView<Boolean> {
    
    private final JCheckBox fieldcomponent;

    /**
     * Constructor
     *
     * @param listener the itemlistener to be associated with this field
     */
    public CheckboxField(ItemListener listener) {
        this(new JCheckBox(), listener);
    }
    
    /**
     * Constructor
     */
    public CheckboxField() {
        this(new JCheckBox(), null);
    }
    
    private CheckboxField(JCheckBox fieldcomponent, ItemListener listener) {
        super(fieldcomponent);
        this.fieldcomponent = fieldcomponent;
        if (listener != null ) {
            fieldcomponent.addItemListener(listener);
        }
    }

    @Override
    public final Boolean get() {
        return fieldcomponent.isSelected();
    }

    @Override
    public final void set(Boolean value) {
        fieldcomponent.setSelected(value);
    }
    
    @Override
    public void addActionListener(ActionListener listener) {
        fieldcomponent.addActionListener(listener);
    }
}
