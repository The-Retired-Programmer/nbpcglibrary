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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;

/**
 * A collection of fields - for use in defining the fields content of a form
 * segment.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class FormFields implements HasInstanceDescription {

    private final List<Field> fields = new ArrayList<>();
    private String[] parameters;
    private final FormRules formrules;
    private final ErrorMarkerField errormarker;

    /**
     * Constructor
     */
    public FormFields() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param formrules the class level rules
     */
    public FormFields(FormRules formrules) {
        this.formrules = formrules;
        errormarker = new ErrorMarkerField();
        add(errormarker);
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this);
    }

    /**
     * add a field to this collection.
     *
     * @param f the field to add
     */
    public final void add(Field f) {
        fields.add(f);
    }

    /**
     * Get the collection of fields.
     *
     * @return the collection of fields
     */
    public final List<Field> getFields() {
        return fields;
    }

    /**
     * Set the values of all fields.
     */
    public final void updateAllFieldsFromSource() {
        fields.stream().filter((f) -> (f instanceof EditableField)).forEach((f) -> {
            ((EditableField) f).updateFieldFromSource();
        });
    }

    /**
     * Set the values of all fields into sources.
     */
    public final void updateAllSourcesFromFields() {
        fields.stream().filter((f) -> (f instanceof EditableField)).forEach((f) -> {
            ((EditableField) f).updateSourceFromField();
        });
    }

    /**
     * Finalise the save action from source to other storage (eg persistent
     * storage)
     *
     * @return true if save was successful
     * @throws IOException if problems occurred during save
     */
    public abstract boolean save() throws IOException;

    String[] getParameters() {
        return parameters;
    }

    /**
     * Set the parameters to be returned from the formfields (consolidated at
     * dialog completion)
     *
     * @param parameters the set of parameters
     */
    protected void setParameters(String... parameters) {
        this.parameters = parameters;
    }

    /**
     * Check if all rules in the form's rule set and each individual field
     * are valid.
     *
     * @return true if all rules are valid
     */
    public final boolean checkRules() {
        boolean valid = true;
        for (Field f : fields) {
            if (f instanceof EditableField) {
                if (!((EditableField) f).checkRules()) {
                    valid = false;
                }
            }
        }
        if (!checkFormRules()) {
            valid = false;
        }
        return valid;
    }

    private boolean checkFormRules() {
        if (formrules != null) {
            boolean res = formrules.checkRules();
            if (res) {
                errormarker.clear();
            } else {
                errormarker.report(formrules.getErrorMessages());
            }
            return res;
        } else {
            return true;
        }
    }
}
