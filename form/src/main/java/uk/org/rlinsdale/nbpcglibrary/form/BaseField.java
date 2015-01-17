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
import uk.org.rlinsdale.nbpcglibrary.common.LogHelper;

/**
 * Abstract Class representing a Field on a Form
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param  <T> type of the data connecting to the backing Object
 */
public abstract class BaseField<T> implements LogHelper {

    private final BaseFieldBackingObject<T> backingObject;

    /**
     * the label text associated with this field
     */
    private final String label;

    /**
     * Constructor
     *
     * @param backingObject the backingobject
     * @param label the label text for this field
     */
    public BaseField(BaseFieldBackingObject<T> backingObject, String label) {
        this.label = label;
        this.backingObject = backingObject;
    }

    @Override
    public String classDescription() {
        return LogBuilder.classDescription(this, label);
    }

    /**
     * Get the label text for this field
     *
     * @return the label text
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the Label Component
     *
     * @return the label component
     */
    public final JComponent getLabelComponent() {
        return new JLabel("".equals(label) ? " " : label + ":");
    }

    /**
     * Get the Field Component
     *
     * @return the working component in which the value is displayed
     */
    JComponent getComponent() {
        return null;
    }

    /**
     * Get the optional additional Jomponent which is displayed to the right of
     * the value field. This can take the form of a button or other component.
     *
     * @return the additional component (or null)
     */
    JComponent getAdditionalComponent() {
        return null;
    }

    /**
     * Get an array of Components which make up the Field. The array will be in
     * left to right display order.
     *
     * @return an array of components
     */
    JComponent[] getComponents() {
        return new JComponent[]{
            getLabelComponent(),
            getComponent(),
            getAdditionalComponent()
        };
    }

    /**
     * request that the value in the field is updated from the value in the
     * Backing Object
     */
    public void updateFieldFromBackingObject() {
        setField(backingObject.get());
    }

    /**
     * Set a value in the field
     *
     * @param value the value to setField into the field
     */
    abstract void setField(T value);

}
