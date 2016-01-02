/*
 * Copyright (C) 2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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

import uk.org.rlinsdale.nbpcglibrary.data.entity.Entity;

/**
 * Interface for Field creation and Datasources for Entity.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Entity Class
 */
public interface EntitySource<E extends Entity> {
    
    /**
     * Initialise the entity source (pre display)
     */
    public void opened();
    
    /**
     * terminate the entity source (post display)
     */
    public void closed();
    
    /**
     * Get the entity associated with this source
     * 
     * @return the entity
     */
    public E getEntity();
    
    /**
     * Get the Form fields associated with this source
     * 
     * @return the form fields
     */
    public FormFields getFormFields();
    
    /**
     * Get the row fields associated with this source
     * 
     * @return the row fields
     */
    public RowFields getRowFields();
   
}
