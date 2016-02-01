/*
 * Copyright (C) 2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.math.BigDecimal;
import javax.swing.JTextField;
import uk.org.rlinsdale.nbpcglibrary.api.BadFormatException;
import uk.org.rlinsdale.nbpcglibrary.api.DateOnly;
import uk.org.rlinsdale.nbpcglibrary.common.Callback;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 * A Field for displaying and editing a value which is a Decimal (or Currency)
 * value.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class DecimalField extends EditableFieldImpl<BigDecimal, FieldSource<BigDecimal>> {

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
    public DecimalField(FieldSource<BigDecimal> source, int size, BigDecimal min, BigDecimal max, BigDecimal initialValue, Callback callback) {
        this(new JTextField(), source, size, min, max, initialValue, callback);
    }

    private DecimalField(JTextField fieldcomponent, FieldSource<BigDecimal> source, int size, BigDecimal min, BigDecimal max, BigDecimal initialValue, Callback callback) {
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
    public final BigDecimal getFieldValue() throws BadFormatException {
        try {
            return new BigDecimal(fieldcomponent.getText().trim());
        } catch (NumberFormatException ex) {
            throw new BadFormatException("Bad entry format - expected a decimal number");
        }
    }

    @Override
    public final void setFieldValue(BigDecimal value) {
        fieldcomponent.setText(value.toString());
    }

    private class MaxRule extends Rule {

        private final BigDecimal max;

        protected MaxRule(BigDecimal max) {
            super("Too large - must be no greater than " + max);
            this.max = max;
        }

        @Override
        public boolean ruleCheck() {
            return source.get().compareTo(max) != 1;
        }
    }

    private class MinRule extends Rule {

        private final BigDecimal min;

        protected MinRule(BigDecimal min) {
            super("Too small - must be no smaller than " + min);
            this.min = min;
        }

        @Override
        public boolean ruleCheck() {
            return source.get().compareTo(min) != -1;
        }
    }
}
