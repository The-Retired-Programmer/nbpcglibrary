/*
 * Copyright (C) 2015 Richard Linsdale.
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

import uk.org.rlinsdale.nbpcglibrary.common.Rule;
import uk.org.rlinsdale.nbpcglibrary.common.Rules;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class SimpleTextBackingObject implements FieldBackingObject<String> {

    private String backingString;
    private final String label;
    private final Rules rules = new Rules();

    /**
     * Constructor
     *
     * @param label the fields label - for reporting
     * @param initialvalue the initial value of the text variable
     */
    public SimpleTextBackingObject(String label, String initialvalue) {
        backingString = initialvalue;
        this.label = label;
    }

    /**
     * Constructor
     *
     * @param label the fields label - for reporting
     * @param initialvalue the initial value of the text variable
     * @param max the maximum entry string length
     */
    public SimpleTextBackingObject(String label, String initialvalue, int max) {
        backingString = initialvalue;
        this.label = label;
        rules.addRule(new StringMaxRule(max));
    }

    /**
     * Constructor
     *
     * @param label the fields label - for reporting
     * @param initialvalue the initial value of the text variable
     * @param min the minimum entry string length
     * @param max the maximum entry string length
     */
    public SimpleTextBackingObject(String label, String initialvalue, int min, int max) {
        backingString = initialvalue;
        this.label = label;
        rules.addRule(new StringMinRule(min));
        rules.addRule(new StringMaxRule(max));
    }

    /**
     * Constructor
     *
     * @param label the fields label - for reporting
     */
    public SimpleTextBackingObject(String label) {
        this(label, "");
    }

    /**
     * Constructor
     *
     * @param label the fields label - for reporting
     * @param max the maximum entry string length
     */
    public SimpleTextBackingObject(String label, int max) {
        this(label, "", max);
    }

    /**
     * Constructor
     *
     * @param label the fields label - for reporting
     * @param min the minimum entry string length
     * @param max the maximum entry string length
     */
    public SimpleTextBackingObject(String label, int min, int max) {
        this(label, "", min, max);
    }

    @Override
    public void set(String value) {
        backingString = value;
    }

    @Override
    public String get() {
        return backingString;
    }

    @Override
    public boolean checkRules() {
        return rules.checkRules();
    }

    @Override
    public String getErrorMessages() {
        return rules.getErrorMessages();
    }

    private class StringMinRule extends Rule {

        private final int min;

        /**
         * Constructor - minimum entry length rule to this field
         *
         * @param min minimum number of characters to enter
         */
        public StringMinRule(int min) {
            super(label + " too short");
            this.min = min;
        }

        @Override
        protected boolean ruleCheck() {
            return backingString.length() >= min;
        }
    }

    private class StringMaxRule extends Rule {

        private final int max;

        /**
         * Constructor - maximum entry length rule
         *
         * @param max maximum number of characters to enter
         */
        public StringMaxRule(int max) {
            super(label + " too long");
            this.max = max;
        }

        @Override
        protected boolean ruleCheck() {
            return backingString.length() <= max;
        }
    }
}
