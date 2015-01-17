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

import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JComboBox;

/**
 * A Field to select a string from a setField of values (implemented as a
 * ComboBox)
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class ChoiceField extends EditableField<String> {

    /**
     * The undefined text as displayed in a choice field
     */
    public final static String UNDEFINED = "...";
    private List<String> choices;
    private final JComboBox<String> combobox;
    private final ChoiceFieldBackingObject backingObject;

    /**
     * Constructor
     *
     * @param backingObject the backing Object
     * @param label the label text for the field
     */
    public ChoiceField(ChoiceFieldBackingObject backingObject, String label) {
        this(backingObject, label, new JComboBox<>());
    }

    private ChoiceField(ChoiceFieldBackingObject backingObject, String label, JComboBox<String> combobox) {
        super(backingObject, label, combobox);
        this.backingObject = backingObject;
        this.combobox = combobox;
        combobox.setEditable(false);
        updateChoicesFromBackingObject(backingObject.get());
    }

    /**
     * get the ChoiceFileBackingObject for this field
     *
     * @return
     */
    public ChoiceFieldBackingObject getBackingObject() {
        return backingObject;
    }
    
    /**
     * Get the choices
     * 
     * @return list of choice texts
     */
    protected List<String> getChoicesText() {
        return backingObject.getChoices();
    }

    @Override
    final String get() {
        return combobox.getItemAt(combobox.getSelectedIndex());
    }

    @Override
    void updateIfChange(String value) {
        if ((value != null) && (!value.equals(lastvaluesetinfield)) && (!value.equals(UNDEFINED))) {
            setField(value);
            backingObject.set(value);
        }
    }

    /**
     * request that the choices for the field are updated from the values in the
     * Backing Object
     */
    public final void updateChoicesFromBackingObject() {
        updateChoicesFromBackingObject(lastvaluesetinfield);
    }
    
    private void updateChoicesFromBackingObject(String value){
        choices = getChoicesText();
        preFieldUpdateAction();
        setField(value);
        postFieldUpdateAction();
    }

    /**
     * hook to allow actions to take place before updating a combobox
     */
    public void preFieldUpdateAction() {
    }

    /**
     * hook to allow actions to take place after updating a combobox
     */
    public void postFieldUpdateAction() {
    }

    @Override
    void set(String value) {
        boolean selected = false;
        combobox.removeAllItems();
        if (choices != null) {
            for (String item : choices) {
                combobox.addItem(item);
                if (item.equals(value)) {
                    combobox.setSelectedItem(item);
                    selected = true;
                }
            }
        }
        if (!selected) {
            combobox.insertItemAt(UNDEFINED, 0);
            combobox.setSelectedItem(UNDEFINED);
            lastvaluesetinfield = UNDEFINED;
        }
    }

    @Override
    final void addActionListener(ActionListener listener) {
        combobox.addActionListener(listener);
    }

    @Override
    final void removeActionListener(ActionListener listener) {
        combobox.removeActionListener(listener);
    }
}
