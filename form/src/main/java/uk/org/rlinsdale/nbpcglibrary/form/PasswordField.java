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
import javax.swing.JPasswordField;

/**
 * A Field to handle password entry.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class PasswordField extends EditableField<String> {

    private JPasswordField field;

    /**
     * Constructor
     *
     * @param label field label
     * @param size the field size
     */
    public PasswordField(String label, int size) {
        this(label, new JPasswordField(), size, null);
    }
    
    /**
     * Constructor
     *
     * @param label field label
     * @param size the field size
     * @param additionalfield the additional field to be display 
     */
    protected PasswordField(String label, int size, JComponent additionalfield) {
        this(label, new JPasswordField(), size, additionalfield);
    }

    private PasswordField(String label, JPasswordField field, int size, JComponent additionalfield) {
        super(label, field, additionalfield);
        this.field = field;
        this.field.setColumns(size);
        field.addActionListener(getActionListener());
    }

    @Override
    protected final String getFieldValue() {
        return new String(field.getPassword());
    }

    @Override
    protected final void setFieldValue(String value) {
        field.setText(value);
    }
}
