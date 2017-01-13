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

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <T> type of data being handled by this field
 */
public class LabelDecorator<T> extends FieldDecorator<T> {

    private final JLabel labelcomponent;

    /**
     * Create a label Decorator wrapped around a field
     *
     * @param labeltext the label text
     * @param field the field which needs decorating
     */
    public LabelDecorator(String labeltext, FieldViewAPI<T> field) {
        super(field);
        labelcomponent = new JLabel(labeltext);
    }

    @Override
    public List<JComponent> getViewComponents() {
        List<JComponent> c = new ArrayList<>();
        c.add(labelcomponent);
        c.addAll(super.getViewComponents());
        return c;
    }
}
