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

    private final String label;
    private final JComponent labelfield;
    private final JComponent field;
    private final JComponent additionalfield;
    private final JComponent errorMarker;

    /**
     * Constructor
     *
     * @param label the label text for this field
     * @param field the actual field to be used
     * @param additionalfield optional additional field (set to null if not
     * required)
     * @param errorMarker the field to be used to display the error marker
     */
    public Field(String label, JComponent field, JComponent additionalfield, JComponent errorMarker) {
        this.label = label;
        labelfield = new JLabel(label);
        this.field = field;
        this.additionalfield = additionalfield;
        this.errorMarker = errorMarker;
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, label);
    }

    /**
     * Get an array of Components which make up the Field. The array will be in
     * left to right display order.
     *
     * @return an array of components
     */
    protected JComponent[] getComponents() {
        return new JComponent[]{labelfield, field, additionalfield, errorMarker};
    }

    /**
     * Request that the value in the field is updated from the value in the
     * source.
     */
    protected void updateFieldFromSource() {
        setFieldValue(getSourceValue());
    }

    /**
     * Get the value from the source
     *
     * @return the value
     */
    abstract protected T getSourceValue();

    /**
     * Set a value into the Field
     *
     * @param value the value to be inserted into the Field
     */
    abstract protected void setFieldValue(T value);
    
}
