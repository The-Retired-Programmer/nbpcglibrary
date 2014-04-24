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
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultEditorKit;
import linsdale.nbpcg.annotations.RegisterLog;
import linsdale.nbpcg.supportlib.Log;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;

/**
 * Top component which displays an explorer object
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
@RegisterLog("linsdale.nbpcg.topcomponentsupportlib")
public abstract class ExplorerTopComponent extends TopComponent implements ExplorerManager.Provider {

    private final ExplorerManager em = new ExplorerManager();
    private final String topComponentName;

    /**
     * Constructor
     *
     * @param topComponentName the name for this topcomponent
     * @param viewComponent the explorer view component
     * @param name the topcomponent name
     * @param hint the topcomponent hint
     */
    public ExplorerTopComponent(String topComponentName, JScrollPane viewComponent, String name, String hint) {
        this.topComponentName = topComponentName;
        Log.get("linsdale.nbpcg.topcomponentsupportlib").log(Level.FINE, "ExplorerTopComponent for {0}: TopComponent created", topComponentName);
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(viewComponent, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(viewComponent, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        setName(name);
        setToolTipText(hint);
        ActionMap map = getActionMap();
        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(em));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(em));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(em));
        map.put("delete", ExplorerUtils.actionDelete(em, true));
        associateLookup(ExplorerUtils.createLookup(em, map));
    }

    /**
     * Open the Topcomponent and make it visible.
     */
    public void visible() {
        open();
        requestActive();
    }

    /**
     * Get the explorer manager being used for this topcomponent.
     *
     * @return the explorer manager
     */
    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    @Override
    public void componentOpened() {
        Logger log = Log.get("linsdale.nbpcg.topcomponentsupportlib");
        log.log(Level.FINE, "ExplorerTopComponent for {0}: Component Opened", topComponentName);
        em.setRootContext(getRootContextNode());
    }

    @Override
    public void componentClosed() {
        Logger log = Log.get("linsdale.nbpcg.topcomponentsupportlib");
        log.log(Level.FINE, "ExplorerTopComponent for {0}: Component Closed", topComponentName);
        dropRootContextNode();
        em.setRootContext(Node.EMPTY);
    }

    /**
     * Get the Root Node
     *
     * @return the root node
     */
    public abstract AbstractNode getRootContextNode();

    /**
     * Drop the Root Node
     */
    public void dropRootContextNode() {
        // empty default - can be overridden
    }
}
