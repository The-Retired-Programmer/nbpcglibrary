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
package uk.org.rlinsdale.nbpcglibrary.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import uk.org.rlinsdale.nbpcglibrary.common.IntWithDescription;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 * A Field to handle password entry.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class PasswordField extends EditableField {

    private JPasswordField passwordfield;
    private String value = "";
    private final PasswordActionListener passwordActionListener = new PasswordActionListener();
    private final PasswordFocusListener passwordFocusListener = new PasswordFocusListener();
    private int min;
    private int max;

    /**
     * Factory method to create a password field.
     * 
     * @param field the field id
     * @param label the label text for the field
     * @return the created password field
     */
    public static PasswordField create(IntWithDescription field, String label) {
        return new PasswordField(field, label, 20, null);
    }

    /**
     * Factory method to create a password field.
     * 
     * @param field the field id
     * @param label the label text for the field
     * @param size the display size of the field
     * @return the created password field
     */
    public static PasswordField create(IntWithDescription field, String label, int size) {
        return new PasswordField(field, label, size, null);
    }

    /**
     * Factory method to create a password field.
     * 
     * @param field the field id
     * @param label the label text for the field
     * @param listener the listener for changes to field value
     * @return the created password field
     */
    public static PasswordField create(IntWithDescription field, String label, Listener<FormFieldChangeEventParams> listener) {
        return new PasswordField(field, label, 20, listener);
    }

    /**
     * Factory method to create a password field.
     * 
     * @param field the field id
     * @param label the label text for the field
     * @param size the display size of the field
     * @param listener the listener for changes to field value
     * @return the created password field
     */
    public static PasswordField create(IntWithDescription field, String label, int size, Listener<FormFieldChangeEventParams> listener) {
        return new PasswordField(field, label, size, listener);
    }

    /**
     * Factory method to create a password field.
     * 
     * @param field the field id
     * @param label the label text for the field
     * @param min minimum number of characters to enter
     * @param max maximum number of characters to enter
     * @return the created password field
     */
    public static PasswordField createWithMinMaxRules(IntWithDescription field, String label, int min, int max) {
        return new PasswordField(field, label, 20, null, min, max);
    }

    /**
     * Factory method to create a password field.
     * 
     * @param field the field id
     * @param label the label text for the field
     * @param size the display size of the field
     * @param min minimum number of characters to enter
     * @param max maximum number of characters to enter
     * @return the created password field
     */
    public static PasswordField createWithMinMaxRules(IntWithDescription field, String label, int size, int min, int max) {
        return new PasswordField(field, label, size, null, min, max);
    }

    /**
     * Factory method to create a password field.
     * 
     * @param field the field id
     * @param label the label text for the field
     * @param listener the listener for changes to field value
     * @param min minimum number of characters to enter
     * @param max maximum number of characters to enter
     * @return the created password field
     */
    public static PasswordField createWithMinMaxRules(IntWithDescription field, String label, Listener<FormFieldChangeEventParams> listener, int min, int max) {
        return new PasswordField(field, label, 20, listener, min, max);
    }

    /**
     * Factory method to create a password field.
     * 
     * @param field the field id
     * @param label the label text for the field
     * @param size the display size of the field
     * @param listener the listener for changes to field value
     * @param min minimum number of characters to enter
     * @param max maximum number of characters to enter
     * @return the created password field
     */
    public static PasswordField createWithMinMaxRules(IntWithDescription field, String label, int size, Listener<FormFieldChangeEventParams> listener, int min, int max) {
        return new PasswordField(field, label, size, listener, min, max);
    }

    private PasswordField(IntWithDescription field, String label, int size, Listener<FormFieldChangeEventParams> listener) {
        super(field, label);
        passwordfield = new JPasswordField();
        passwordfield.setColumns(size);
        max = size;
        addListener(listener);
        passwordfield.addActionListener(passwordActionListener);
        passwordfield.addFocusListener(passwordFocusListener);
    }

    private PasswordField(IntWithDescription field, String label, int size, Listener<FormFieldChangeEventParams> listener, int min, int max) {
        this(field, label, size, listener);
        addMinRule(min);
        addMaxRule(max);
    }

    /**
     * Add a minimum entry length rule to this field
     *
     * @param len minimum number of characters to enter
     */
    public final void addMinRule(int len) {
        min = len;
        addRule(new StringMinRule());
    }

    private class StringMinRule extends Rule {

        public StringMinRule() {
            super(label + " too short");
        }

        @Override
        protected boolean ruleCheck() {
            return get().length() >= min;
        }
    }

    /**
     * Add a maximum entry length rule to this field
     *
     * @param len maximum number of characters to enter
     */
    public final void addMaxRule(int len) {
        max = len;
        addRule(new StringMaxRule());
    }

    private class StringMaxRule extends Rule {

        public StringMaxRule() {
            super(label + " too long");
        }

        @Override
        protected boolean ruleCheck() {
            return get().length() <= max;
        }
    }

    private class PasswordActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            update(get());
        }
    }

    private class PasswordFocusListener implements FocusListener {

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
        return passwordfield;
    }

    /**
     * Get the value of this field
     *
     * @return the field value
     */
    public final String get() {
        return new String(passwordfield.getPassword());
    }

    /**
     * Set the value of the field
     *
     * @param value the value
     */
    public final void set(String value) {
        passwordfield.removeActionListener(passwordActionListener);
        passwordfield.removeFocusListener(passwordFocusListener);
        this.value = value;
        passwordfield.setText(value);
        passwordfield.addActionListener(passwordActionListener);
        passwordfield.addFocusListener(passwordFocusListener);
    }

    /**
     * Update the value of the field, firing the listener action if the value
     * changes.
     * 
     * @param newvalue the new value
     */
    public final void update(String newvalue) {
        boolean fire = false;
        if (!newvalue.equals(value)) {
            set(newvalue);
            fire = true;
        }
        boolean checkresult = checkRules();
        if (fire || (!checkresult)) {
            fireChanged();
        }
    }
}
