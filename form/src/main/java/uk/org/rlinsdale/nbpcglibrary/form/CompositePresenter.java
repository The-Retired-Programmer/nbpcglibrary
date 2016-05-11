/*
 * Copyright (C) 2014-2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterLog;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * Presenter for a composite object (composed of many vertically stacked
 * presenters)
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
