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

import javax.swing.JTextField;
import uk.org.rlinsdale.nbpcglibrary.common.Callback;

/**
 * A General purpose Field for displaying and editing a value which is a simple
 * textual string.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class TextField extends FieldImpl<String, FieldSource<String>> {

    private final JTextField fieldcomponent;
    
    /**
     * Constructor
     *
     * @param source the data source for this field
     * @param size the size of the text field object
     * @param min the minimum valid length of the text entry
     * @param max the maximum valid length of the text entry
     * @param initialValue the initial value of the display (or null if source
     * provides this
     * @param callback the callback with is used to inform of source updates
     * from field
     */
    public TextField(FieldSource<String> source, int size, Integer min, Integer max, String initialValue, Callback callback) {
         this(new JTextField(), source, size, min, max, initialValue, callback);
    }
    
    private TextField(JTextField fieldcomponent, FieldSource<String> source, int size, Integer min, Integer max, String initialValue, Callback callback) {
        super(fieldcomponent, source, initialValue, callback);
        this.fieldcomponent = fieldcomponent;
        fieldcomponent.setColumns(size);
        fieldcomponent.addActionListener(getActionListener());
        if (min != null) {
            source.getRules().addRule(new MinLengthRule(min));
        }
        if (max != null) {
            source.getRules().addRule(new MaxLengthRule(max));
        }
        reset();
    }

    @Override
    public final String getFieldValue() {
        return fieldcomponent.getText().trim();
    }

    @Override
    public final void setFieldValue(String value) {
        fieldcomponent.setText(value);
    }
}
