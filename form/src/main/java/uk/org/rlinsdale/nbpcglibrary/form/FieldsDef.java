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
import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.HasInstanceDescription;

/**
 * A collection of a setField of fields - for use in defining the fields content
 * of a form segment.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class FieldsDef implements HasInstanceDescription {

    private final List<Field> fields = new ArrayList<>();
    private String[] parameters;
    private final ErrorMarker errorMarker = new ErrorMarker();
    private final FieldsDefBackingObject backingObject;

    /**
     * Constructor
     *
     */
    public FieldsDef() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param backingObject the backingObject for the fieldsDef - used to access
     * fieldsdef level validation.
     */
    public FieldsDef(FieldsDefBackingObject backingObject) {
        this.backingObject = backingObject;
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this);
    }

    public JComponent[] getErrorMarkerRow() {
        return new JComponent[]{
            null,
            null,
            null,
            errorMarker
        };
    }

    /**
     * add a field to this collection.
     *
     * @param f the field to add
     */
    public final void add(Field f) {
        fields.add(f);
        f.setParent(this);
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
    public final void updateAllFieldsFromBackingObjects() {
        fields.stream().forEach((f) -> {
            f.updateFieldFromBackingObject();
        });
    }

    /**
     * Set the values of all fields into backingObjects.
     */
    public final void updateAllBackingObjectsFromFields() {
        fields.stream().forEach((f) -> {
            f.updateBackingObjectFromField();
        });
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
        for (Field field : fields) {
            if (!field.checkRules()) {
                valid = false;
            }
        }
        if (!checkFieldsDefRules()) {
            valid = false;
        }
        return valid;
    }

    public boolean checkFieldsDefRules() {
        if (backingObject != null) {
            boolean res = backingObject.checkRules();
            if (res) {
                errorMarker.clearError();
            } else {
                errorMarker.setError(backingObject.getErrorMessages());
            }
            return res;
        }
        return true;
    }
}
