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

/**
 * JPanelPresenter - a presenter for a view which is a JPanel
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <C> the child presenter class
 */
public interface JPanelPresenter<C> extends Presenter<JPanelView> {

    /**
     * Set the  set of child presenters to be used with this panel
     * 
     * @param childpresenters a set of child presenters (as a set of parameters)
     */
    public void setChildPresenters(C... childpresenters);

    /**
     * Set the  set of child presenters to be used with this panel
     * 
     * @param childpresenters a list of child presenters
     */
    public void setChildPresenters(List<C> childpresenters);
    
    /**
     * Apply the save action for the MVP
     *
     * @param sb StringBuilder object to which error messages can be added if
     * save fails
     * @return true if save is ok
     */
    public boolean save(StringBuilder sb);

}
