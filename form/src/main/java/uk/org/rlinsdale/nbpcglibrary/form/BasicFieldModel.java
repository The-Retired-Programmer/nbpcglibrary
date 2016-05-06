/*
 * Copyright (C) 2015-2016 Richard Linsdale.
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

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;
import uk.org.rlinsdale.nbpcglibrary.common.Rules;

/**
 * The Field Model - Basic implementation
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
            super("Field Length is too short");
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
            super("Field Length is too long");
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

        private final long min;

        public MinRule(long min) {
            super("Field value is too small");
            this.min = min;
        }

        @Override
        protected boolean ruleCheck() {
            return ((Long) value) >= min;
        }
    }

    /**
     * Add a rule testing the maximum value of number
     *
     * @param max the minimum value
     * @return itself (to enable fluent construction)
     */
    public BasicFieldModel<T> addMaxRule(long max) {
        rules.addRule(new MaxRule(max));
        return this;
    }

    private class MaxRule extends Rule {

        private final long max;

        public MaxRule(long max) {
            super("Field value is too large");
            this.max = max;
        }

        @Override
        protected boolean ruleCheck() {
            return ((Long) value) <= max;
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
            super("Field value is not a filename");
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
            super("Field value is not a folder");
        }

        @Override
        protected boolean ruleCheck() {
            return new File((String) value).isDirectory();
        }
    }
}
