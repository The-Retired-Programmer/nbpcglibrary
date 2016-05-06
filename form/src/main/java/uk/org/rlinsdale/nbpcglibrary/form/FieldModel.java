/*
 * Copyright (C) 2015-2016 Richard Linsdale.
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

import java.util.List;

/**
 * The Field Model API
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the type of the field value
 */
public abstract class FieldModel<T> {

    /**
     * Set the value in this field model
     *
     * @param value the value
     */
    public abstract void set(T value);

    /**
     * Get the value for this field model
     *
     * @return the value
     */
    public abstract T get();

    /**
     * Test if null selection is allowed in choice field
     *
     * @return true if a null selection is allowed
     */
    public abstract boolean isNullSelectionAllowed();

    /**
     * Get the list of possible selection values for a choice field
     *
     * @return the list of choices
     */
    public abstract List<T> getChoices();

    /**
     * Check that the model rules are ok.
     *
     * @param sb A string builder into which any error messages are placed if
     * test fails
     * @return true if all rules are ok.
     */
    public abstract boolean test(StringBuilder sb);
}
