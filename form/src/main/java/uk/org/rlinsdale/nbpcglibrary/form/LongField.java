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
import uk.org.rlinsdale.nbpcglibrary.common.Callback;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 * A Field for displaying and editing a value which is a Long Value.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class LongField extends EditableFieldImpl<Long, FieldSource<Long>> {

    private final JTextField fieldcomponent;

    /**
     * Constructor
     *
     * @param source the data source for this field
     * @param size the size of the text field object
     * @param min the minimum size of the number entry
     * @param max the maximum size of the number entry
     * @param initialValue the initial value of the display (or null if source
     * provides this
     * @param callback the callback with is used to inform of source updates
     * from field
     */
    public LongField(FieldSource<Long> source, int size, Long min, Long max, Long initialValue, Callback callback) {
        this(new JTextField(), source, size, min, max, initialValue, callback);
    }

    private LongField(JTextField fieldcomponent, FieldSource<Long> source, int size, Long min, Long max, Long initialValue, Callback callback) {
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
    public final Long getFieldValue() throws BadFormatException {
        try {
            return Long.parseLong(fieldcomponent.getText().trim());
        } catch (NumberFormatException ex) {
            throw new BadFormatException("Bad entry format - expected a number");
        }
    }

    @Override
    public final void setFieldValue(Long value) {
        fieldcomponent.setText(value.toString());
    }

    private class MinRule extends Rule {

        private final long min;

        protected MinRule(long min) {
            super("Too small - minimum value is " + min);
            this.min = min;
        }

        @Override
        public boolean ruleCheck() {
            return source.get() >= min;
        }
    }

    private class MaxRule extends Rule {

        private final long max;

        protected MaxRule(long max) {
            super("Too big - maximum value is " + max);
            this.max = max;
        }

        @Override
        public boolean ruleCheck() {
            return source.get() <= max;
        }
    }
}
