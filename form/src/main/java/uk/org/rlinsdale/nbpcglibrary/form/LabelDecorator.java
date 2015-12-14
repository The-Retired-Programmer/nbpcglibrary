/*
 * Copyright (C) 2015 Richard Linsdale.
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

import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> type of data being handled by this field
 */
public class LabelDecorator<T> extends FieldDecorator<T> {

    private final JLabel labelcomponent;
    private final String labeltext;

    public LabelDecorator(EditableField<T> field, String labeltext) {
        super(field);
        labelcomponent = new JLabel(labeltext);
        this.labeltext = labeltext;
    }

    @Override
    public String instanceDescription() {
        return field.instanceDescription() + "/" + labeltext;
    }

    @Override
    public List<JComponent> getComponents() {
        List<JComponent> c = super.getComponents();
        c.add(0, labelcomponent);
        return c;
    }
}
