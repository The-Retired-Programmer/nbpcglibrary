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
package linsdale.nbpcg.supportlib;

import java.util.List;
import java.util.logging.Level;
import linsdale.nbpcg.annotations.RegisterLog;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
@RegisterLog("linsdale.nbpcg.supportlib.finder")
public abstract class FindAction<C> {

    private final String name;

    public FindAction(String name) {
        this.name = name;
    }

    public List<C> find() {
        List<C> res = findValues();
        Log.get("linsdale.nbpcg.supportlib.finder").log(Level.FINE, "Find: {0} called - returning {1} values", new Object[]{name, res.size()});
        return res;
    }

    public abstract List<C> findValues();
}
