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

/**
 * PanePresenter - a presenter for a view which is a JPanel
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <C> the child presenter class
 */
public interface PanePresenter<C> extends Presenter<PaneView> {

    /**
     * Define a function to be used to get the set of child presenters
     * @param getchildpresentersfunction the function
     */
    public void setGetChildPresentersFunction(Supplier<List<C>> getchildpresentersfunction);
    
    /**
     * Apply the save action for the MVP
     *
     * @param sb StringBuilder object to which error messages can be added if
     * save fails
     * @return true if save is ok
     */
    public boolean save(StringBuilder sb);

}
