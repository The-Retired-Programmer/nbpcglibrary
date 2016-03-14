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

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import uk.org.rlinsdale.nbpcglibrary.common.Callback;

/**
 * A Field to select choice combobox
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> type of the data to be represented and selected in the combo box
 */
public class ChoiceField<T> extends FieldImpl<T, ChoiceFieldSource<T>> {

    private List<T> choices = new ArrayList<>();
    private final boolean nullSelectionAllowed;
    private final JComboBox fieldcomponent;

    /**
     * Constructor
     *
     * @param source the data source for this field
     * @param nullSelectionAllowed true if a null selection item is to be added
     * to the choice items
     * @param initialValue the initial value of the display (or null if source
     * provides this
     * @param choices the choices to be displayed in the combobox
     * @param callback the callback with is used to inform of source updates
     * from field
     */
    public ChoiceField(ChoiceFieldSource<T> source, boolean nullSelectionAllowed, T initialValue, List<T> choices, Callback callback) {
        this(new JComboBox(), source, nullSelectionAllowed, initialValue, choices, callback);
    }

    private ChoiceField (JComboBox fieldcomponent, ChoiceFieldSource<T> source, boolean nullSelectionAllowed, T initialValue, List<T> choices, Callback callback) {
        super(fieldcomponent, source, initialValue, callback);
        this.fieldcomponent = fieldcomponent;
        this.nullSelectionAllowed = nullSelectionAllowed;
        fieldcomponent.setEditable(false);
        fieldcomponent.addActionListener(getActionListener());
        source.setChoices(choices == null ? source.getChoices() : choices);
        if (initialValue != null) {
            source.set(initialValue);
        }
        updateChoicesFromSource();
    }

    /**
     * Request that the values in the combo box are updated from the source.
     */
    protected void updateChoicesFromSource() {
        choices = getSourceChoices();
        preFieldUpdateAction();
        updateFieldFromSource(true);
        postFieldUpdateAction();
    }

    /**
     * hook to allow actions to take place before updating a combobox
     */
    protected void preFieldUpdateAction() {
    }

    /**
     * hook to allow actions to take place after updating a combobox
     */
    protected void postFieldUpdateAction() {
    }

    @Override
    public final T getFieldValue() {
        return (T) fieldcomponent.getItemAt(fieldcomponent.getSelectedIndex());
    }

    @Override
    public final void setFieldValue(T value) {
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

    /**
     * Request that the values in the combo box are updated from the source.
     *
     * @return the set of values to be inserted
     */
    protected List<T> getSourceChoices() {
        return source.getChoices();
    }
}
