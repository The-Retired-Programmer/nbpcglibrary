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

import java.util.logging.Level;
import linsdale.nbpcg.annotations.RegisterLog;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
@RegisterLog("linsdale.nbpcg.supportlib.listener")
public abstract class Listener<P extends ListenerParams> {
    
    private final String description;
    
    public Listener(String description){
        this.description = description;
    }

    public void actionPerformed(P p) {
        Log.get("linsdale.nbpcg.supportlib.listener").log(Level.FINEST, "Listener {0}: action {1}", new Object[] {description, p});
        action(p);
    }
    
    @Override
    public String toString() {
        return description;
    }
    public abstract void action(P p) ;
    
}
