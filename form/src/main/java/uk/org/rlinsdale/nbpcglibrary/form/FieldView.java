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

import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;

/**
 * Abstract Class representing a Field View
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> type of the data contained in the field
 */
public abstract class FieldView<T> implements FieldViewAPI<T> {

    private final JComponent fieldcomponent;

    /**
     * Constructor
     *
     * @param fieldcomponent the primary component to be used in field
     */
    protected FieldView(JComponent fieldcomponent) {
        this.fieldcomponent = fieldcomponent;
    }

    @Override
    public List<JComponent> getViewComponents() {
        return Arrays.asList(new JComponent[]{fieldcomponent});
    }

    @Override
    public void addFocusListener(FocusListener listener) {
        fieldcomponent.addFocusListener(listener);
    }

    @Override
    public void setErrorMarker(String message) {
    }
    
    @Override
    public void setChoices(List<T> choices) {
    }

    @Override
    public void setNullSelectionAllowed(boolean isAllowed) {
    }
}
