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
import uk.org.rlinsdale.nbpcglibrary.api.Timestamp;
import uk.org.rlinsdale.nbpcglibrary.common.Callback;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 * A Field for displaying and editing a value which is a Timestamp Value.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class DatetimeField extends EditableFieldImpl<Timestamp> {

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
    public DatetimeField(FieldSource<Timestamp> source, int size, Timestamp min, Timestamp max, Timestamp initialValue, Callback callback) {
        this(new JTextField(), source, size, min, max, initialValue, callback);
    }

    private DatetimeField(JTextField fieldcomponent, FieldSource<Timestamp> source, int size, Timestamp min, Timestamp max, Timestamp initialValue, Callback callback) {
        super(fieldcomponent, source, initialValue, callback);
        this.fieldcomponent = fieldcomponent;
        fieldcomponent.setColumns(size);
        fieldcomponent.addActionListener(getActionListener());
    }

    @Override
    public final Timestamp getFieldValue() throws BadFormatException {
        return new Timestamp(fieldcomponent.getText().trim());
    }

    @Override
    public final void setFieldValue(Timestamp value) {
        fieldcomponent.setText(value.toString());
    }

    private class MaxRule extends Rule {

        private final Timestamp max;

        protected MaxRule(Timestamp max) {
            super("Too late - must be previous/equal to " + max);
            this.max = max;
        }

        @Override
        public boolean ruleCheck() {
            return source.get().compareTo(max) != 1;
        }
    }

    private class MinRule extends Rule {

        private final Timestamp min;

        protected MinRule(Timestamp min) {
            super("Too early - must be later/equal to " + min);
            this.min = min;
        }

        @Override
        public boolean ruleCheck() {
            return source.get().compareTo(min) != -1;
        }
    }
}
