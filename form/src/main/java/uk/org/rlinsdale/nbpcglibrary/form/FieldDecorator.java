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

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcglibrary.api.BadFormatException;
import uk.org.rlinsdale.nbpcglibrary.common.CallbackReport;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <T> type class for data element (set or get)
 */
public class FieldDecorator<T> implements EditableField<T> {

    protected final EditableField<T> field;

    public FieldDecorator(EditableField<T> field) {
        this.field = field;
    }

    @Override
    public T get() {
        return field.get();
    }

    @Override
    public T getSourceValue() {
        return field.getSourceValue();
    }

    @Override
    public void setFieldValue(T value) {
        field.setFieldValue(value);
    }

    @Override
    public T getFieldValue() throws BadFormatException {
        return field.getFieldValue();
    }

    @Override
    public void addSourceRule(Rule rule) {
        field.addSourceRule(rule);
    }

    @Override
    public List<JComponent> getComponents() {
        return field == null ? new ArrayList<>() : field.getComponents();
    }

    @Override
    public String instanceDescription() {
        return field.instanceDescription();
    }

    @Override
    public void setErrorReporter(CallbackReport errorReporter) {
        field.setErrorReporter(errorReporter);
    }

    @Override
    public void updateFieldFromSource() {
        field.updateFieldFromSource();
    }

    @Override
    public void updateSourceFromField() {
        field.updateSourceFromField();
    }

    @Override
    public void updateFieldFromSource(boolean force) {
        field.updateFieldFromSource(force);
    }

    @Override
    public void closeChoices() {
        field.closeChoices();
    }

    @Override
    public void reset() {
        field.reset();
    }

    @Override
    public void set(T value) {
        field.set(value);
    }

    @Override
    public boolean checkRules() {
        return field.checkRules();
    }
}
