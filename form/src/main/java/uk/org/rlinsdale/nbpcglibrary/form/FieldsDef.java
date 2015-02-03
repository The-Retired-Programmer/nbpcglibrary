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
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.HasInstanceDescription;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;
import uk.org.rlinsdale.nbpcglibrary.common.Rules;

/**
 * A collection of a setField of fields - for use in defining the fields content of a
 form segment.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class FieldsDef implements HasInstanceDescription {

    private final List<BaseField> fields = new ArrayList<>();
    private final Rules rules = new Rules();

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this);
    }
    
    /**
     * add a field to this collection.
     *
     * @param f the field to add
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
    public final void set() {
        fields.stream().forEach((f) -> {
            f.updateFieldFromBackingObject();
        });
    }
    
    /**
     * save the field values
     */
    public final void saveFields() {
        fields.stream().forEach((f) -> {
            if (f instanceof EditableField) {
                ((EditableField)f).updateBackingObjectFromField();
            }
        });
    }

    /**
     * finalise the save action
     *
     * @return true if save was successful
     */
    public abstract boolean save();

    /**
     * Cancel occurred - action this.
     */
    public abstract void cancel();

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
 collection's rule setField and each individual field which is failing.
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
     * Check if all rules in the collection's rule setField and each individual field
 are valid.
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
