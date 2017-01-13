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
package uk.theretiredprogrammer.nbpcglibrary.form;

import java.util.List;
import javax.swing.JComponent;

/**
 * A View which implements a JPanel as its core component
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <V> the class of the inserted views
 */
public interface PaneView<V> {

    /**
     * Insert a set of child views into this view.
     * 
     * @param childviews the list of child views
     */
    public void insertChildViews(List<V> childviews);
    
    /**
     * Get the Component which is this View.
     *
     * @return the components
     */
    public JComponent getViewComponent();
}
