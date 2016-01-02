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
 * A Field for displaying and editing a value which is an Integer Value.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class IntegerField extends EditableFieldImpl<Integer, FieldSource<Integer>> {

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
    public IntegerField(FieldSource<Integer> source, int size, Integer min, Integer max, Integer initialValue, Callback callback) {
        this(new JTextField(), source, size, min, max, initialValue, callback);
    }

    private IntegerField(JTextField fieldcomponent, FieldSource<Integer> source, int size, Integer min, Integer max, Integer initialValue, Callback callback) {
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
    public final Integer getFieldValue() throws BadFormatException {
        try {
            return Integer.parseInt(fieldcomponent.getText().trim());
        } catch (NumberFormatException ex) {
            throw new BadFormatException("Bad entry format - expected a number");
        }
    }

    @Override
    public final void setFieldValue(Integer value) {
        fieldcomponent.setText(value.toString());
    }

    private class MinRule extends Rule {

        private final int min;

        protected MinRule(int min) {
            super("Too small - minimum value is " + min);
            this.min = min;
        }

        @Override
        public boolean ruleCheck() {
            return source.get() >= min;
        }
    }

    private class MaxRule extends Rule {

        private final int max;

        protected MaxRule(int max) {
            super("Too big - maximum value is " + max);
            this.max = max;
        }

        @Override
        public boolean ruleCheck() {
            return source.get() <= max;
        }
    }
}
