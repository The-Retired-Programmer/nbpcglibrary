/*
 * Copyright (C) 2016 Richard Linsdale.
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
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import uk.org.rlinsdale.nbpcglibrary.api.BadFormatException;

/**
 * Presenter for a row within a table
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)/** Called
 * whenever a row checkbox is checked or unchecked.
 */
public class RowPresenter implements JPanelPresenter<FieldPresenter> {

    private final List<FieldPresenter> fields = new ArrayList<>();
    private final FieldView<Boolean> checkbox;
    private final Runnable copyaction;
    private final Runnable deleteaction;
    private final RowView view;
    private Function<StringBuilder, Boolean> savefunction;

    /**
     * Constructor
     *
     * @param parent the parent table presenter
     * @param copyaction a runnable function to create a copy of this entity
     * @param deleteaction a runnable function to delete this entity
     */
    public RowPresenter(TablePresenter parent, Runnable copyaction, Runnable deleteaction) {
        this.copyaction = copyaction;
        this.deleteaction = deleteaction;
        checkbox = new CheckboxField(parent);
        view = new RowView(parent.getView());
    }
    
    /**
     * Set the save function to be used when closing the form
     * 
     * @param savefunction the save function
     */
    public void setSaveFunction(Function<StringBuilder, Boolean> savefunction) {
        this.savefunction = savefunction;
    }

    @Override
    public void setChildPresenters(FieldPresenter... fieldpresenters) {
        setChildPresenters(Arrays.asList(fieldpresenters));
    }

    @Override
    public void setChildPresenters(List<FieldPresenter> fieldpresenters) {
        fields.add(new FieldPresenter<>("Row Checkbox",checkbox,new BasicFieldModel<>(false)));
        fields.addAll(fieldpresenters);
    }

    /**
     * Test if the checkbox for this row is checked.
     *
     * @return true if checked.
     */
    public final boolean isChecked() {
        try {
            return checkbox.get();
        } catch (BadFormatException ex) {
            return false; // should never happen!!
        }
    }

    /**
     * Copy the entity associated with this row into a new entity
     */
    public final void copy() {
        copyaction.run();
    }

    /**
     * Delete the entity associated with this row
     */
    public final void delete() {
        deleteaction.run();
    }

    @Override
    public boolean test(StringBuilder sb) {
        return fields.stream().filter(presenter -> !presenter.test(sb)).count() == 0;
    }


    @Override
    public boolean save(StringBuilder sb) {
        return savefunction != null ? savefunction.apply(sb): true;
    }

    @Override
    public void enableView() {
        getView().insertChildViews(
                fields.stream().map(fp -> fp.getView()).collect(Collectors.toList())
        );
        fields.stream().forEach(presenter -> presenter.enableView());
    }
    
    @Override
    public void refreshView() {
        fields.stream().forEach(presenter -> presenter.refreshView());
    }

    @Override
    public RowView getView() {
        return view;
    }
}
