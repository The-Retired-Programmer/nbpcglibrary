/*
 * Copyright (C) 2014-2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.util.Arrays;
import java.util.List;
import org.openide.awt.StatusDisplayer;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams;
import static uk.org.rlinsdale.nbpcglibrary.data.entity.EntityStateChangeEventParams.EntityStateChange.REMOVE;
import uk.org.rlinsdale.nbpcglibrary.form.PanePresenter;

/**
 * Editor -  Topcomponent which displays/edits an entity.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
