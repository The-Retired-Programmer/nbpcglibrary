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

import java.util.List;
import java.util.function.Supplier;

/**
 * PanePresenter - a presenter for a view which is a JPanel
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <C> the child presenter class
 */
public interface PanePresenter<C> extends Presenter<PaneView> {

    /**
     * Define a function to be used to get the set of child presenters
     * @param getchildpresentersfunction the function
     */
    public void setGetChildPresentersFunction(Supplier<List<C>> getchildpresentersfunction);
    
    /**
     * Apply the save action for the MVP
     *
     * @param sb StringBuilder object to which error messages can be added if
     * save fails
     * @return true if save is ok
     */
    public boolean save(StringBuilder sb);

}
