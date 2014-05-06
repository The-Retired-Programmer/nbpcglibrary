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

import java.util.ArrayList;
import java.util.List;
import uk.org.rlinsdale.nbpcg.supportlib.Rule;
import uk.org.rlinsdale.nbpcg.supportlib.Rules;

/**
 * A collection of a set of fields - for use in defining the fields content of a
 * form segment.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class FieldsDef {

    private final List<BaseField> fields = new ArrayList<>();
    private final String name;
    private final Rules rules = new Rules();

    /**
     * Constructor
     *
     * @param name the name for this collection of fields.
     */
    public FieldsDef(String name) {
        this.name = name;
    }

    /**
     * Get the name of this collection of fields.
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * add a field to this collection.
     *
     * @param f teh field to add
     */
    public final void add(BaseField f) {
        fields.add(f);
    }

    /**
     * Get the collection of fields.
     *
     * @return the collection of fields
     */
    public final List<BaseField> getFields() {
        return fields;
    }

    /**
     * Set the values of fields in the collection.
     */
    public abstract void set();

    /**
     * First phase of the saving of values of fields in the collection.
     */
    public void presave() {
    }

    /**
     * Second phase of the saving of values of fields in the collection
     *
     * @return true if save was successful
     */
    public boolean save() {
        return true;
    }

    /**
     * Reset values of fields in the collection to their previously checkpointed
     * values.
     */
    public void reset() {
    }

    /**
     * Add a rule to the collection - these rules are not individual field rules
     * (which would be added to the field), but rather more complex rules
     * associated with the collection as a whole.
     *
     * @param r the rule
     */
    public final void addRule(Rule r) {
        rules.addRule(r);
    }

    /**
     * Add failure messages to the StringBuilder for each rule in the
     * collection's rule set and each individual field which is failing.
     *
     * @param sb the StringBuilder collecting failure messages
     */
    public final void addFailureMessages(StringBuilder sb) {
        fields.stream().filter((f) -> (f instanceof EditableField)).forEach((f) -> {
            ((EditableField) f).addFailureMessages(sb);
        });
        rules.addFailureMessages(sb);
    }

    /**
     * Check if all rules in the collection's rule set and each individual field
     * are valid.
     *
     * @return true if all rules are valid
     */
    public final boolean checkRules() {
        boolean valid = true;
        for (BaseField f : fields) {
            if (f instanceof EditableField) {
                valid = valid && ((EditableField) f).checkRules();
            }
        }
        return valid && rules.checkRules();
    }
}
