/*
 * Copyright (C) 2014-2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import javax.swing.JCheckBox;

/**
 * A General purpose Field for displaying and editing a value which is a simple
 * boolean using a checkbox.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class CheckboxField extends EditableField<Boolean> {

    private final JCheckBox checkbox;

    /**
     * Constructor
     *
     * @param label field label
     */
    public CheckboxField(String label) {
        this(label, new JCheckBox("", false));
    }

    private CheckboxField(String label, JCheckBox checkbox) {
        super(label, checkbox, null);
        this.checkbox = checkbox;
        checkbox.addActionListener(getActionListener());
        updateFieldFromSource();
    }

    @Override
    protected final Boolean getFieldValue() {
        return checkbox.isSelected();
    }

    @Override
    protected final void setFieldValue(Boolean value) {
        checkbox.setSelected(value);
    }

    // no validation is normally required for a checkbox
    @Override
    protected boolean sourceCheckRules() {
        return true;
    }

    @Override
    protected String getSourceErrorMessages() {
        return "";
    }

}
