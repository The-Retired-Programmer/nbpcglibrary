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
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import uk.theretiredprogrammer.nbpcglibrary.api.BadFormatException;

/**
 * Presenter for a row within a table
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)/** Called
 * whenever a row checkbox is checked or unchecked.
 */
public class RowPresenter implements PanePresenter<FieldPresenter> {

    private final List<FieldPresenter> fieldpresenters = new ArrayList<>();
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
    public void setGetChildPresentersFunction(Supplier<List<FieldPresenter>> getchildpresentersfunction) {
        fieldpresenters.add(new FieldPresenter<>("Row Checkbox",checkbox,new BasicFieldModel<>(false)));
        fieldpresenters.addAll(getchildpresentersfunction.get());
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
        return fieldpresenters.stream().filter(presenter -> !presenter.test(sb)).count() == 0;
    }


    @Override
    public boolean save(StringBuilder sb) {
        return savefunction != null ? savefunction.apply(sb): true;
    }

    @Override
    public void enableView() {
        getView().insertChildViews(
                fieldpresenters.stream().map(fp -> fp.getView()).collect(Collectors.toList())
        );
        fieldpresenters.stream().forEach(presenter -> presenter.enableView());
    }
    
    @Override
    public void refreshView() {
        fieldpresenters.stream().forEach(presenter -> presenter.refreshView());
    }

    @Override
    public RowView getView() {
        return view;
    }
}
