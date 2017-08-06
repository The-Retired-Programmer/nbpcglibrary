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

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import uk.theretiredprogrammer.nbpcglibrary.common.Rule;
import uk.theretiredprogrammer.nbpcglibrary.common.Rules;

/**
 * The Field Model - Basic implementation
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <T> the type of the source value
 */
public class BasicFieldModel<T> extends FieldModel<T> {

    private T value; // data source
    private final Rules rules = new Rules();
    private Consumer<T> callbackfunction;
    private boolean isNullSelectionAllowed;
    private List<T> choices;

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
        return rules.checkRules(sb);
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
    public BasicFieldModel<T> addMinLengthRule(int min) {
        rules.addRule(new MinLengthRule(min));
        return this;
    }

    private class MinLengthRule extends Rule {

        private final int min;

        public MinLengthRule(int min) {
            super("Too short");
            this.min = min;
        }

        @Override
        protected boolean ruleCheck() {
            return ((String) value).length() >= min;
        }
    }

    /**
     * Add a rule testing the maximum length for the text entered
     *
     * @param max the minimum length of the text
     * @return itself (to enable fluent construction)
     */
    public BasicFieldModel<T> addMaxLengthRule(int max) {
        rules.addRule(new MaxLengthRule(max));
        return this;
    }

    private class MaxLengthRule extends Rule {

        private final int max;

        public MaxLengthRule(int max) {
            super("Too long");
            this.max = max;
        }

        @Override
        protected boolean ruleCheck() {
            return ((String) value).length() <= max;
        }
    }

    /**
     * Add a rule testing the minimum value of number
     *
     * @param min the minimum value
     * @return itself (to enable fluent construction)
     */
    public BasicFieldModel<T> addMinRule(int min) {
        rules.addRule(new MinRule(min));
        return this;
    }

    private class MinRule extends Rule {

        private final int min;

        public MinRule(int min) {
            super("Too small");
            this.min = min;
        }

        @Override
        protected boolean ruleCheck() {
            return (Integer) value >= min;
        }
    }

    /**
     * Add a rule testing the maximum value of number
     *
     * @param max the minimum value
     * @return itself (to enable fluent construction)
     */
    public BasicFieldModel<T> addMaxRule(int max) {
        rules.addRule(new MaxRule(max));
        return this;
    }

    private class MaxRule extends Rule {

        private final int max;

        public MaxRule(int max) {
            super("Too large");
            this.max = max;
        }

        @Override
        protected boolean ruleCheck() {
            return (Integer)value <= max;
        }
    }

    /**
     * Add a rule testing the value is a file name
     *
     * @return itself (to enable fluent construction)
     */
    public BasicFieldModel<T> addFilenameRule() {
        rules.addRule(new FilenameRule());
        return this;
    }

    private class FilenameRule extends Rule {

        public FilenameRule() {
            super("Not a filename");
        }

        @Override
        protected boolean ruleCheck() {
            return new File((String) value).isFile();
        }
    }

    /**
     * Add a rule testing the value is a file name
     *
     * @return itself (to enable fluent construction)
     */
    public BasicFieldModel<T> addFoldernameRule() {
        rules.addRule(new FoldernameRule());
        return this;
    }

    private class FoldernameRule extends Rule {

        public FoldernameRule() {
            super("Not a folder");
        }

        @Override
        protected boolean ruleCheck() {
            return new File((String) value).isDirectory();
        }
    }
}
