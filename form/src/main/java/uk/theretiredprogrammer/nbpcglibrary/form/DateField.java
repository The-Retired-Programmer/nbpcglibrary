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
import javax.swing.JTextField;
import uk.theretiredprogrammer.nbpcglibrary.api.BadFormatException;
import uk.theretiredprogrammer.nbpcglibrary.api.DateOnly;

/**
 * A Field for displaying and editing a value which is a DateOnly Value.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class DateField extends FieldView<DateOnly> {

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
    public final DateOnly get() throws BadFormatException {
        return new DateOnly(fieldcomponent.getText().trim());
    }

    @Override
    public final void set(DateOnly value) {
        fieldcomponent.setText(value.toString());
    }

    @Override
    public void addActionListener(ActionListener listener) {
        fieldcomponent.addActionListener(listener);
    }
}
