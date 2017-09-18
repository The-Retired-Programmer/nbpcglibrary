/*
 * Copyright 2014-2017 Richard Linsdale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.nbpcglibrary.form;

import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextField;
import uk.theretiredprogrammer.nbpcglibrary.api.BadFormatException;

/**
 * A Field for displaying and editing a value which is a DateOnly Value.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class DateField extends FieldView<Date> {

    private final JTextField fieldcomponent;

    /**
     * Constructor
     */
    public DateField() {
        this(new JTextField(), 20);
    }

    /**
     * Constructor
     *
     * @param size the size of the text field object
     */
    public DateField(int size) {
        this(new JTextField(), size);
    }

    private DateField(JTextField fieldcomponent, int size) {
        super(fieldcomponent);
        this.fieldcomponent = fieldcomponent;
        fieldcomponent.setColumns(size);
    }

    @Override
    public final Date get() throws BadFormatException {
        SimpleDateFormat dateonly_readable = new SimpleDateFormat("dd-MMM-yyyy");
        dateonly_readable.setLenient(true);
        try {
            return dateonly_readable.parse(fieldcomponent.getText().trim());
        } catch (ParseException ex) {
            throw new BadFormatException("Bad input for Date Only");
        }
    }

    @Override
    public final void set(Date value) {
        SimpleDateFormat dateonly_readable = new SimpleDateFormat("dd-MMM-yyyy");
        dateonly_readable.setLenient(true);
        fieldcomponent.setText(dateonly_readable.format(value));
    }

    @Override
    public void addActionListener(ActionListener listener) {
        fieldcomponent.addActionListener(listener);
    }
}
