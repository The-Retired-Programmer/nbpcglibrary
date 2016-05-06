/*
 * Copyright (C) 2015-2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JTextField;
import uk.org.rlinsdale.nbpcglibrary.api.BadFormatException;

/**
 * A Field for displaying and editing a value which is a Long Value.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class LongField extends FieldView<Long> {

    private final JTextField fieldcomponent;

    /**
     * Constructor
     *
     */
    public LongField() {
        this(new JTextField(), 20);
    }
    
    /**
     * Constructor
     *
     * @param size the size of the text field object
     */
    public LongField(int size) {
        this(new JTextField(), size);
    }

    private LongField(JTextField fieldcomponent, int size) {
        super(fieldcomponent);
        this.fieldcomponent = fieldcomponent;
        fieldcomponent.setColumns(size);
    }

    @Override
    public final Long get() throws BadFormatException {
        try {
            return Long.parseLong(fieldcomponent.getText().trim());
        } catch (NumberFormatException ex) {
            throw new BadFormatException("Bad entry format - expected a number");
        }
    }

    @Override
    public final void set(Long value) {
        fieldcomponent.setText(value.toString());
    }

    @Override
    public void addActionListener(ActionListener listener) {
        fieldcomponent.addActionListener(listener);
    }
}
