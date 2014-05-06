/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package uk.org.rlinsdale.nbpcg.formsupportlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcg.supportlib.IntWithDescription;
import uk.org.rlinsdale.nbpcg.supportlib.Listener;

/**
 * A Field to select a string value from a set of values (implemented as a
 * ComboBox)
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class ChoiceField extends EditableField {

    /**
     * The undefined text as displayed in a choice field
     */
    public final static String UNDEFINED = "...";
    private List<String> choices;
    private final JComboBox combobox;
    private String value = UNDEFINED;
    private final ComboActionListener comboactionlistener = new ComboActionListener();
    private final ComboFocusListener combofocuslistener = new ComboFocusListener();

    /**
     * Factory method to create a choice field
     *
     * @param field the field id
     * @param label the label text for the field
     * @return the created choice field
     */
    public static ChoiceField create(IntWithDescription field, String label) {
        return new ChoiceField(field, label, null);
    }

    /**
     * Factory method to create a choice field
     *
     * @param field the field id
     * @param label the label text for the field
     * @param listener the listener for changes to field value
     * @return the created choice field
     */
    public static ChoiceField create(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener) {
        return new ChoiceField(field, label, listener);
    }

    /**
     * Constructor
     *
     * @param field the field id
     * @param label the label text for the field
     * @param listener the listener for changes to field value
     */
    protected ChoiceField(IntWithDescription field, String label, Listener<FormFieldChangeListenerParams> listener) {
        super(field, label);
        combobox = new JComboBox();
        combobox.setEditable(false);
        addListener(listener);
    }

    private class ComboActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            update(get());
        }
    }

    private class ComboFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent fe) {
        }

        @Override
        public void focusLost(FocusEvent fe) {
            update(get());
        }
    }

    @Override
    public JComponent getComponent() {
        return combobox;
    }

    /**
     * Get the value of this field
     *
     * @return the field value
     */
    public final String get() {
        return (String) combobox.getSelectedItem();
    }

    /**
     * Set the value of the field
     *
     * @param value the value
     */
    public final void set(String value) {
        this.value = value;
        setupCombobox();
    }

    private void update(String newvalue) {
        if ((newvalue != null) && (!newvalue.equals(value)) && (!newvalue.equals(UNDEFINED))) {
            value = newvalue;
            fireChanged();
        }
    }

    /**
     * Set the set of possible choices for this field
     *
     * @param choices the set of choices
     */
    public final void setChoices(List<String> choices) {
        this.choices = choices;
        setupCombobox();
    }

    /**
     * Set the value and the set of possible choices for this field
     *
     * @param value the field value
     * @param choices the set of choices
     */
    public final void setValueAndChoices(String value, List<String> choices) {
        this.value = value;
        this.choices = choices;
        setupCombobox();
    }

    private void setupCombobox() {
        combobox.removeActionListener(comboactionlistener);
        combobox.removeFocusListener(combofocuslistener);
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
            value = UNDEFINED;
        }
        combobox.addActionListener(comboactionlistener);
        combobox.addFocusListener(combofocuslistener);
    }
}
