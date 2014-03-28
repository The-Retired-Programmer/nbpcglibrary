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
package linsdale.nbpcg.nodesupportlib.nodes;

import linsdale.nbpcg.datasupportlib.entity.Entity;
import linsdale.nbpcg.datasupportlib.entity.SetChangeListenerParams;
import linsdale.nbpcg.supportlib.Listener;
import org.openide.nodes.ChildFactory;

/**
 * class providing extended ChildFactory support
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
public abstract class RootChildFactory<E extends Entity> extends ChildFactory<Entity> {

    private ChildListener childListener;
    private final E parentEntity;
    
    public RootChildFactory(E parentEntity) {
        this.parentEntity = parentEntity;
    }
    
    public E getParentEntity() {
        return parentEntity;
    }

    public void enableChangeListening(String name, Entity parent) {
        childListener = new ChildListener(name);
        parent.addSetChangeListener(childListener);
    }

    private class ChildListener extends Listener<SetChangeListenerParams> {

        public ChildListener(String name) {
            super(name);
        }

        @Override
        public void action(SetChangeListenerParams p) {
            refresh(true); 
        }
    }
}
