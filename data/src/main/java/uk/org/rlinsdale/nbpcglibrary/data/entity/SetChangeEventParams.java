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
package uk.org.rlinsdale.nbpcglibrary.data.entity;

import uk.org.rlinsdale.nbpcglibrary.common.EventParams;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * The Parameter Class for a SetChange listener.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <F> the Fields enum class
 */
public class SetChangeEventParams<F> implements EventParams {

    private final F set;

    /**
     * Constructor.
     *
     * @param set the Id of the set
     */
    public SetChangeEventParams(F set) {
        this.set = set;
    }

    /**
     * Get the Id of the set.
     *
     * @return the Id
     */
    public F get() {
        return set;
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, set.toString() + " change");
    }
}
