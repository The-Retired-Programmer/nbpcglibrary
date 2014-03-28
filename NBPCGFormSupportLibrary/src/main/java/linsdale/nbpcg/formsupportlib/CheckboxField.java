/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package linsdale.nbpcg.formsupportlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.Listener;

/**
 * A General purpose Field for displaying and editing a value which is a simple
 * boolean using a checkbox.
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class CheckboxField extends EditableField {

    private final JCheckBox checkbox;
    private boolean value;
    private final CheckboxActionListener checkboxActionListener = new CheckboxActionListener();
    private final CheckboxFocusListener checkboxFocusListener = new CheckboxFocusListener();

    public static CheckboxField create(IntWithDescription field, String label) {
        return new CheckboxField(field, label, null);
    }

    public static CheckboxField create(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener) {
        return new CheckboxField(field, label, listener);
    }

    /**
     * Constructor
     *
     * @param id the unique id for this field on the form
     * @param label field label
     */
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

    public final void update(boolean newvalue) {
        if (newvalue != value) {
            set(newvalue);
            fireChanged();
        }
    }
}
