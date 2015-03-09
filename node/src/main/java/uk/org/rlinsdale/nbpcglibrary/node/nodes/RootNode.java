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

import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;

/**
 * Root Node Abstract Class
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class
 */
public abstract class RootNode<E extends Entity> extends BasicNode<E> {

    private final E e;

    /**
     * Constructor
     *
     * @param e the entity
     * @param cf the childfactory
     * @param allowedPaste allowed paste actions
     */
    protected RootNode(E e, RootChildFactory<E> cf, DataFlavorAndAction[] allowedPaste) {
        super(cf, allowedPaste);
        this.e = e;
    }

    /**
     * Constructor.
     *
     * @param e the entity
     */
    protected RootNode(E e) {
        super();
        this.e = e;
    }

    @Override
    public E getEntity() {
        return e;
    }
}
