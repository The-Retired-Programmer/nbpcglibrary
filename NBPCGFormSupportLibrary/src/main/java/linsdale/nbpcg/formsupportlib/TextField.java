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
import javax.swing.JTextField;
import linsdale.nbpcg.supportlib.FindAction;
import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.Listener;
import linsdale.nbpcg.supportlib.Rule;

/**
 * A General purpose Field for displaying and editing a value which is a simple
 * textual string.
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class TextField extends EditableField {

    private final JTextField textfield;
    private String value = "";
    private final TextActionListener textActionListener = new TextActionListener();
    private final TextFocusListener textFocusListener = new TextFocusListener();
    private int min;
    private int max;
    private FindAction<String> findOthers;

    // set of factory methods to create TextFields
    /**
     * @param field
     * @param label
     * @return
     */
    public static TextField create(IntWithDescription field, String label) {
        return new TextField(field, label, 20, null);
    }

    public static TextField create(IntWithDescription field, String label, int size) {
        return new TextField(field, label, size, null);
    }

    public static TextField create(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener) {
        return new TextField(field, label, 20, listener);
    }

    public static TextField create(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener) {
        return new TextField(field, label, size, listener);
    }

    public static TextField createWithMinMaxRules(IntWithDescription field, String label, int min, int max) {
        return new TextField(field, label, 20, null, min, max);
    }

    public static TextField createWithMinMaxRules(IntWithDescription field, String label, int size, int min, int max) {
        return new TextField(field, label, size, null, min, max);
    }

    public static TextField createWithMinMaxRules(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener, int min, int max) {
        return new TextField(field, label, 20, listener, min, max);
    }

    public static TextField createWithMinMaxRules(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener, int min, int max) {
        return new TextField(field, label, size, listener, min, max);
    }

    public static TextField createWithMinMaxUnqiueRules(IntWithDescription field, String label, int min, int max, FindAction<String> findOthers) {
        return new TextField(field, label, 20, null, min, max, findOthers);
    }

    public static TextField createWithMinMaxUnqiueRules(IntWithDescription field, String label, int size, int min, int max, FindAction<String> findOthers) {
        return new TextField(field, label, size, null, min, max, findOthers);
    }

    public static TextField createWithMinMaxUnqiueRules(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener, int min, int max, FindAction<String> findOthers) {
        return new TextField(field, label, 20, listener, min, max, findOthers);
    }

    public static TextField createWithMinMaxUnqiueRules(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener, int min, int max, FindAction<String> findOthers) {
        return new TextField(field, label, size, listener, min, max, findOthers);
    }

    public static TextField createWithUnqiueRule(IntWithDescription field, String label, FindAction<String> findOthers) {
        return new TextField(field, label, 20, null, findOthers);
    }

    public static TextField createWithUnqiueRule(IntWithDescription field, String label, int size, FindAction<String> findOthers) {
        return new TextField(field, label, size, null, findOthers);
    }

    public static TextField createWithUnqiueRule(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener, FindAction<String> findOthers) {
        return new TextField(field, label, 20, listener, findOthers);
    }

    public static TextField createWithUnqiueRule(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener, FindAction<String> findOthers) {
        return new TextField(field, label, size, listener, findOthers);
    }

    /**
     * Constructor
     *
     * @param field
     * @param label field label
     * @param size size of the value display
     * @param listener
     */
    protected TextField(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener) {
        super(field, label);
        textfield = new JTextField();
        textfield.setColumns(size);
        addListener(listener);
        textfield.addActionListener(textActionListener);
        textfield.addFocusListener(textFocusListener);
    }

    /**
     * Constructor
     *
     * @param id the unique id for this field on the form
     * @param label field label
     * @param size size of the value display
     */
    private TextField(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener, int min, int max) {
        this(field, label, size, listener);
        addMinRule(min);
        addMaxRule(max);
    }

    /**
     * Constructor
     *
     * @param id the unique id for this field on the form
     * @param label field label
     * @param size size of the value display
     */
    private TextField(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener, int min, int max, FindAction<String> findOthers) {
        this(field, label, size, listener, min, max);
        addUniqueRule(findOthers);
    }

    /**
     * Constructor
     *
     * @param id the unique id for this field on the form
     * @param label field label
     * @param size size of the value display
     */
    private TextField(IntWithDescription field, String label, int size, Listener<FormFieldChangeListenerParams> listener, FindAction<String> findOthers) {
        this(field, label, size, listener);
        addUniqueRule(findOthers);
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

    public final void addUniqueRule(FindAction<String> findOthers) {
        this.findOthers = findOthers;
        addRule(new UniqueRule());
    }

    private class UniqueRule extends Rule {

        public UniqueRule() {
            super(label + " is not unique");
        }

        @Override
        protected boolean ruleCheck() {
            return uniqueTest(get());
        }

        private boolean uniqueTest(String val) {
            for (String s : findOthers.find()) {
                if (s.equals(val)) {
                    return false;
                }
            }
            return true;
        }
    }

    private class TextActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            update(get());
        }
    }

    private class TextFocusListener implements FocusListener {

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
        return textfield;
    }

    /**
     * Get the value of this field
     *
     * @return the field value
     */
    public final String get() {
        return textfield.getText().trim();
    }

    /**
     * Set the value of the field
     *
     * @param value the value
     */
    public final void set(String value) {
        textfield.removeActionListener(textActionListener);
        textfield.removeFocusListener(textFocusListener);
        this.value = value;
        textfield.setText(value);
        textfield.addActionListener(textActionListener);
        textfield.addFocusListener(textFocusListener);
    }

    protected final void update(String newvalue) {
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