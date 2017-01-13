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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterLog;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;

/**
 * Presenter for a composite object (composed of many vertically stacked
 * presenters)
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@RegisterLog("nbpcglibrary.form")
public class CompositePresenter implements PanePresenter<PanePresenter> {

    private List<PanePresenter> childpresenters;
    private final CompositeView view;

    /**
     * Constructor
     */
    public CompositePresenter() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param borderTitle the title to be included on the panel border
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public CompositePresenter(String borderTitle) {
        this.view = new CompositeView(borderTitle);
        LogBuilder.writeConstructorLog("nbpcglibrary.form", this);
    }

    @Override
    public void setGetChildPresentersFunction(Supplier<List<PanePresenter>> getchildpresentersfunction) {
        childpresenters = getchildpresentersfunction.get();
    }

    @Override
    public boolean test(StringBuilder sb) {
        return childpresenters.stream().filter(presenter -> !presenter.test(sb)).count() == 0;
    }

    @Override
    public boolean save(StringBuilder sb) {
        return childpresenters.stream().filter(presenter -> !presenter.save(sb)).count() == 0;
    }

    @Override
    public void enableView() {
        getView().insertChildViews(childpresenters.stream()
                .map(p -> (PaneView) p.getView()).collect(Collectors.toList()));
        childpresenters.stream().forEach(presenter -> presenter.enableView());
    }

    @Override
    public void refreshView() {
        childpresenters.stream().forEach(presenter -> presenter.enableView());
    }

    @Override
    public CompositeView getView() {
        return view;
    }
}
