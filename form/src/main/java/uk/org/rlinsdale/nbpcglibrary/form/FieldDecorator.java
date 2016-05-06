/*
 * Copyright (C) 2015-2016 Richard Linsdale.
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
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> type class for data element (set or get)
 */
public abstract class FieldDecorator<T> implements FieldViewAPI<T> {

    /**
     * The field which is being decorated
     */
    protected final FieldViewAPI<T> field;

    /**
     * the Constructor
     *
     * @param field The field which is being decorated
     */
    public FieldDecorator(FieldViewAPI<T> field) {
        this.field = field;
    }

    @Override
    public T get() throws BadFormatException {
        return field.get();
    }

    @Override
    public List<JComponent> getViewComponents() {
        return field.getViewComponents();
    }

    @Override
    public void set(T value) {
        field.set(value);
    }

    @Override
    public void addFocusListener(FocusListener listener) {
        field.addFocusListener(listener);
    }

    @Override
    public void addActionListener(ActionListener listener) {
        field.addActionListener(listener);
    }

    @Override
    public void setErrorMarker(String message) {
        field.setErrorMarker(message);
    }

    @Override
    public void setChoices(List<T> choices) {
        field.setChoices(choices);
    }

    @Override
    public void setNullSelectionAllowed(boolean isAllowed) {
        field.setNullSelectionAllowed(isAllowed);
    }
}
