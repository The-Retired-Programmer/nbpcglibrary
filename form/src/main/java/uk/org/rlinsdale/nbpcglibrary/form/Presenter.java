/*
 * Copyright (C) 2016 Richard Linsdale.
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
package uk.org.rlinsdale.nbpcglibrary.form;

/**
 * Presenter
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <V> the view class
 */
public interface Presenter<V> {
    
    /**
     * Get the view associated with this presenter
     *
     * @return the view
     */
    public V getView();
    
    /**
     * Check that the MVP rules are ok.
     *
     * @param sb StringBuilder object to which error messages can be added if
     * test fails
     * @return true if all rules are ok.
     */
    public boolean test(StringBuilder sb);

    /**
     * Enable the View
     */
    public void enableView();
    
    /**
     * Refresh the View (using data from model)
     */
    public void refreshView();
}
