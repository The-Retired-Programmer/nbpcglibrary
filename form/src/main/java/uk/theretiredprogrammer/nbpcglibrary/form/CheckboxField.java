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
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;

/**
 * A Field for displaying and editing a value which is a simple boolean using a
 * checkbox.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class CheckboxField extends FieldView<Boolean> {
    
    private final JCheckBox fieldcomponent;

    /**
     * Constructor
     *
     * @param listener the itemlistener to be associated with this field
     */
    public CheckboxField(ItemListener listener) {
        this(new JCheckBox(), listener);
    }
    
    /**
     * Constructor
     */
    public CheckboxField() {
        this(new JCheckBox(), null);
    }
    
    private CheckboxField(JCheckBox fieldcomponent, ItemListener listener) {
        super(fieldcomponent);
        this.fieldcomponent = fieldcomponent;
        if (listener != null ) {
            fieldcomponent.addItemListener(listener);
        }
    }

    @Override
    public final Boolean get() {
        return fieldcomponent.isSelected();
    }

    @Override
    public final void set(Boolean value) {
        fieldcomponent.setSelected(value);
    }
    
    @Override
    public void addActionListener(ActionListener listener) {
        fieldcomponent.addActionListener(listener);
    }
}
