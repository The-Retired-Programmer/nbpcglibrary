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

import java.awt.EventQueue;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultEditorKit;
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterLog;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * Top component which displays an explorer object
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
@RegisterLog("nbpcglibrary.topcomponent")
public abstract class ExplorerTopComponent extends TopComponent implements ExplorerManager.Provider, HasInstanceDescription {

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
    @SuppressWarnings("LeakingThisInConstructor")
    public ExplorerTopComponent(String topComponentName, JScrollPane viewComponent, String name, String hint) {
        this.topComponentName = topComponentName;
        LogBuilder.writeConstructorLog("nbpcglibrary.topcomponent", this, name, hint);
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
        LogBuilder.writeLog("nbpcglibrary.topcomponent", this, "componentOpened");
        em.setRootContext(getRootContextNode());
    }

    @Override
    public void componentClosed() {
        LogBuilder.writeLog("nbpcglibrary.topcomponent", this, "componentClosed");
        dropRootContextNode();
        em.setRootContext(Node.EMPTY);
    }
    
    @Override
    public String toString() {
        return topComponentName;
    }
    
    /**
     * Reset the RootContext node (and refresh display)
     * 
     * @param rootcontext the new Root Context node
     */
    public void resetRootContextNode(AbstractNode rootcontext){
         if (EventQueue.isDispatchThread()) {
            em.setRootContext(rootcontext);
        } else {
            EventQueue.invokeLater(new SetRootContext(rootcontext));
        }
    }
    
    private class SetRootContext implements Runnable {

        private final AbstractNode rootcontext;

        public SetRootContext(AbstractNode rootcontext) {
            this.rootcontext = rootcontext;
        }

        @Override
        public void run() {
             em.setRootContext(rootcontext);
        }
    }

    /**
     * Get the Root Node
     *
     * @return the root node
     */
    public abstract Node getRootContextNode();

    /**
     * Drop the Root Node
     */
    public void dropRootContextNode() {
        // empty default - can be overridden
    }
}
