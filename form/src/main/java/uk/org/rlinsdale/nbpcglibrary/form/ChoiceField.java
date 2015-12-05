/*
 * Copyright (C) 2014-2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

/**
 * A Field to select choice combobox
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> type of the data to be represented and selected in the combo box
 */
public abstract class ChoiceField<T> extends EditableField<T> {

    /**
     * The undefined text as displayed in a choice field
     */
//    public final static String UNDEFINED = "...";
    private List<T> choices = new ArrayList<>();
    private final JComboBox<T> combobox;
    private final boolean nullSelectionAllowed;

    /**
     * Constructor
     *
     * @param label the label text for the field
     * @param nullSelectionAllowed true if a null selection item is to be added
     * to the choice items
     */
    protected ChoiceField(String label, boolean nullSelectionAllowed) {
        this(label, new JComboBox<>(), nullSelectionAllowed);
    }

    private ChoiceField(String label, JComboBox<T> combobox, boolean nullSelectionAllowed) {
        super(label, combobox, null);
        this.combobox = combobox;
        this.nullSelectionAllowed = nullSelectionAllowed;
        combobox.setEditable(false);
        combobox.addActionListener(getActionListener());
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
    protected final T getFieldValue() {
        return combobox.getItemAt(combobox.getSelectedIndex());
    }

//    @Override
//    protected final void updateIfChange(T value) {
//        if ((value != null) && (!value.equals(lastvaluesetinfield)) && (nullSelectionAllowed || !value.equals(UNDEFINED))) {
//            setSourceValue(value);
//            backingObject.set(value);
//        }
//    }
    @Override
    protected final void setFieldValue(T value) {
        boolean selected = false;
        combobox.removeAllItems();
        if (choices != null) {
            for (T item : choices) {
                combobox.addItem(item);
                if (item.equals(value)) {
                    combobox.setSelectedItem(item);
                    selected = true;
                }
            }
        }
        if (!selected || nullSelectionAllowed) {
            combobox.insertItemAt(null, 0);
//            combobox.insertItemAt(UNDEFINED, 0);
        }
        if (!selected) {
//            combobox.setSelectedItem(UNDEFINED);
//            lastvaluesetinfield = UNDEFINED;
            combobox.setSelectedItem(null);
        }
    }

    // no validation is normally required for a combobox
    @Override
    protected boolean sourceCheckRules() {
        return true;
    }

    @Override
    protected String getSourceErrorMessages() {
        return "";
    }

    /**
     * Request that the values in the combo box are updated from the source.
     *
     * @return the set of values to be inserted
     */
    protected abstract List<T> getSourceChoices();
}
