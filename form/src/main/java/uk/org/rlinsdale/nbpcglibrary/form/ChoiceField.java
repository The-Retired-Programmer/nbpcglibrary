/*
 * Copyright (C) 2014-2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

/**
 * A Field to select choice combobox
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
