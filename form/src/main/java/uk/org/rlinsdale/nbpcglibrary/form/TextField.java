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

import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * A General purpose Field for displaying and editing a value which is a simple
 * textual string.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class TextField extends EditableField<String> {

    private final JTextField field;

    /**
     * Constructor
     *
     * @param label label to be displayed with field
     * @param size the size of the text field object
     */
    public TextField(String label, int size) {
        this(label, new JTextField(), size, null);
    }
    
     /**
     * Constructor
     *
     * @param label label to be displayed with field
     * @param size the size of the text field object
     * @param additionalfield the additional field obejct to be added after the
     * main field object
     */
    protected TextField(String label, int size, JComponent additionalfield) {
        this(label, new JTextField(), size, additionalfield);
    }

    private TextField(String label, JTextField field, int size, JComponent additionalfield) {
        super(label, field, additionalfield);
        this.field = field;
        field.setColumns(size);
        field.addActionListener(getActionListener());
    }

    @Override
    protected final String getFieldValue() {
        return field.getText().trim();
    }

    @Override
    protected final void setFieldValue(String value) {
        field.setText(value);
    }
}
