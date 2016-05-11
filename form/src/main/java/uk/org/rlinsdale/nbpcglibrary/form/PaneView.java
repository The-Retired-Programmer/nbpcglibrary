/*
 * Copyright (C) 2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.org.rlinsdale.nbpcglibrary.form;

import java.util.List;
import javax.swing.JComponent;

/**
 * A View which implements a JPanel as its core component
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
