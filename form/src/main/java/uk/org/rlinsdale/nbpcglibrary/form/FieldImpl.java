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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcglibrary.api.BadFormatException;
import uk.org.rlinsdale.nbpcglibrary.common.Callback;
import uk.org.rlinsdale.nbpcglibrary.common.CallbackReport;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 * Abstract Class representing an editable Field on a Form
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> type of the data contained in the field
 * @param <S> type of the source
 */
public abstract class FieldImpl<T, S extends FieldSource<T>> implements Field<T> {

    private final JComponent fieldcomponent;
    private FieldActionListener actionListener;
    private final FieldFocusListener focusListener;
    private T lastvaluesetinfield;
    private boolean inhibitListeneractions = false;
    private final Callback callback;

    /**
     * the initial value of the field
     */
    protected T initialValue;

    /**
     * the source object associated with the field
     */
    protected S source;

    /**
     * the callback error reporter - called when errors detected in data entry
     */
    protected CallbackReport errorReporter = null;

    /**
     * Constructor
     *
     * @param fieldcomponent the field component to be used in field
     * @param source the data source for this field
     * @param initialValue the initial value of the display (or null if source
     * provides this
     * @param callback the callback with is used to inform of source updates
     * from field
     */
    protected FieldImpl(JComponent fieldcomponent, S source, T initialValue, Callback callback) {
        fieldcomponent.addFocusListener(focusListener = new FieldFocusListener());
        this.fieldcomponent = fieldcomponent;
        this.source = source;
        this.callback = callback;
        this.initialValue = initialValue;
    }

    @Override
    public final void setErrorReporter(CallbackReport errorReporter) {
        this.errorReporter = errorReporter;
    }

    public void addSourceRule(Rule rule) {
        source.getRules().addRule(rule);
    }

    /**
     * Get a action Listener which can be associated with the field action event
     *
     * @return the listener
     */
    protected final ActionListener getActionListener() {
        return (actionListener = new FieldActionListener());
    }

    @Override
    public final void updateFieldFromSource() {
        T value = getSourceValue();
        if (!value.equals(lastvaluesetinfield)) {
            lastvaluesetinfield = value;
            insertField(value);
        }
    }

    @Override
    public final void updateFieldFromSource(boolean force) {
        if (force) {
            insertField(getSourceValue());
        } else {
            updateFieldFromSource();
        }
    }

    @Override
    public T getSourceValue() {
        return source.get();
    }

    private void insertField(T value) {
        inhibitListeneractions = true;
        this.lastvaluesetinfield = value;
        setFieldValue(value);
        checkRules();
        inhibitListeneractions = false;
    }

    @Override
    public final void updateSourceFromField() {
        try {
            setSourceValue(getFieldValue());
        } catch (BadFormatException ex) {
            errorReporter.report("Badly Formated Field");
        }
    }

    private void updateSourceFromFieldIfChange(T value) {
        if (value == null) {
            if (lastvaluesetinfield != null) {
                lastvaluesetinfield = value;
                setSourceValue(value);
                updateFieldFromSource(true); // and rewrite the field
                checkRules();
            }
        } else {
            if (!value.equals(lastvaluesetinfield)) {
                lastvaluesetinfield = value;
                setSourceValue(value);
                updateFieldFromSource(true); // and rewrite the field
                checkRules();
            }
        }
    }

    private void setSourceValue(T value) {
        source.set(value);
        if (callback != null) {
            callback.call();
        }
    }

    @Override
    public boolean checkRules() {
        boolean res = source.getRules().checkRules();
        if (errorReporter != null) {
            if (res) {
                errorReporter.clear();
            } else {
                errorReporter.report(source.getRules().getErrorMessages());
            }
        }
        return res;
    }

    @Override
    public List<JComponent> getComponents() {
        List<JComponent> c = new ArrayList<>();
        c.add(fieldcomponent);
        return c;
    }

    @Override
    public String instanceDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class FieldActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!inhibitListeneractions) {
                try {
                    updateSourceFromFieldIfChange(getFieldValue());
                } catch (BadFormatException ex) {
                    if (errorReporter != null) {
                        errorReporter.report("Badly Formated Field");
                    }
                }
            }
        }
    }

    private class FieldFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent fe) {
        }

        @Override
        public void focusLost(FocusEvent fe) {
            if (!inhibitListeneractions) {
                try {
                    updateSourceFromFieldIfChange(getFieldValue());
                } catch (BadFormatException ex) {
                    if (errorReporter != null) {
                        errorReporter.report("Badly Formated Field");
                    }
                }
            }
        }
    }

    @Override
    public final T get() {
        return source.get();
    }

    @Override
    public void set(T value) {
        source.set(value);
        updateFieldFromSource();
    }

    @Override
    public void reset() {
        if (initialValue != null) {
            source.set(initialValue);
        }
        updateFieldFromSource();
    }

    @Override
    public void closeChoices() {
    }

    /**
     * a rule class which checks if field length is greater than or equal to a
     * defined minimum
     */
    protected class MinLengthRule extends Rule {

        private final int min;

        /**
         * constructor
         *
         * @param min the minimum field length
         */
        protected MinLengthRule(int min) {
            super("Too short - minimum length is " + min);
            this.min = min;
        }

        @Override
        public boolean ruleCheck() {
            return ((String) source.get()).length() >= min;
        }
    }

    /**
     * a rule class which checks if field length is less than or equal to a
     * defined maximum
     */
    protected class MaxLengthRule extends Rule {

        private final int max;

        /**
         * constructor
         * @param max the maximum field length
         */
        protected MaxLengthRule(int max) {
            super("Too long - maximum length is " + max);
            this.max = max;
        }

        @Override
        public boolean ruleCheck() {
            return ((String) source.get()).length() <= max;
        }
    }
}
