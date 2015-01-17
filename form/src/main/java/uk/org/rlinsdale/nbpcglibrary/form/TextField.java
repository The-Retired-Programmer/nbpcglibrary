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
import javax.swing.JTextField;

/**
 * A General purpose Field for displaying and editing a value which is a simple
 * textual string.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class TextField extends EditableStringField {
    
    private final JTextField textfield;

    /**
     * Constructor
     *
     * @param backingObject the backingObject
     * @param label field label
     * @param size size of the value display
     */
    public TextField(EditableFieldBackingObject<String> backingObject, String label, int size) {
        this(backingObject, label, size, new JTextField());
    }

    /**
     * Constructor
     *
     * @param backingObject the backingObject
     * @param label field label
     */
    public TextField(EditableFieldBackingObject<String> backingObject, String label) {
        this(backingObject, label, 20);
    }
    
    private TextField(EditableFieldBackingObject<String> backingObject, String label, int size, JTextField textfield) {
        super(backingObject, label, textfield);
        this.textfield = textfield;
        textfield.setColumns(size);
        setField(backingObject.get());
    }
    
    @Override
    protected final String get() {
        return textfield.getText().trim();
    }
    
    @Override
    protected final void set(String value) {
        textfield.setText(value);
    }
    
    @Override
    protected final void addActionListener(ActionListener listener) {
        textfield.addActionListener(listener);
    }
    
    @Override
    protected final void removeActionListener(ActionListener listener) {
        textfield.removeActionListener(listener);
    }
}
