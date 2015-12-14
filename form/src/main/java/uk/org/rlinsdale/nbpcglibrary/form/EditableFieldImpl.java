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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
 */
public abstract class EditableFieldImpl<T> extends FieldImpl implements EditableField<T> {

    private FieldActionListener actionListener;
    private final FieldFocusListener focusListener;
    private T lastvaluesetinfield;
    private boolean inhibitListeneractions = false;
    private final Callback callback;
    protected T initialValue;
    protected FieldSource<T> source;
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
    protected EditableFieldImpl(JComponent fieldcomponent, FieldSource<T> source, T initialValue, Callback callback) {
        super(fieldcomponent);
        fieldcomponent.addFocusListener(focusListener = new FieldFocusListener());
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
        if (!value.equals(lastvaluesetinfield)) {
            lastvaluesetinfield = value;
            setSourceValue(value);
            updateFieldFromSource(true); // and rewrite the field
            checkRules();
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

//    @Override
//    public void setFieldValue(T value) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public T getFieldValue() throws BadFormatException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

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

    protected class MinLengthRule extends Rule {

        private final int min;

        protected MinLengthRule(int min) {
            super("Too short - minimum length is " + min);
            this.min = min;
        }

        @Override
        public boolean ruleCheck() {
            return ((String) source.get()).length() >= min;
        }
    }

    protected class MaxLengthRule extends Rule {

        private final int max;

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
