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

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Presenter for a form object
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FormPresenter implements JPanelPresenter<FieldPresenter> {

    private List<FieldPresenter> fieldpresenters;
    private final FormView view;
    private Function<StringBuilder, Boolean> savefunction;

    /**
     * Constructor
     */
    public FormPresenter() {
        this(null);
    }

    /**
     * Constructor
     * 
     * @param title the form title displayed with a form border
     */
    public FormPresenter(String title) {
       view = new FormView(title);
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
        this.fieldpresenters = fieldpresenters;
    }

    @Override
    public FormView getView() {
        return view;
    }

    @Override
    public boolean test(StringBuilder sb) {
        return fieldpresenters.stream().filter(presenter -> !presenter.test(sb)).count() == 0;
    }

    @Override
    public boolean save(StringBuilder sb) {
        return savefunction != null ? savefunction.apply(sb) : true;
    }

    @Override
    public void enableView() {
        getView().insertChildViews(fieldpresenters.stream()
                .map(fp -> fp.getView()).collect(Collectors.toList()));
        fieldpresenters.stream().forEach(presenter -> presenter.enableView());
    }

    @Override
    public void refreshView() {
        fieldpresenters.stream().forEach(presenter -> presenter.refreshView());
    }
}
