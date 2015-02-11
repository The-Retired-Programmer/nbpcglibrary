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
import javax.swing.JComponent;
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
    public TextField(FieldBackingObject<String> backingObject, String label, int size) {
        this(backingObject, label, new JTextField(), null, size);
    }

    /**
     * Constructor
     *
     * @param backingObject the backingObject
     * @param label field label
     */
    public TextField(FieldBackingObject<String> backingObject, String label) {
        this(backingObject, label, 20);
    }
    
    /**
     * Constructor
     *
     * @param backingObject the backingObject
     * @param label field label
     * @param size size of the value display
     * @param additionalComponent additional component to additional to right of text field
     */
    public TextField(FieldBackingObject<String> backingObject, String label, int size, JComponent additionalComponent) {
        this(backingObject, label, new JTextField(), additionalComponent, size);
    }

    /**
     * Constructor
     *
     * @param backingObject the backingObject
     * @param label field label
     * @param additionalComponent additional component to additional to right of text field
     */
    public TextField(FieldBackingObject<String> backingObject, String label, JComponent additionalComponent) {
        this(backingObject, label, 20, additionalComponent);
    }
    
    private TextField(FieldBackingObject<String> backingObject, String label, JTextField textfield, JComponent additionalfield, int size) {
        super(backingObject, label, textfield, additionalfield);
        this.textfield = textfield;
        textfield.setColumns(size);
        updateFieldFromBackingObject();
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
