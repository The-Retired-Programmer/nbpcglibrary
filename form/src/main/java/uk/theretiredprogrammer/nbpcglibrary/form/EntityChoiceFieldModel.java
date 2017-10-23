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

import java.io.IOException;

/**
 * The Choice Field Model - basic implementation
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <T> the type of the source value
 */
public abstract class EntityChoiceFieldModel<T> extends EntityFieldModel<T> {

    /**
     * Add a listener to collection from which the choice list is derived.
     *
     * @param listener the listener
     * @throws IOException if problems
     */
    public void addCollectionListeners(Runnable listener) throws IOException {
    }

    /**
     * Remove a listener from the collection from which the choice list is
     * derived.
     *
     * @param listener the listener
     * @throws IOException if problems
     */
    public void removeCollectionListeners(Runnable listener) throws IOException {
    }
}
