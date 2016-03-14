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

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcglibrary.api.BadFormatException;
import uk.org.rlinsdale.nbpcglibrary.common.CallbackReport;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 * Abstract Class representing a non-editable Field on a Form
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FieldNonEditableImpl implements Field<String> {

    private final JComponent fieldcomponent;
    
    /**
     * Constructor
     *
     * @param fieldcomponent the field component to be used in field
     */
    protected FieldNonEditableImpl(JComponent fieldcomponent ) {
       this.fieldcomponent= fieldcomponent;
    }

    @Override
    public final void setErrorReporter(CallbackReport errorReporter) {
    }

    @Override
    public void addSourceRule(Rule rule) {
    }

    @Override
    public final void updateFieldFromSource() {
    }

    @Override
    public final void updateFieldFromSource(boolean force) {
    }

    @Override
    public String getSourceValue() {
        return null;
    }

    private void insertField(String value) {
    }

    @Override
    public final void updateSourceFromField() {
    }

    @Override
    public boolean checkRules() {
       return true;
    }

    @Override
    public final String get() {
        return null;
    }

    @Override
    public void set(String value) {
    }
    
    @Override
    public void reset() {
    }

    @Override
    public void closeChoices() {
    }
    
    @Override
    public void setFieldValue(String value) {
    }

    @Override
    public String getFieldValue() throws BadFormatException {
        return null;
    }
    
    public List<JComponent> getComponents() {
        List<JComponent> c = new ArrayList<>();
        c.add(fieldcomponent);
        return c;
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this);
    }
}
