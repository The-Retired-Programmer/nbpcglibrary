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
package uk.org.rlinsdale.nbpcglibrary.node.nodes;

import uk.org.rlinsdale.nbpcglibrary.common.EventParams;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * Listener Parameter Class for NameChange listeners.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class NameChangeEventParams implements EventParams {

    private final String name;

    /**
     * Constructor.
     *
     * @param name the name
     */
    public NameChangeEventParams(String name) {
        this.name = name;
    }
    
    @Override
    public String classDescription() {
        return LogBuilder.classDescription(this, name);
    }

    /**
     * Get the name.
     *
     * @return the name
     */
    public String get() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof NameChangeEventParams) {
            return name.equals(((NameChangeEventParams) obj).name);
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
