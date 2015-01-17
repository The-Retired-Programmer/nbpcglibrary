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
import javax.swing.JPasswordField;

/**
 * A Field to handle password entry.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class PasswordField extends EditableStringField {

    private JPasswordField passwordfield;

    /**
     * Constructor
     *
     * @param backingObject the backing Object
     * @param label field label
     * @param size size of the value display
     */
    public PasswordField(EditableFieldBackingObject<String> backingObject, String label, int size) {
        this(backingObject, label, size, new JPasswordField());
    }

    private PasswordField(EditableFieldBackingObject<String> backingObject, String label, int size, JPasswordField passwordfield) {
        super(backingObject, label, passwordfield);
        this.passwordfield = passwordfield;
        this.passwordfield.setColumns(size);
        setField(backingObject.get());
    }

    /**
     * Constructor
     *
     * @param backingObject the backing Object
     * @param label field label
     */
    public PasswordField(EditableFieldBackingObject<String> backingObject, String label) {
        this(backingObject, label, 20);
    }

    @Override
    final String get() {
        return new String(passwordfield.getPassword());
    }

    @Override
    final void set(String value) {
        passwordfield.setText(value);
    }

    @Override
    final void addActionListener(ActionListener listener) {
        passwordfield.addActionListener(listener);
    }

    @Override
    final void removeActionListener(ActionListener listener) {
        passwordfield.removeActionListener(listener);
    }
}
