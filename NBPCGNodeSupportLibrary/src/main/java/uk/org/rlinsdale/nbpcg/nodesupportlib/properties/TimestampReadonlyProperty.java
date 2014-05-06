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
package uk.org.rlinsdale.nbpcg.nodesupportlib.properties;

import uk.org.rlinsdale.nbpcg.supportlib.Timestamp;
import org.openide.nodes.PropertySupport;

/**
 * Timestamp Readonly Property item
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class TimestampReadonlyProperty extends PropertySupport.ReadOnly<String> {

    private String val;

    /**
     * Constructor
     *
     * @param name the property name
     * @param val the property value
     */
    public TimestampReadonlyProperty(String name, Timestamp val) {
        super(name, String.class, name, name);
        setName(name);
        update(val);
    }
    
    /**
     * Update the value
     *
     * @param val the value used to update
     */
    public final void update(Timestamp val) {
        this.val = val.toString();
    }

    @Override
    public String getValue() {
        return val;
    }
}
