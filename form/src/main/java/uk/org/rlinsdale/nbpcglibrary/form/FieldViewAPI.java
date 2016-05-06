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
package uk.org.rlinsdale.nbpcglibrary.form;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcglibrary.api.BadFormatException;

/**
 * API for a Field View
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> type of the data contained in the field
 */
public interface FieldViewAPI<T> {

    /**
     * Get an list of Components which make up the View. The list will be in
     * left to right display order.
     *
     * @return an list of components
     */
    public List<JComponent> getViewComponents();

    /**
     * Add a focus listener to this field view
     *
     * @param listener the listener
     */
    public void addFocusListener(FocusListener listener);

    /**
     * Add an action listener to this field view
     *
     * @param listener the listener
     */
    public void addActionListener(ActionListener listener);

    /**
     * Set a value into the Field
     *
     * @param value the value to be inserted into the Field
     */
    public void set(T value);

    /**
     * Set choice values into the Field
     *
     * @param choices the list of choices
     */
    public void setChoices(List<T> choices);

    /**
     * Allow/Disallow a null selection from a choice field
     *
     * @param isAllowed true if null selection allowed
     */
    public void setNullSelectionAllowed(boolean isAllowed);

    /**
     * Get a value from the field
     *
     * @return the value of the field
     * @throws BadFormatException if field is not valid format for input type
     * required.
     */
    public T get() throws BadFormatException;
    
    /**
     * Set/Clear the error marker for this field view and display the associated error
     * message
     *
     * @param message the message to associate with the error marker or null if
     * no error marker to be displayed
     */
    public void setErrorMarker(String message);

}
