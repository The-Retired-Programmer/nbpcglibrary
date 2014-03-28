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

import linsdale.nbpcg.datasupportlib.entity.EntityManagerRO;
import linsdale.nbpcg.datasupportlib.entity.EntityRO;
import linsdale.nbpcg.datasupportlib.entityreferences.EntityReference;

/**
 * class providing extended ChildFactory support
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 * @param <E>
 */
public abstract class BasicChildFactory<E extends EntityRO> extends RootChildFactory<E> {

    private final EntityReference<E> parentref;
    
    public BasicChildFactory(String factoryname, E parentEntity,Class<? extends EntityManagerRO> emclass ) {
        super(null);
        parentref = new EntityReference<>(factoryname+"/parent",parentEntity,emclass);
    }
    
    @Override
    public E getParentEntity() {
        return parentref.get();
    }
}
