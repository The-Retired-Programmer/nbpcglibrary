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
package uk.org.rlinsdale.nbpcglibrary.form;

import uk.org.rlinsdale.nbpcglibrary.common.Rule;
import uk.org.rlinsdale.nbpcglibrary.common.Rules;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.IntWithDescription;
import uk.org.rlinsdale.nbpcglibrary.common.Listening;

/**
 * Abstract Class representing an editable Field on a Form
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class EditableField extends BaseField {

    private final Listening<FormFieldChangeListenerParams> listening;
    private final IntWithDescription field;
    private final Rules rules = new Rules();

    /**
     * Constructor
     *
     * @param field the field id
     * @param label the label text for this field
     */
    public EditableField(IntWithDescription field, String label) {
        super(label);
        this.field = field;
        listening = new Listening<>("Form/" + label);
    }

    /**
     * Add a listener to this field. The listener will fire on changes to field
     * content.
     *
     * @param listener the listener
     */
    public void addListener(Listener<FormFieldChangeListenerParams> listener) {
        listening.addListener(listener);
    }

    /**
     * Remove a listener from this field.
     *
     * @param listener the listener
     */
    public void removeListener(Listener<FormFieldChangeListenerParams> listener) {
        listening.removeListener(listener);
    }

    /**
     * Test if there are any listeners attached to this field.
     *
     * @return true if one or more listeners are attached to this field
     */
    public boolean hasListener() {
        return listening.hasListener();
    }

    /**
     * Add a rule to this field's Rule Set.
     *
     * @param r the rule to add
     */
    public void addRule(Rule r) {
        rules.addRule(r);
    }

    /**
     * Check if all rules in the field's rule set are valid.
     *
     * @return true if all rules are valid
     */
    public boolean checkRules() {
        return rules.checkRules();
    }

    /**
     * Add failure messages to the StringBuilder for each rule in this field's
     * rule set which is failing.
     *
     * @param sb the StringBuilder collecting failure messages
     */
    public final void addFailureMessages(StringBuilder sb) {
        rules.addFailureMessages(sb);
    }

    /**
     * fire the listener - called when a change to this field occurs.
     */
    protected void fireChanged() {
        listening.fire(new FormFieldChangeListenerParams(field));
    }
}
