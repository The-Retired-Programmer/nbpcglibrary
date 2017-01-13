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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <T> type of data being handled by this field
 */
public class ErrorMarkerDecorator<T> extends FieldDecorator<T> {

    private final JLabel errorMarker = new JLabel();

    /**
     * the Constructor
     *
     * @param field the field to associate with this error marker.
     */
    public ErrorMarkerDecorator(FieldViewAPI<T> field) {
        super(field);
        errorMarker.setPreferredSize(new Dimension(16, 16));
    }

    @Override
    public List<JComponent> getViewComponents() {
        List<JComponent> c = new ArrayList<>();
        c.addAll(super.getViewComponents());
        c.add(errorMarker);
        return c;
    }

    @Override
    public void setErrorMarker(String errormessages) {
        errorMarker.setIcon(new ImageIcon(getClass().getResource(errormessages == null ? "empty.png" : "error.png")));
        errorMarker.setToolTipText(errormessages);
    }
}
