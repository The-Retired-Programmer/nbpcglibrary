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
package uk.org.rlinsdale.nbpcg.topcomponentsupportlib;

import java.util.List;
import uk.org.rlinsdale.nbpcg.formsupportlib.ChoiceField;
import uk.org.rlinsdale.nbpcg.formsupportlib.FormFieldChangeListenerParams;
import uk.org.rlinsdale.nbpcg.supportlib.IntWithDescription;
import uk.org.rlinsdale.nbpcg.supportlib.Listener;

/**
 * Choice Field - taking values from enum.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class EnumChoiceField extends ChoiceField {

    private List<String> choices;

    /**
     * Constructor.
     *
     * @param field the field Id
     * @param label the field label
     * @param choices the list of possible values
     */
    public EnumChoiceField(IntWithDescription field, String label, List<String> choices) {
        this(field, label, choices, null);
    }

    /**
     * Constructor
     *
     * @param field the field Id
     * @param label the field label
     * @param choices the list of possible values
     * @param listener change listener
     */
    public EnumChoiceField(IntWithDescription field, String label, List<String> choices, Listener<FormFieldChangeListenerParams> listener) {
        super(field, label, listener);
        this.choices = choices;
    }

    /**
     * initialise the choices text
     */
    public void initChoices() {
        setChoices(choices);
    }

    /**
     * finish managing the choices text
     */
    public void closeChoices() {
    }
}
