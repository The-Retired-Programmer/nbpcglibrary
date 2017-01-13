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

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Presenter for a form object
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class FormPresenter implements PanePresenter<FieldPresenter> {

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
    public void setGetChildPresentersFunction(Supplier<List<FieldPresenter>> getchildpresentersfunction) {
        fieldpresenters = getchildpresentersfunction.get();
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
