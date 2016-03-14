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

import uk.org.rlinsdale.nbpcglibrary.common.Rules;

/**
 * The Field Source - Basic implementation
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> the type of the source value
 */
public class FieldSource<T> {

    private T value; // data source
    private final Rules rules = new Rules();

    /**
     * Set the value in this field source
     * @param value the value
     */
    public void set(T value) {
        this.value = value;
    }

    /**
     * Get the rules associated with this field source
     * @return the set of rules
     */
    public Rules getRules() {
        return rules;
    }

    /**
     * Get the value for this field source
     * @return the value
     */
    public T get() {
        return value;
    }
}
