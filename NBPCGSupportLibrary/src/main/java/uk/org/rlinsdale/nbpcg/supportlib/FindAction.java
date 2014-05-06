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
package uk.org.rlinsdale.nbpcg.supportlib;

import java.util.List;
import java.util.logging.Level;
import uk.org.rlinsdale.nbpcg.annotations.RegisterLog;

/**
 * An Abstract Class used to find a set of values.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <C> The class of the values found
 */
@RegisterLog("uk.org.rlinsdale.nbpcg.supportlib.finder")
public abstract class FindAction<C> {

    private final String name;

    /**
     * Constructor
     *
     * @param name the action's name
     */
    public FindAction(String name) {
        this.name = name;
    }

    /**
     * Get the set of values returned by this action.
     *
     * @return the set of values
     */
    public List<C> find() {
        List<C> res = findValues();
        Log.get("uk.org.rlinsdale.nbpcg.supportlib.finder").log(Level.FINE, "Find: {0} called - returning {1} values", new Object[]{name, res.size()});
        return res;
    }

    /**
     * Get the set of values returned by this action.
     *
     * @return the set of values
     */
    public abstract List<C> findValues();
}
