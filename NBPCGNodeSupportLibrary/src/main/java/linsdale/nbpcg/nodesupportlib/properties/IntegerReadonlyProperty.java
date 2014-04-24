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
package linsdale.nbpcg.nodesupportlib.properties;

import org.openide.nodes.PropertySupport;

/**
 * Integer Readonly Property item
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class IntegerReadonlyProperty extends PropertySupport.ReadOnly<String> {

    private String val;

    /**
     * Constructor
     *
     * @param name the property name
     * @param val the property value
     */
    public IntegerReadonlyProperty(String name, Integer val) {
        super(name, String.class, name, name);
        setName(name);
        update(val);
    }
    
    /**
     * Constructor
     *
     * @param name the property name
     * @param val the property value
     */
    public IntegerReadonlyProperty(String name, int val) {
        super(name, String.class, name, name);
        setName(name);
        update(val);
    }
    
    /**
     * Update the value
     *
     * @param val the value used to update
     */
    public final void update(int val) {
        this.val = Integer.toString(val);
    }

    @Override
    public String getValue() {
        return val;
    }
}
