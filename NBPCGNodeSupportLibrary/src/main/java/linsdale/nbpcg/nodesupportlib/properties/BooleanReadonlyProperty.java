/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;

/**
 * Boolean Readonly Property item
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class BooleanReadonlyProperty extends PropertySupport.ReadOnly<String> {

    private String val;

    /**
     * Constructor
     * @param name the property name
     * @param val the property value
     */
    public BooleanReadonlyProperty(String name, Boolean val) {
        super(name, String.class, name, name);
        setName(name);
        update(val);
    }
    
    /**
     * Constructor
     * @param name the property name
     * @param val the property value
     */
    public BooleanReadonlyProperty(String name, boolean val) {
        super(name, String.class, name, name);
        setName(name);
        update(val);
    }
    
    public final void update(boolean val) {
        this.val = val?"Yes":"No";
    }

    /**
     * Get the Property Value
     * @return the property value
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        return val;
    }
}
