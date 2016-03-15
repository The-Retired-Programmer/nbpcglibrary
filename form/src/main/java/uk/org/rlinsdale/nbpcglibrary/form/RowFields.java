/*
 * Copyright (C) 2014-2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;
import uk.org.rlinsdale.nbpcglibrary.common.Rules;

/**
 * A row of fields - for use in defining the fields content of a table row.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class RowFields extends ArrayList<Field> implements HasInstanceDescription {

    private final Rules rowrules;
    private final ErrorMarkerField errormarker = new ErrorMarkerField();

    /**
     * Constructor
     * 
     */
    public RowFields() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param rowrules the class level rules
     */
    public RowFields(Rules rowrules) {
        this.rowrules = rowrules;
        add(errormarker);
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this);
    }

    /**
     * Set the values of all fields.
     */
    public final void updateFieldsFromSources() {
        this.stream().forEach((f) -> {
            f.updateFieldFromSource();
        });
    }

     /**
     * Get the list of components which must be displayed.
     * @return the list of components
     */
    public List<JComponent> getComponents() {
        List<JComponent> c = new ArrayList<>();
        this.stream().forEach((field) -> {
            field.getComponents().stream().forEach(component -> {
                c.add((JComponent) component);
            });
        });
        return c;
    }
    
    /**
     * Set the values of all fields into sources.
     */
    public final void updateSourcesFromFields() {
        this.stream().forEach((f) -> {
            f.updateSourceFromField();
        });
    }

    /**
     * Check if all rules in the row's rule set and each individual field are
     * valid.
     *
     * @return true if all rules are valid
     */
    public final boolean checkRules() {
        boolean valid = true;
        for (Field f : this) {
                if (!f.checkRules()) {
                    valid = false;
                }
            }
        if (!checkRowRules()) {
            valid = false;
        }
        return valid;
    }

    private boolean checkRowRules() {
        if (rowrules != null) {
            boolean res = rowrules.checkRules();
            if (res) {
                errormarker.clear();
            } else {
                errormarker.report(rowrules.getErrorMessages());
            }
            return res;
        } else {
            return true;
        }
    }
}
