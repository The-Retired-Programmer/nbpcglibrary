/*
 * Copyright 2014-2017 Richard Linsdale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.nbpcglibrary.topcomponent;

import java.awt.EventQueue;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultEditorKit;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterLog;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;

/**
 * Top component which displays an explorer object
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@RegisterLog("nbpcglibrary.topcomponent")
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
    @SuppressWarnings("LeakingThisInConstructor")
    public ExplorerTopComponent(String topComponentName, JScrollPane viewComponent, String name, String hint) {
        this.topComponentName = topComponentName;
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
        em.setRootContext(getRootContextNode());
    }

    @Override
    public void componentClosed() {
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
