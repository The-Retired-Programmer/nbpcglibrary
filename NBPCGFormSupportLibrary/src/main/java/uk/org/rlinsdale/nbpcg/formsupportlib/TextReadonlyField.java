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
package uk.org.rlinsdale.nbpcg.formsupportlib;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * A General purpose Field for displaying a value which is a simple textual
 * string.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class TextReadonlyField extends BaseField {

    private final JTextField textfield;

    /**
     * Factory method to create a read-only text field.
     *
     * @param label the label text for the field
     * @return the created read-only text field
     */
    public static TextReadonlyField create(String label) {
        return new TextReadonlyField(label, 20);
    }

    /**
     * Factory method to create a read-only text field.
     *
     * @param label the label text for the field
     * @param size the display size of the field
     * @return the created read-only text field
     */
    public static TextReadonlyField create(String label, int size) {
        return new TextReadonlyField(label, size);
    }

    private TextReadonlyField(String label, int size) {
        super(label);
        textfield = new JTextField();
        textfield.setColumns(size);
        textfield.setEditable(false);
        textfield.setForeground(Color.GRAY);
    }

    @Override
    public final JComponent getComponent() {
        return textfield;
    }

    /**
     * Get the value of this field
     *
     * @return the field value
     */
    public final String get() {
        return textfield.getText().trim();
    }

    /**
     * Set the value of the field
     *
     * @param value the value
     */
    public final void set(String value) {
        textfield.setText(value);
    }
}
