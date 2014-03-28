/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package linsdale.nbpcg.topcomponentsupportlib;

import java.util.List;
import linsdale.nbpcg.formsupportlib.ChoiceField;
import linsdale.nbpcg.formsupportlib.FormFieldChangeListenerParams;
import linsdale.nbpcg.supportlib.IntWithDescription;
import linsdale.nbpcg.supportlib.Listener;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public abstract class EnumChoiceField extends ChoiceField {

    private List<String> choices; 

    /**
     * Constructor
     *
     * @param field
     * @param label the field label
     * @param choices the list of possible values
     */
    public EnumChoiceField(IntWithDescription field, String label, List<String> choices) {
        this(field, label, choices, null);
    }
    
    /**
     * Constructor
     *
     * @param field
     * @param label the field label
     * @param choices the list of possible values
     * @param listener
     */
    public EnumChoiceField(IntWithDescription field, String label, List<String> choices, Listener<FormFieldChangeListenerParams> listener) {
        super(field, label, listener);
        this.choices = choices;
    }

    public void initChoices() {
        setChoices(choices);
    }
    
    public void closeChoices() {
    }
}
