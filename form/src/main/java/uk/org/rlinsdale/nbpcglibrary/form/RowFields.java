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
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;

/**
 * A row of fields - for use in defining the fields content of a table segment.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class RowFields implements HasInstanceDescription {

    private final EditableFieldList row = new EditableFieldList();
    private final FieldsDefRules fieldsdefrules;
    private final ErrorMarkerField errormarker;

    /**
     * Constructor
     */
    public RowFields() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param fieldsdefrules the class level rules
     */
    public RowFields(FieldsDefRules fieldsdefrules) {
        this.fieldsdefrules = fieldsdefrules;
        errormarker = new ErrorMarkerField();
        // TODO need to fix this ommission!
//        add(errormarker);
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this);
    }

    /**
     * add a field to this row.
     *
     * @param f the field to add
     */
    public final void add(EditableField f) {
        row.add(f);
    }

    /**
     * Get the row (collection of fields).
     *
     * @return the crow
     */
    public final EditableFieldList getRow() {
        return row;
    }

    /**
     * Set the values of all fields.
     */
    public final void updateAllFieldsFromSource() {
        row.stream().forEach((f) -> {
            f.updateFieldFromSource();
        });
    }

    /**
     * Set the values of all fields into sources.
     */
    public final void updateAllSourcesFromFields() {
        row.stream().forEach((f) -> {
            f.updateSourceFromField();
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

    /**
     * Check if all rules in the row's rule set and each individual field
     * are valid.
     *
     * @return true if all rules are valid
     */
    public final boolean checkRules() {
        boolean valid = true;
        for (EditableField f : row) {
            if (!f.checkRules()) {
                valid = false;
            }
        }
        if (!checkFieldsDefRules()) {
            valid = false;
        }
        return valid;
    }

    /**
     * Check the rules defined for the fieldDef.
     *
     * @return true if the rules are obeyed (ie OK)
     */
    public boolean checkFieldsDefRules() {
        if (fieldsdefrules != null) {
            boolean res = fieldsdefrules.checkRules();
            if (res) {
                errormarker.clear();
            } else {
                errormarker.report(fieldsdefrules.getErrorMessages());
            }
            return res;
        } else {
            return true;
        }
    }
}
