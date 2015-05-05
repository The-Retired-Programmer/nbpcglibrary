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

import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.JLabel;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;

/**
 * Abstract Class representing a Field on a Form
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> type of the data connecting to the backing Object
 */
public abstract class Field<T> implements HasInstanceDescription {

    private final FieldBackingObject<T> backingObject;
    private final String label;
    private final JComponent labelfield;
    private final JComponent field;
    private final JComponent additionalfield;
    private final ErrorMarker errorMarker = new ErrorMarker();
    private WeakReference<FieldsDef> parent = new WeakReference<>(null);

    /**
     * Constructor
     *
     * @param backingObject the backingobject
     * @param label the label text for this field
     * @param field the actual field to be used
     * @param additionalfield optional additional field (set to null if not
     * required)
     */
    public Field(FieldBackingObject<T> backingObject, String label, JComponent field, JComponent additionalfield) {
        this.label = label;
        labelfield = new JLabel(label);
        this.field = field;
        this.additionalfield = additionalfield;
        this.backingObject = backingObject;
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, label);
    }
    
    void setParent(FieldsDef parent){
        this.parent = new WeakReference<>(parent);
    }

    /**
     * Get an array of Components which make up the Field. The array will be in
     * left to right display order.
     *
     * @return an array of components
     */
    JComponent[] getComponents() {
        return new JComponent[]{labelfield, field, additionalfield, errorMarker};
    }

    /**
     * request that the value in the field is updated from the value in the
     * Backing Object
     */
    public void updateFieldFromBackingObject() {
        setField(backingObject.get());
    }

    /**
     * request that the backing bean has its value updated with the current
     * value of the field
     */
    public void updateBackingObjectFromField() {
        // a null action for a RO field - overwrite for a RW field
    }

    /**
     * Set a value in the field
     *
     * @param value the value to setField into the field
     */
    abstract void setField(T value);

    /**
     * Check if all rules in the field's rule set are valid, and update error
     * markers and error messages on the form.
     *
     * @return true if all rules are valid
     */
    public boolean checkRules() {
        boolean res = true;
        if (backingObject != null) {
            res = backingObject.checkRules();
            if (res) {
                errorMarker.clearError();
            } else {
                errorMarker.setError(backingObject.getErrorMessages());
            }
        }
        // now try and do the fieldsdef check as well
        FieldsDef fdef = parent.get();
        if (fdef != null) {
            fdef.checkFieldsDefRules(); // do check - will update the field defs reporting
        }
        return res;
    }
}
