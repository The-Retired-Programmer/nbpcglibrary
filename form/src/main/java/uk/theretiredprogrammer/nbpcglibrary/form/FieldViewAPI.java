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

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.JComponent;
import uk.theretiredprogrammer.nbpcglibrary.api.BadFormatException;

/**
 * API for a Field View
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
