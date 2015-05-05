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

import java.io.IOException;
import java.util.logging.Level;
import org.openide.util.Exceptions;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManagerRO;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRO;
import uk.org.rlinsdale.nbpcglibrary.data.entityreferences.EntityReference;

/**
 * Extended ChildFactory support
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Parent Entity Class
 */
public abstract class BasicChildFactory<E extends EntityRO> extends RootChildFactory<E> {

    private EntityReference<E> parentref;

    /**
     * Constructor.
     * 
     * @param factoryname the factory name
     * @param parentEntity the parent entity
     * @param emclass the parent entity manager class 
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public BasicChildFactory(String factoryname, E parentEntity, Class<? extends EntityManagerRO> emclass) {
        super(null);
        try {
            parentref = new EntityReference<>( factoryname+">"+parentEntity.instanceDescription(), parentEntity, emclass);
        } catch (IOException ex) {
            LogBuilder.create("nbpcglibrary.node", Level.SEVERE).addConstructorName(this, factoryname, parentEntity)
                            .addExceptionMessage(ex).write();
        }
    }

    @Override
    public E getParentEntity() {
        try {
            return parentref.get();
        } catch (IOException ex) {
            LogBuilder.create("nbpcglibrary.node", Level.SEVERE).addMethodName(this, "getParentEntity")
                            .addExceptionMessage(ex).write();
            return null;
        }
    }
}
