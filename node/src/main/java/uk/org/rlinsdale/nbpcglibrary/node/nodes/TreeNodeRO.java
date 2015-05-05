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
package uk.org.rlinsdale.nbpcglibrary.node.nodes;

import java.io.IOException;
import java.util.logging.Level;
import org.openide.util.Exceptions;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityManagerRO;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRO;
import uk.org.rlinsdale.nbpcglibrary.data.entityreferences.EntityReference;

/**
 * Read-Only Tree Node Abstract Class
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class
 */
public abstract class TreeNodeRO<E extends EntityRO> extends BasicNode<E> {

    private EntityReference<E> eref;

    /**
     * Constructor.
     *
     * @param  nodename the node name
     * @param e the entity
     * @param cf the childfactory
     * @param emclass the entity manager class
     * @param allowedPaste allowed paste actions
     */
    @SuppressWarnings("LeakingThisInConstructor")
    protected TreeNodeRO(String nodename, E e, BasicChildFactory<E> cf, Class<? extends EntityManagerRO> emclass, DataFlavorAndAction[] allowedPaste) {
        super(cf, allowedPaste);
        try {
            eref = new EntityReference<>(nodename, e, emclass);
        } catch (IOException ex) {
            LogBuilder.create("nbpcglibrary.node", Level.SEVERE).addConstructorName(this, nodename, e, cf)
                            .addExceptionMessage(ex).write();
        }
    }

    /**
     * Constructor.
     *
     * @param nodename the node name
     * @param e the entity
     * @param emclass the entity manager class
     */
    protected TreeNodeRO(String nodename, E e, Class<? extends EntityManagerRO> emclass) {
        super();
        try {
            eref = new EntityReference<>(nodename, e, emclass);
        } catch (IOException ex) {
           LogBuilder.create("nbpcglibrary.node", Level.SEVERE).addConstructorName(this, nodename, e)
                            .addExceptionMessage(ex).write();
        }
    }

    @Override
    public E getEntity() {
        try {
            return eref.get();
        } catch (IOException ex) {
            LogBuilder.create("nbpcglibrary.node", Level.SEVERE).addMethodName(this, "getEntity")
                            .addExceptionMessage(ex).write();
            return null;
        }
    }

    /**
     *
     */
    public void setNoEntity() {
        try {
            eref.set();
        } catch (IOException ex) {
            LogBuilder.create("nbpcglibrary.node", Level.SEVERE).addMethodName(this, "setNoEntity")
                            .addExceptionMessage(ex).write();
        }
    }

    /**
     * Get the display title for this node.
     * 
     * @return the title
     */
    public abstract String getDisplayTitle();

}