/*
 * Copyright (C) 2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import org.openide.windows.TopComponent;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * Topcomponent which can displays a defined swing component
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class DisplayTopComponent extends TopComponent {

    private final String name;
    private JComponent displaycomponent;

    /**
     * Constructor
     *
     * @param name the topcomponent name
     * @param hint the topcomponent hint
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public DisplayTopComponent( String name, String hint) {
        LogBuilder.writeConstructorLog("nbpcglibrary.topcomponent", this, name, hint);
        setName(name);
        setToolTipText(hint);
        this.name = name;
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
        LogBuilder.writeLog("nbpcglibrary.topcomponent", this, "componentOpened", name);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        displaycomponent = getDisplayComponent();
        add(displaycomponent);
        opened();
    }

    /**
     * Get the component to be displayed on this top component.
     *
     * @return the component to be displayed
     */
    protected abstract JComponent getDisplayComponent();

   /**
     * do any opening activities.
     *
     */
    protected abstract void opened();

    @Override
    public void componentClosed() {
        LogBuilder.writeLog("nbpcglibrary.topcomponent",this, "componentClosed", name);
        remove(displaycomponent);
        displaycomponent = null;
        closed();
    }
    
    /**
     * Do any closing activities.
     *
     */
    protected abstract void closed();
}
