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
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.Listener;
import linsdale.nbpcg.supportlib.Rule;

/**
 * A Field to handle password entry.
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class PasswordField extends EditableField {

    private JPasswordField passwordfield;
    private String value = "";
    private final PasswordActionListener passwordActionListener = new PasswordActionListener();
    private final PasswordFocusListener passwordFocusListener = new PasswordFocusListener();
    private int min;
    private int max;

    public static PasswordField create(IntWithDescription field, String label) {
        return new PasswordField(field, label, 20, null);
    }

    public static PasswordField create(IntWithDescription field, String label, int size) {
        return new PasswordField(field, label, size, null);
    }

    public static PasswordField create(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener) {
        return new PasswordField(field, label, 20, listener);
    }

    public static PasswordField create(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener) {
        return new PasswordField(field, label, size, listener);
    }

    public static PasswordField createWithMinMaxRules(IntWithDescription field, String label, int min, int max) {
        return new PasswordField(field, label, 20, null, min, max);
    }

    public static PasswordField createWithMinMaxRules(IntWithDescription field, String label, int size, int min, int max) {
        return new PasswordField(field, label, size, null, min, max);
    }

    public static PasswordField createWithMinMaxRules(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener, int min, int max) {
        return new PasswordField(field, label, 20, listener, min, max);
    }

    public static PasswordField createWithMinMaxRules(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener, int min, int max) {
        return new PasswordField(field, label, size, listener, min, max);
    }

    /**
     * Constructor
     *
     * @param id the unique id for this field on the form
     * @param label field label
     * @param size size of the value display
     */
    private PasswordField(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener) {
        super(field, label);
        passwordfield = new JPasswordField();
        passwordfield.setColumns(size);
        max = size;
        addListener(listener);
        passwordfield.addActionListener(passwordActionListener);
        passwordfield.addFocusListener(passwordFocusListener);
    }

    /**
     * Constructor
     *
     * @param id the unique id for this field on the form
     * @param label field label
     * @param size size of the value display
     */
    private PasswordField(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener, int min, int max) {
        this(field, label, size, listener);
        addMinRule(min);
        addMaxRule(max);
    }

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
