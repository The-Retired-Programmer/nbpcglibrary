/*
 * Copyright (C) 2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import javax.swing.JTextField;
import uk.org.rlinsdale.nbpcglibrary.api.BadFormatException;
import uk.org.rlinsdale.nbpcglibrary.api.DateOnly;
import uk.org.rlinsdale.nbpcglibrary.common.Callback;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 * A Field for displaying and editing a value which is a DateOnly Value.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class DateField extends EditableFieldImpl<DateOnly> {

    private final JTextField fieldcomponent;

    /**
     * Constructor
     *
     * @param source the data source for this field
     * @param size the size of the text field object
     * @param min the minimum valid length of the text entry
     * @param max the maximum valid length of the text entry
     * @param initialValue the initial value of the display (or null if source
     * provides this
     * @param callback the callback with is used to inform of source updates
     * from field
     */
    public DateField(FieldSource<DateOnly> source, int size, DateOnly min, DateOnly max, DateOnly initialValue, Callback callback) {
        this(new JTextField(), source, size, min, max, initialValue, callback);
    }

    private DateField(JTextField fieldcomponent, FieldSource<DateOnly> source, int size, DateOnly min, DateOnly max, DateOnly initialValue, Callback callback) {
        super(fieldcomponent, source, initialValue, callback);
        this.fieldcomponent = fieldcomponent;
        fieldcomponent.setColumns(size);
        fieldcomponent.addActionListener(getActionListener());
        if (min != null) {
            source.getRules().addRule(new MinRule(min));
        }
        if (max != null) {
            source.getRules().addRule(new MaxRule(max));
        }
        reset();
    }

    @Override
    public final DateOnly getFieldValue() throws BadFormatException {
        return new DateOnly(fieldcomponent.getText().trim());
    }

    @Override
    public final void setFieldValue(DateOnly value) {
        fieldcomponent.setText(value.toString());
    }

    private class MaxRule extends Rule {

        private final DateOnly max;

        protected MaxRule(DateOnly max) {
            super("Too late - must be previous/equal to " + max);
            this.max = max;
        }

        @Override
        public boolean ruleCheck() {
            return source.get().compareTo(max) != 1;
        }
    }

    private class MinRule extends Rule {

        private final DateOnly min;

        protected MinRule(DateOnly min) {
            super("Too early - must be later/equal to " + min);
            this.min = min;
        }

        @Override
        public boolean ruleCheck() {
            return source.get().compareTo(min) != -1;
        }
    }
}
