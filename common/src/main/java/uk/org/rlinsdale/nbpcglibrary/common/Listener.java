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
package uk.org.rlinsdale.nbpcglibrary.common;

/**
 * A Standard Listener - defined with a generic class for use as parameters
 * passed on action.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <P> the Class of the listener parameter
 */
public abstract class Listener<P extends EventParams> implements LogHelper {

    private final String description;

    /**
     * Constructor
     *
     * @param description the listener's descriptive name - for use in error
     * /log reporting
     */
    public Listener(String description) {
        this.description = description;
        LogBuilder.writeEnteringConstructorLog("nbpcglibrary.common", this, description);
    }
    
    
    @Override
    public String classDescription() {
        return LogBuilder.classDescription(this, description);
    }

    /**
     * Fired on Action.
     *
     * @param p the listener parameters
     */
    public void actionPerformed(P p) {
        LogBuilder.writeEnteringLog("nbpcglibrary.common",this, "actionPerformed", p);
        action(p);
    }

    @Override
    public String toString() {
        return description;
    }

    /**
     * The action processing method.
     *
     * @param p the listener parameters
     */
    public abstract void action(P p);
}
