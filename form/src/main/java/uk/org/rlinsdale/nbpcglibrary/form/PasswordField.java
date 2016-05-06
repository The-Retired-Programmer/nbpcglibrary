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
import java.util.List;
import javax.swing.JPasswordField;

/**
 * A Field to handle password entry.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class PasswordField extends FieldView<String> {

    private final JPasswordField fieldcomponent;

    /**
     * Constructor
     */
    public PasswordField() {
        this(new JPasswordField(), 20);
    }
    
    /**
     * Constructor
     *
     * @param size the size of the text field object
     */
    public PasswordField(int size) {
        this(new JPasswordField(), size);
    }

    private PasswordField(JPasswordField fieldcomponent, int size) {
        super(fieldcomponent);
        this.fieldcomponent = fieldcomponent;
        fieldcomponent.setColumns(size);
    }

    @Override
    public final String get() {
        return new String(fieldcomponent.getPassword());
    }

    @Override
    public final void set(String value) {
        fieldcomponent.setText(value);
    }
    
    @Override
    public void addActionListener(ActionListener listener) {
        fieldcomponent.addActionListener(listener);
    }
}
