/*
 * Copyright (C) 2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcglibrary.api.BadFormatException;

/**
 * A null field view which generates a defined number of null field components
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class NullField implements FieldViewAPI<String> {

    private final int componentcount;

    /**
     * Constructor
     *
     */
    public NullField() {
        this(0);
    }

    /**
     * Constructor
     *
     * @param componentcount the number of null components to generate for this
     * field
     */
    public NullField(int componentcount) {
        this.componentcount = componentcount;
    }

    @Override
    public List<JComponent> getViewComponents() {
        List<JComponent> components = new ArrayList<>();
        int count = componentcount;
        while (count > 0) {
            components.add(null);
            count--;
        }
        return components;
    }

    @Override
    public void addFocusListener(FocusListener listener) {
    }

    @Override
    public void setErrorMarker(String message) {
    }

    @Override
    public void setChoices(List<String> choices) {
    }

    @Override
    public void setNullSelectionAllowed(boolean isAllowed) {
    }

    @Override
    public void addActionListener(ActionListener listener) {
    }

    @Override
    public void set(String value) {
    }

    @Override
    public String get() throws BadFormatException {
        return "";
    }
}
