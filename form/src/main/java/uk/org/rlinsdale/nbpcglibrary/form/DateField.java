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

/**
 * A Field for displaying and editing a value which is a DateOnly Value.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class DateField extends EditableField<DateOnly> {

    private final JTextField field;

    /**
     * Constructor
     *
     * @param label field label
     * @param size size of the value display
     */
    public DateField(String label, int size) {
        this(label, new JTextField(), size);
    }

    private DateField(String label, JTextField field, int size) {
        super(label, field, null);
        this.field = field;
        field.setColumns(size);
        field.addActionListener(getActionListener());
    }

    @Override
    protected final DateOnly getFieldValue() throws BadFormatException {
            return new DateOnly(field.getText().trim());
    }

    @Override
    protected final void setFieldValue(DateOnly value) {
        field.setText(value.toString());
    }
}
