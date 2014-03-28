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
package linsdale.nbpcg.formsupportlib;

import linsdale.nbpcg.supportlib.*;

/**
 * Abstract Class representing an editable Field on a Form
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public abstract class EditableField extends BaseField {

    private final Listening<FormFieldChangeListenerParams> listening;
    private final IntWithDescription field;
    private final Rules rules = new Rules();

    /**
     * Constructor
     *
     * @param field
     * @param label the label text for this field
     */
    public EditableField(IntWithDescription field, String label) {
        super(label);
        this.field = field;
        listening = new Listening<>("Form/" + label);
    }

    public void addListener(Listener<FormFieldChangeListenerParams> listener) {
        listening.addListener(listener);
    }

    public void removeListener(Listener<FormFieldChangeListenerParams> listener) {
        listening.removeListener(listener);
    }

    public boolean hasListener() {
        return listening.hasListener();
    }

    /**
     * pass through routines - to provide the Rules interface
     * @param r
     */
    public void addRule(Rule r) {
        rules.addRule(r);
    }

    public boolean checkRules() {
        return rules.checkRules();
    }

    public final void addFailureMessages(StringBuilder sb) {
        rules.addFailureMessages(sb);
    }

    /**
     * fire the listener for a change to this field
     */
    protected void fireChanged() {
        listening.fire(new FormFieldChangeListenerParams(field));
    }
}
