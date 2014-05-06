/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package uk.org.rlinsdale.nbpcg.formsupportlib;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Abstract Class representing a Field on a Form
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class BaseField {

    /**
     * the label text associated with this field
     */
    protected final String label;

    /**
     * Constructor
     *
     * @param label the label text for this field
     */
    public BaseField(String label) {
        this.label = label;
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
    public JComponent getComponent() {
        return null;
    }

    /**
     * Get the optional additional Jomponent which is displayed to the right of
     * the value field. This can take the form of a button or other component.
     *
     * @return the additional component (or null)
     */
    public JComponent getAdditionalComponent() {
        return null;
    }

    /**
     * Get an array of Components which make up the Field. The array will be in
     * left to right display order.
     *
     * @return an array of components
     */
    public JComponent[] getComponents() {
        return new JComponent[]{
            getLabelComponent(),
            getComponent(),
            getAdditionalComponent()
        };
    }
}
