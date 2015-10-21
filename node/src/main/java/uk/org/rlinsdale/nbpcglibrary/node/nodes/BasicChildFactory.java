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
package uk.org.rlinsdale.nbpcglibrary.node.nodes;

import org.openide.util.Lookup;
import uk.org.rlinsdale.nbpcglibrary.data.entity.CoreEntity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManager;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;
import uk.org.rlinsdale.nbpcglibrary.data.entityreferences.EntityReference;

/**
 * Extended ChildFactory support
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> the Parent Primary Key Class
 * @param <E> the Parent Entity Class
 * @param <P> The Parent of Parent Entity Class
 */
public abstract class BasicChildFactory<K, E extends Entity<K, E, P, ?>, P extends CoreEntity> extends CoreChildFactory<E> {

    private final EntityReference<K, E, P> parentref;

    /**
     * Constructor.
     *
     * @param factoryname the factory name
     * @param parentEntity the parent entity
     * @param emclass the parent entity manager class
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public BasicChildFactory(String factoryname, E parentEntity, Class<? extends EntityManager> emclass) {
        super(null);
        EntityManager<K, E, P> em = Lookup.getDefault().lookup(emclass);
        parentref = new EntityReference<>(factoryname + ">" + parentEntity.instanceDescription(), parentEntity, em);
    }

    @Override
    public E getParentEntity() {
        return parentref.get();
    }
}
