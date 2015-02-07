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
 * A collection of a setField of fields - for use in defining the fields content
 * of a form segment.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class FieldsDef implements HasInstanceDescription {

    private final List<BaseField> fields = new ArrayList<>();
    private final Rules rules = new Rules();
    private String[] parameters;

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
     * Set the values of all fields.
     */
    public final void updateAllFieldsFromBackingObject() {
        fields.stream().forEach((f) -> {
            f.updateFieldFromBackingObject();
        });
    }

    /**
     * Test each field and if OK then copy its value into the backing Object,
     * finally test any fieldsef rules and if all rules are OK then return true
     * (ie the fieldsdef is savable).
     *
     * @return true if all rules are OK.
     */
    public final boolean testAndSaveAllFields() {
        boolean valid = true;
        for (BaseField f : fields) {
            if (f.checkRules()) {
                f.updateBackingObjectFromField();
            } else {
                valid = false;
            }
        }
        return valid && rules.checkRules();
    }

    /**
     * finalise the save action from backingObject to other storage (eg
     * persistent storage)
     *
     * @return true if save was successful
     */
    public abstract boolean save();

    String[] getParameters() {
        return parameters;
    }

    /**
     * Set the parameters to be returned from the fielddef (consolidated at
     * dialog completion)
     *
     * @param parameters
     */
    protected void setParameters(String... parameters) {
        this.parameters = parameters;
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
        fields.stream().forEach((f) -> {
            f.addFailureMessages(sb);
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
            valid = valid && f.checkRules();
        }
        return valid && rules.checkRules();
    }
}
