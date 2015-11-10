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
package uk.org.rlinsdale.nbpcglibrary.topcomponent;

import java.util.logging.Level;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcglibrary.form.Form;
import org.openide.windows.TopComponent;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.data.entity.CoreEntity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.REMOVE;
import uk.org.rlinsdale.nbpcglibrary.node.nodes.TreeNode;

/**
 * Editor Topcomponent which displays/edits a node.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <K> the primary Key class for this entity
 * @param <E> the entity class
 * @param <P> the parent entity class
 * @param <F> the fields enum for this entity
 */
public abstract class NodeEditorTopComponent<K, E extends Entity<K,E,P,F>, P extends CoreEntity, F> extends DisplayTopComponent {

    private boolean abandon = false;
    private EntityStateChangeListener statechangelistener;

    /**
     * the node being edited
     */
    protected final TreeNode<K, E, P, F> node;

    /**
     * the entity being edited
     */
    protected E entity;

    /**
     * Constructor
     *
     * @param node the node
     * @param name the topcomponent name
     * @param hint the topcomponent hint
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public NodeEditorTopComponent(TreeNode<K, E, P, F> node, String name, String hint) {
        super(name,hint);
        this.node = node;
        entity = node.getEntity();
    }

    @Override
    public boolean canClose() {
        return abandon ? true : canCloseForm();
    }

    /**
     * Can Close the form
     *
     * @return true if form can be closed
     */
    protected abstract boolean canCloseForm();

    @Override
    protected void opened() {
        entity = node.getEntity();
        entity.addStateListener(statechangelistener = new EntityStateChangeListener("TopComponent:" + entity.instanceDescription()));
    }
    
    @Override
    protected void closed() {
        entity = null;
        statechangelistener = null;
    }
    
    private class EntityStateChangeListener extends Listener<EntityStateChangeEventParams> {

        public EntityStateChangeListener(String name) {
            super(name);
        }

        @Override
        public void action(EntityStateChangeEventParams p) {
            if (p.getTransition() == REMOVE) {
                abandon = true;
                NodeEditorTopComponent.this.close();
            }
        }
    }
}
