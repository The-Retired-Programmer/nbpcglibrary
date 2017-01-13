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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import uk.theretiredprogrammer.nbpcglibrary.api.BadFormatException;

/**
 * A null field view which generates a defined number of null field components
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
