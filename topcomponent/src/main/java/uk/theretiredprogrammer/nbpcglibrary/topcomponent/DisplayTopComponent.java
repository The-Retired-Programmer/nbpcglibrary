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

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.openide.windows.TopComponent;

/**
 * Topcomponent which can displays a defined swing component
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public abstract class DisplayTopComponent extends TopComponent {

    private final String name;
    private JComponent displaycomponent;
    private JToolBar toolbar;

    /**
     * Constructor
     *
     * @param name the topcomponent name
     * @param hint the topcomponent hint
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public DisplayTopComponent(String name, String hint) {
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
        setLayout(new BorderLayout());
        toolbar = new JToolBar("editor toolbar");
        List<ToolbarElement> ltbe = getToolbarElements();
        if (!ltbe.isEmpty()) {
            ltbe.stream().forEach(tbe -> toolbar.add(tbe.getToolbarButton()));
            add(toolbar, BorderLayout.PAGE_START);
        }
        displaycomponent = getDisplayComponent();
        add(displaycomponent, BorderLayout.CENTER);
        opened();
    }

    /**
     * Get the component to be displayed on this top component.
     *
     * @return the component to be displayed
     */
    protected abstract JComponent getDisplayComponent();

    /**
     * Get the toolbar elements to be displayed on this top component.
     *
     * @return the component to be displayed
     */
    protected abstract List<ToolbarElement> getToolbarElements();

    /**
     * do any opening activities.
     *
     */
    protected abstract void opened();

    @Override
    public void componentClosed() {
        closed();
        remove(displaycomponent);
        remove(toolbar);
        displaycomponent = null;
    }

    /**
     * Do any closing activities.
     *
     */
    protected abstract void closed();
}
