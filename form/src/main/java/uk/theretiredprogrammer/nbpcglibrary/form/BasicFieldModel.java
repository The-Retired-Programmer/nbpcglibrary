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
import java.util.function.Consumer;
import uk.theretiredprogrammer.nbpcglibrary.rules.FilenameRule;
import uk.theretiredprogrammer.nbpcglibrary.rules.FoldernameRule;
import uk.theretiredprogrammer.nbpcglibrary.rules.MaxIntegerRule;
import uk.theretiredprogrammer.nbpcglibrary.rules.MaxStringRule;
import uk.theretiredprogrammer.nbpcglibrary.rules.MinIntegerRule;
import uk.theretiredprogrammer.nbpcglibrary.rules.MinStringRule;
import uk.theretiredprogrammer.nbpcglibrary.rules.Rules;

/**
 * The Field Model - Basic implementation
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <T> the type of the source value
 */
public class BasicFieldModel<T> extends FieldModel<T> {

    private T value; // data source
    private Consumer<T> callbackfunction;
    private boolean isNullSelectionAllowed;
    private List<T> choices;
    private final Rules<T> rules = new Rules<>();

    /**
     * Constructor
     *
     * @param initialvalue the initial value for this variable
     */
    public BasicFieldModel(T initialvalue) {
        this(initialvalue, null, false, null);
    }
    
    /**
     * Constructor
     *
     * @param initialvalue the initial value for this variable
     * @param choices selection choices (if being used in connection with a
     * choice field)
     * @param isNullSelectionAllowed true if null selection is to be allowed (if
     * being used in connection with a choice field)
     */
    public BasicFieldModel(T initialvalue, List<T> choices, boolean isNullSelectionAllowed) {
        this(initialvalue, choices, isNullSelectionAllowed, null);
    }
    
    /**
     * Constructor
     *
     * @param initialvalue the initial value for this variable
     * @param callbackfunction the callback function to be called on change to
     * this model's value
     */
    public BasicFieldModel(T initialvalue, Consumer<T> callbackfunction) {
        this(initialvalue, null, false, callbackfunction);
    }
    
    /**
     * Constructor
     *
     * @param initialvalue the initial value for this variable
     * @param choices selection choices (if being used in connection with a
     * choice field)
     * @param isNullSelectionAllowed true if null selection is to be allowed (if
     * being used in connection with a choice field)
     * @param callbackfunction the callback function to be called on change to
     * this model's value
     */
    public BasicFieldModel(T initialvalue, List<T> choices, boolean isNullSelectionAllowed, Consumer<T> callbackfunction) {
        this.value = initialvalue;
        this.choices = choices;
        this.isNullSelectionAllowed = isNullSelectionAllowed;
        this.callbackfunction = callbackfunction;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T value) {
        if ((this.value == null && value != null) || (this.value != null && !this.value.equals(value))) {
            this.value = value;
            if (callbackfunction != null) {
                callbackfunction.accept(value);
            }
        }
    }

    @Override
    public boolean test(StringBuilder sb) {
        return rules.checkRules(sb, value);
    }

    @Override
    public boolean isNullSelectionAllowed() {
        return isNullSelectionAllowed;
    }

    @Override
    public List<T> getChoices() {
        return choices;
    }

    /**
     * Add a rule testing the minimum length for the text entered
     *
     * @param min the minimum length of the text
     * @return itself (to enable fluent construction)
     */
    public BasicFieldModel<T> addMinStringRule(int min) {
        rules.addRule(new MinStringRule((v) -> v, min));
        return this;
    }

    /**
     * Add a rule testing the maximum length for the text entered
     *
     * @param max the minimum length of the text
     * @return itself (to enable fluent construction)
     */
    public BasicFieldModel<T> addMaxStringRule(int max) {
        rules.addRule(new MaxStringRule((v) -> v, max));
        return this;
    }

    /**
     * Add a rule testing the minimum value of number
     *
     * @param min the minimum value
     * @return itself (to enable fluent construction)
     */
    public BasicFieldModel<T> addMinIntegerRule(int min) {
        rules.addRule(new MinIntegerRule((v) -> v, min));
        return this;
    }
    
    /**
     * Add a rule testing the maximum value of number
     *
     * @param max the minimum value
     * @return itself (to enable fluent construction)
     */
    public BasicFieldModel<T> addMaxIntegerRule(int max) {
        rules.addRule(new MaxIntegerRule((v) -> v, max));
        return this;
    }

    /**
     * Add a rule testing the value is a file name
     *
     * @return itself (to enable fluent construction)
     */
    public BasicFieldModel<T> addFilenameRule() {
        rules.addRule(new FilenameRule((v)-> v));
        return this;
    }

    /**
     * Add a rule testing the value is a file name
     *
     * @return itself (to enable fluent construction)
     */
    public BasicFieldModel<T> addFoldernameRule() {
        rules.addRule(new FoldernameRule((v)-> v));
        return this;
    }
}
