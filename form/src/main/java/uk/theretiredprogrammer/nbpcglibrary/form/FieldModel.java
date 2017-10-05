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
import uk.theretiredprogrammer.nbpcglibrary.common.Rules;

/**
 * The Field Model API
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <T> the type of the field value
 */
public abstract class FieldModel<T> extends Rules {

    /**
     * Set the value in this field model
     *
     * @param value the value
     */
    public abstract void set(T value);

    /**
     * Get the value for this field model
     *
     * @return the value
     */
    public abstract T get();

    /**
     * Test if null selection is allowed in choice field
     *
     * @return true if a null selection is allowed
     */
    public abstract boolean isNullSelectionAllowed();

    /**
     * Get the list of possible selection values for a choice field
     *
     * @return the list of choices
     */
    public abstract List<T> getChoices();

    /**
     * Check that the model rules are ok.
     *
     * @param sb A string builder into which any error messages are placed if
     * test fails
     * @return true if all rules are ok.
     */
    public abstract boolean test(StringBuilder sb);
}
