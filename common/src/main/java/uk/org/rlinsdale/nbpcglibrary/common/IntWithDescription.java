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
package uk.org.rlinsdale.nbpcglibrary.common;

/**
 * An Integer with an associated text field - used for providing textual context
 * to indices.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class IntWithDescription {

    /**
     * A shared NULL IntWithDescription
     */
    public final static IntWithDescription NULL = new IntWithDescription(-1, "NULL");

    private final int val;
    private final String description;

    /**
     * Constructor
     *
     * @param val the integer key
     * @param description the associated descriptive string - used for error /
     * log message generation.
     */
    public IntWithDescription(int val, String description) {
        this.val = val;
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public int hashCode() {
        return val;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        final IntWithDescription other = (IntWithDescription) obj;
        return this.val == other.val;
    }

    /**
     * Test if this object's key equals a given int.
     *
     * @param i the given value
     * @return true if equal
     */
    public boolean equals(int i) {
        return val == i;
    }
}
