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

import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import uk.org.rlinsdale.nbpcglibrary.common.Callback;

/**
 * A Field for displaying and editing a value which is a simple boolean using a
 * checkbox.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class CheckboxField extends EditableFieldImpl<Boolean, FieldSource<Boolean>> {
    
    private final JCheckBox fieldcomponent;

    /**
     * Constructor
     *
     * @param source the data source for this field
     * @param initialValue the initial value of the display (or null if source
     * provides this
     * @param listener the itemlistener to be associated with this field
     * @param callback the callback with is used to inform of source updates
     * from field
     */
    public CheckboxField(FieldSource<Boolean> source, Boolean initialValue, ItemListener listener, Callback callback) {
        this(new JCheckBox(), source, initialValue, listener, callback);
    }
    
    private CheckboxField(JCheckBox fieldcomponent, FieldSource<Boolean> source, Boolean initialValue, ItemListener listener, Callback callback) {
        super(fieldcomponent, source, initialValue, callback);
        this.fieldcomponent = fieldcomponent;
        if (listener != null ) {
            fieldcomponent.addItemListener(listener);
        }
        fieldcomponent.addActionListener(getActionListener());
        reset();
    }

    @Override
    public final Boolean getFieldValue() {
        return fieldcomponent.isSelected();
    }

    @Override
    public final void setFieldValue(Boolean value) {
        fieldcomponent.setSelected(value);
    }
}
