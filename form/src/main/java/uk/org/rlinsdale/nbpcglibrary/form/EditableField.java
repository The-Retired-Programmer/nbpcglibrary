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

import uk.org.rlinsdale.nbpcglibrary.api.BadFormatException;
import uk.org.rlinsdale.nbpcglibrary.common.CallbackReport;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 * Interface for an editable Field on a Form
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> type of the data contained in the field
 */
public interface EditableField<T> extends Field {

//    /**
//     * Define the callback for reporting errors and associated errormessages
//     * 
//     * @param errorReporter  the callback object
//     */
//    void setErrorReporter(CallbackReport errorReporter);
    /**
     * Get the value of the field object (not the actual JComponent).
     *
     * @return the value
     */
    public T get();

    /**
     * Get the value from the source
     *
     * @return the value
     */
    public T getSourceValue();

    /**
     * Add a rule to this set of source rules
     *
     * @param rule the rule to be added
     */
    public void addSourceRule(Rule rule);

    /**
     * Update the field from the source. Only update field if source value
     * appears to have changed since last setting this field.
     */
    public void updateFieldFromSource();

    /**
     * Update the field from the source
     *
     * @param force force the update even if the value does not appear to have
     * changed
     */
    public void updateFieldFromSource(boolean force);

    /**
     * Update the source from the field. Will not do the update if the field
     * value is not correctly formatted.
     */
    public void updateSourceFromField();

    /**
     * Set a value into the Field
     *
     * @param value the value to be inserted into the Field
     */
    public void setFieldValue(T value);

    /**
     * Get a value from the field
     *
     * @return the value of the field
     * @throws BadFormatException if field is not valid format for input type
     * required.
     */
    public T getFieldValue() throws BadFormatException;

    /**
     * Define the callback for reporting errors and associated errormessages
     *
     * @param errorReporter the callback object
     */
    public void setErrorReporter(CallbackReport errorReporter);

    /**
     * finish managing the choices text
     */
    public void closeChoices();

    /**
     * Reset the value of the field object to the initial field value (will
     * cause the actual field component to be updated).
     */
    public void reset();
    
    /**
     * Set the value of the field object (will cause
     * the actual field component to be updated).
     *
     * @param value the value
     */
    public void set(T value);
    
     /**
     * Check if all rules in the field's rule set are valid, and update error
     * markers and error messages on the form.
     *
     * @return true if all rules are valid
     */
    public boolean checkRules();
}
