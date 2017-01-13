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
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
