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

import java.awt.Color;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * A General purpose Field for displaying a value which is a simple textual
 * string.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class TextReadonlyField extends FieldImpl {
    
    private JLabel labelcomponent;

    /**
     * Constructor
     *
     * @param size size of the value display
     * @param initialValue the field initial value
     */
    public TextReadonlyField(int size, String initialValue) {
        this(new JTextField(), size, initialValue);
    }
    
    private TextReadonlyField(JTextField fieldcomponent, int size, String initialValue) {
        super(fieldcomponent);
        fieldcomponent.setColumns(size);
        fieldcomponent.setEditable(false);
        fieldcomponent.setForeground(Color.GRAY);
        fieldcomponent.setText(initialValue);
    }
    
     /**
     * Constructor
     *
     * @param label the label to be associated with this field
     * @param size size of the value display
     * @param initialValue the field initial value
     */
    public TextReadonlyField(String label, int size, String initialValue) {
        this(new JLabel(), label, new JTextField(), size, initialValue);
    }
    
    private TextReadonlyField(JLabel labelcomponent, String labeltext, JTextField fieldcomponent, int size, String initialValue) {
        super(fieldcomponent);
        fieldcomponent.setColumns(size);
        fieldcomponent.setEditable(false);
        fieldcomponent.setForeground(Color.GRAY);
        fieldcomponent.setText(initialValue);
        labelcomponent.setText(labeltext);
        this.labelcomponent = labelcomponent;
    }
    
    @Override
    public List<JComponent> getComponents() {
        List<JComponent> c = super.getComponents();
        if (labelcomponent != null) {
            c.add(0, labelcomponent);
        }
        return c;
    }
}
