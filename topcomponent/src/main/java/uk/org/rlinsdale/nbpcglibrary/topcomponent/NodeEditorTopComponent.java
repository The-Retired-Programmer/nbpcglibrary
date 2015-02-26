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
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRW;
import uk.org.rlinsdale.nbpcglibrary.form.Form;
import uk.org.rlinsdale.nbpcglibrary.node.nodes.TreeNodeRW;
import org.openide.windows.TopComponent;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * Editor Topcomponent which displays/edits a node.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the entity class
 * @param <F> the entity fields enum class
 */
public abstract class NodeEditorTopComponent<E extends EntityRW, F> extends TopComponent {

    private final String name;

    /**
     * the node being edited
     */
    protected final TreeNodeRW<E, F> node;

    /**
     * the entity being edited
     */
    protected E entity;
    
    /**
     * the instance content for dynamic additions to lookup
     */
    protected InstanceContent content;
    
    /**
     * Constructor
     *
     * @param node the node
     * @param name the topcomponent name
     * @param hint the topcomponent hint
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public NodeEditorTopComponent(TreeNodeRW<E, F> node, String name, String hint) {
        LogBuilder.writeConstructorLog("nbpcglibrary.topcomponent", this, node, name, hint);
        setName(name);
        setToolTipText(hint);
        content = new InstanceContent();
        associateLookup(new AbstractLookup(content));
        this.name = name;
        this.node = node;
    }

    /**
     * Open the Topcomponent and make it visible.
     */
    public void visible() {
        open();
        requestActive();
    }

    @Override
    public void componentOpened() {
        LogBuilder.create("nbpcglibrary.topcomponent", Level.FINE).addMethodName(this, "componentOpened")
                .addMsg("TopComponent is {0})", this).write();
        entity = node.getEntity();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(getForm());

    }

    /**
     * Get the form being displayed on this editor.
     *
     * @return the form
     */
    protected abstract Form getForm();
    
    @Override
    public boolean canClose() {
        return canCloseForm();
    }
    
    /**
     * Can Close the form
     *
     * @return true if form can be closed
     */
    protected abstract boolean canCloseForm();

    @Override
    public void componentClosed() {
        LogBuilder.create("nbpcglibrary.topcomponent", Level.FINE).addMethodName(this, "componentClosed")
                .addMsg("TopComponent is {0})", this).write();
        remove(dropForm());
        entity = null;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Drop the form
     *
     * @return the form
     */
    protected abstract Form dropForm();

}
