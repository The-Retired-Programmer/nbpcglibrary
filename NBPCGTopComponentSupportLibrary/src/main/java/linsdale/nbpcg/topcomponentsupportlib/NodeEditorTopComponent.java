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
package linsdale.nbpcg.topcomponentsupportlib;

import java.util.logging.Level;
import javax.swing.BoxLayout;
import linsdale.nbpcg.datasupportlib.entity.EntityRW;
import linsdale.nbpcg.formsupportlib.Form;
import linsdale.nbpcg.nodesupportlib.nodes.TreeNodeRW;
import linsdale.nbpcg.supportlib.Log;
import org.openide.windows.TopComponent;

/**
 * Editor Topcomponent which displays/edits a node.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the entity class
 */
public abstract class NodeEditorTopComponent<E extends EntityRW> extends TopComponent {

    private final String name;

    /**
     * the node being edited
     */
    protected final TreeNodeRW<E> node;

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
    public NodeEditorTopComponent(TreeNodeRW<E> node, String name, String hint) {
        Log.get("linsdale.nbpcg.topcomponentsupportlib").log(Level.FINE, "NodeEditorTopComponent for {0}: TopComponent created", name);
        setName(name);
        setToolTipText(hint);
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
        Log.get("linsdale.nbpcg.topcomponentsupportlib").log(Level.FINE, "NodeEditorTopComponent for {0}: TopComponent opened()", name);
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
    public void componentClosed() {
        Log.get("linsdale.nbpcg.topcomponentsupportlib").log(Level.FINE, "NodeEditorTopComponent for {0}: TopComponent closed()", name);
        remove(dropForm());
        entity = null;
    }

    /**
     * Drop the form
     *
     * @return the form
     */
    protected abstract Form dropForm();

}
