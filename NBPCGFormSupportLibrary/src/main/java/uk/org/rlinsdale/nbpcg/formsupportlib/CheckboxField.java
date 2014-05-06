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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcg.supportlib.IntWithDescription;
import uk.org.rlinsdale.nbpcg.supportlib.Listener;

/**
 * A General purpose Field for displaying and editing a value which is a simple
 * boolean using a checkbox.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class CheckboxField extends EditableField {

    private final JCheckBox checkbox;
    private boolean value;
    private final CheckboxActionListener checkboxActionListener = new CheckboxActionListener();
    private final CheckboxFocusListener checkboxFocusListener = new CheckboxFocusListener();

    /**
     * Factory method to create a checkbox field
     * 
     * @param field the field id
     * @param label the label text for the field
     * @return the created checkbox field
     */
    public static CheckboxField create(IntWithDescription field, String label) {
        return new CheckboxField(field, label, null);
    }

    /**
     * Factory method to create a checkbox field
     *
     * @param field the field id
     * @param label the label text for the field
     * @param listener the listener for changes to field value
     * @return the created checkbox field
     */
    public static CheckboxField create(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener) {
        return new CheckboxField(field, label, listener);
    }

    private CheckboxField(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener) {
        super(field, label);
        checkbox = new JCheckBox("", false);
        addListener(listener);
        checkbox.addActionListener(checkboxActionListener);
        checkbox.addFocusListener(checkboxFocusListener);
    }

    private class CheckboxActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            update(get());
        }
    }

    private class CheckboxFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent fe) {
        }

        @Override
        public void focusLost(FocusEvent fe) {
            update(get());
        }
    }

    @Override
    public final JComponent getComponent() {
        return checkbox;
    }

    /**
     * Get the value of this field
     *
     * @return the field value
     */
    public final boolean get() {
        return checkbox.isSelected();
    }

    /**
     * Set the value of the field
     *
     * @param value the value
     */
    public final void set(boolean value) {
        checkbox.removeActionListener(checkboxActionListener);
        checkbox.removeFocusListener(checkboxFocusListener);
        checkbox.setSelected(value);
        this.value = value;
        checkbox.addActionListener(checkboxActionListener);
        checkbox.addFocusListener(checkboxFocusListener);
    }

    /**
     * Update the value of the field, firing the listener action if the value
     * changes.
     * 
     * @param newvalue the new value
     */
    public final void update(boolean newvalue) {
        if (newvalue != value) {
            set(newvalue);
            fireChanged();
        }
    }
}
