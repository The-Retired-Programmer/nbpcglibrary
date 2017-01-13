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

import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;

/**
 * Abstract Class representing a Field View
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
