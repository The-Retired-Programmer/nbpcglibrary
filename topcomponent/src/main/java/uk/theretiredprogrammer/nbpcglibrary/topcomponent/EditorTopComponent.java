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
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams;
import static uk.theretiredprogrammer.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.REMOVE;
import uk.theretiredprogrammer.nbpcglibrary.form.PanePresenter;

/**
 * Editor -  Topcomponent which displays/edits an entity.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the class of the entity being edited
 */
public abstract class EditorTopComponent<E extends Entity> extends DisplayTopComponent {
    private boolean abandon = false;
    private EntityStateChangeListener statechangelistener;

    /**
     * the entity being edited
     */
    protected final E entity;

    /**
     * Constructor
     *
     * @param entity the entity being edited
     * @param name the topcomponent name
     * @param hint the topcomponent hint
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public EditorTopComponent(E entity, String name, String hint) {
        super(name, hint);
        this.entity = entity;
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
        entity.addStateListener(statechangelistener = new EntityStateChangeListener("TopComponent:" + entity.instanceDescription()));
    }

    @Override
    protected void closed() {
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
                EditorTopComponent.this.close();
            }
        }
    }
}
