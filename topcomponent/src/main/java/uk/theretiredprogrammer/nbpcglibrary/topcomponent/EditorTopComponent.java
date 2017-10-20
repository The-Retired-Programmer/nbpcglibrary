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

import java.util.Arrays;
import java.util.List;
import org.openide.awt.StatusDisplayer;
import org.openide.nodes.Node;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
//import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams;
//import static uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.REMOVE;
import uk.theretiredprogrammer.nbpcglibrary.form.PanePresenter;

/**
 * Editor -  Topcomponent which displays/edits an entity.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <N> the class of the node being edited
 */
public abstract class EditorTopComponent<N extends Node> extends DisplayTopComponent {
    private boolean abandon = false;
    private EntityStateChangeListener statechangelistener;

    /**
     * the node being edited
     */
    protected final N node;

    /**
     * Constructor
     *
     * @param node the node to be edited
     * @param name the topcomponent name
     * @param hint the topcomponent hint
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public EditorTopComponent(N node, String name, String hint) {
        super(name, hint);
        this.node = node;
    }

    @Override
    protected List<ToolbarElement> getToolbarElements() {
        return Arrays.asList(
                new ToolbarElement("tick", "Test the data in this form", ()-> testaction()),
                new ToolbarElement("disk", "Save the data in this form", ()-> saveaction())
        );
    }
    
    private boolean testaction() {
        StringBuilder messages = new StringBuilder();
        boolean res = getPresenter().test(messages);
        if (!res) {
            StatusDisplayer.getDefault().setStatusText(messages.toString());
        }
        return res;
    }
    
    private boolean saveaction() {
        return testaction() && saveonlyaction() ;
    }
    
    private boolean saveonlyaction() {
        StringBuilder messages = new StringBuilder();
        boolean res = getPresenter().save(messages);
        if (!res) {
            StatusDisplayer.getDefault().setStatusText(messages.toString());
        }
        return res;
    }
    
    /**
     * Get the top level presenter for this editor form
     * 
     * @return the top level presenter
     */
    protected abstract PanePresenter getPresenter();

    @Override
    public boolean canClose() {
        return abandon ? true : saveaction();
    }

    @Override
    protected void opened() {
    }

    @Override
    protected void closed() {
        statechangelistener = null;
    }

    private class EntityStateChangeListener extends Listener {

        @Override
        public void action(Object p) {
//            if ( ((EntityStateChangeEventParams)p).getTransition() == REMOVE) {
//                abandon = true;
//                EditorTopComponent.this.close();
//            }
        }
    }
}
