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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

/**
 * A Field to select choice combobox
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <T> type of the data to be represented and selected in the combo box
 */
public class ChoiceField<T> extends FieldView<T> {

    private List<T> choices = new ArrayList<>();
    private boolean nullSelectionAllowed = false;
    private final JComboBox fieldcomponent;

    /**
     * Constructor
     */
    public ChoiceField() {
        this(new JComboBox());
    }

    private ChoiceField(JComboBox fieldcomponent) {
        super(fieldcomponent);
        this.fieldcomponent = fieldcomponent;
        fieldcomponent.setEditable(false);
    }

    @Override
    public final T get() {
        return (T) fieldcomponent.getItemAt(fieldcomponent.getSelectedIndex());
    }

    @Override
    public void setChoices(List<T> choices) {
        this.choices = choices;
    }

    @Override
    public void setNullSelectionAllowed(boolean isAllowed) {
        this.nullSelectionAllowed = isAllowed;
    }

    @Override
    public final void set(T value) {
        boolean selected = false;
        fieldcomponent.removeAllItems();
        if (choices != null) {
            for (T item : choices) {
                fieldcomponent.addItem(item);
                if (item.equals(value)) {
                    fieldcomponent.setSelectedItem(item);
                    selected = true;
                }
            }
        }
        if (!selected || nullSelectionAllowed) {
            fieldcomponent.insertItemAt(null, 0);
        }
        if (!selected) {
            fieldcomponent.setSelectedItem(null);
        }
    }

    @Override
    public void addActionListener(ActionListener listener) {
        fieldcomponent.addActionListener(listener);
    }
}
