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
import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;

/**
 * A collection of fields - for use in defining the fields content of a form
 * segment.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class TableDef implements HasInstanceDescription {

    private String[] parameters;
    private final ErrorMarkerField errorMarker = new ErrorMarkerField();
    private final String title;
    private final FieldsDefRules tabledefrules;

    /**
     * Constructor
     *
     * @param title the table title (or null if no title to be displayed
     * @param tabledefrules the table level rules or null if notable level rules
     */
    public TableDef(String title, FieldsDefRules tabledefrules) {
        this.title = title;
        this.tabledefrules = tabledefrules;
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this);
    }
    
    /**
     * Get the error marker field for displaying table level errors
     * 
     * @return the error marker field
     */
    public ErrorMarkerField getTableErrorMarker() {
        return errorMarker;
    }

    /**
     * Get the Title string for the table
     *
     * @return the table title or null if no title required
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the number of components forming the table display
     *
     * @return number of components forming the table display
     */
    public abstract int getTableWidth();

    /**
     * Get the list of column headings for this table
     *
     * @return A list of column headings
     */
    public abstract FieldList getColumnHeadings();

    /**
     * Get the list of row fields for this table
     *
     * @return A list of rows contents (each is a fieldlist)
     */
    public abstract List<EditableFieldList> getRows();

    /**
     * Get the new row fields for this table
     */
    public abstract void createNewRow();

    /**
     * Copy a row to create new row fields for this table
     *
     * @param rowindices the list of rows to copy
     */
    public abstract void createCopyRows(List<Integer> rowindices);

    /**
     * Delete row fields for this table
     *
     * @param rowindices the list of rows to delete
     */
    public abstract void deleteRows(List<Integer> rowindices);

    /**
     * Set the values of all fields.
     */
    public final void updateAllFieldsFromSource() {
        getRows().stream().forEach((r) -> {
            r.updateRowFieldsFromSource();
        });
    }

    /**
     * Set the values of all fields into sources.
     */
    public final void updateAllSourcesFromFields() {
        getRows().stream().forEach((r) -> {
            r.updateRowSourcesFromFields();
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

    protected String[] getParameters() {
        return parameters;
    }

    /**
     * Set the parameters to be returned from the fielddef (consolidated at
     * dialog completion)
     *
     * @param parameters the set of parameters
     */
    protected void setParameters(String... parameters) {
        this.parameters = parameters;
    }

    /**
     * Check if all rules in the collection's rule set and each individual field
     * are valid.
     *
     * @return true if all rules are valid
     */
    public final boolean checkRules() {
        boolean valid = true;
        for (EditableFieldList r : getRows()) {
            if (!r.checkRules()) {
                valid = false;
            }
        }
        if (!checkTableDefRules()) {
            valid = false;
        }
        return valid;
    }

    /**
     * Check the rules defined for the Table.
     *
     * @return true if the rules are obeyed (ie OK)
     */
    public boolean checkTableDefRules() {
        if (tabledefrules != null) {
            boolean res = tabledefrules.checkRules();
            if (res) {
                errorMarker.clear();
            } else {
                errorMarker.report(tabledefrules.getErrorMessages());
            }
            return res;
        } else {
            return true;
        }
    }
}
