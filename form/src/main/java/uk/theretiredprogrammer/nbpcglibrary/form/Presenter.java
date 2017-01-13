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

/**
 * Presenter
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <V> the view class
 */
public interface Presenter<V> {
    
    /**
     * Get the view associated with this presenter
     *
     * @return the view
     */
    public V getView();
    
    /**
     * Check that the MVP rules are ok.
     *
     * @param sb StringBuilder object to which error messages can be added if
     * test fails
     * @return true if all rules are ok.
     */
    public boolean test(StringBuilder sb);

    /**
     * Enable the View
     */
    public void enableView();
    
    /**
     * Refresh the View (using data from model)
     */
    public void refreshView();
}
