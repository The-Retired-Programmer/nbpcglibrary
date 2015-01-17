/*
 * Copyright (C) 2014-2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcglibrary.common.FindAction;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;

/**
 * Abstract Class representing an editable Field on a Form
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class EditableStringField extends EditableField<String> {
    
    private final EditableFieldBackingObject<String> backingObject;

    /**
     * Constructor
     *
     * @param backingObject the backing object
     * @param label the label text for this field
     * @param field the swing field component
     */
    public EditableStringField(EditableFieldBackingObject<String> backingObject, String label, JComponent field) {
        super(backingObject, label, field);
        this.backingObject = backingObject;
    }

    /**
     * Add a minimum entry length rule to this field
     *
     * @param len minimum number of characters to enter
     * @return this object
     */
    public final EditableStringField addStringMinRule(int len) {
        addRule(new StringMinRule(len));
        return this;
    }

    private class StringMinRule extends Rule {

        private final int min;

        public StringMinRule(int min) {
            super(getLabel() + " too short");
            this.min = min;
        }

        @Override
        protected boolean ruleCheck() {
            return get().length() >= min;
        }
    }

    /**
     * Add a maximum entry length rule to this field
     *
     * @param len maximum number of characters to enter
     * @return this field
     */
    public final EditableStringField addStringMaxRule(int len) {
        addRule(new StringMaxRule(len));
        return this;
    }

    private class StringMaxRule extends Rule {

        private final int max;

        public StringMaxRule(int max) {
            super(getLabel() + " too long");
            this.max = max;
        }

        @Override
        protected boolean ruleCheck() {
            return get().length() <= max;
        }
    }

    /**
     * Add a Unique rule to this field
     *
     * @param findOthers the object used to find the setField of given values
     * @return this field
     */
    public final EditableStringField addStringUniqueRule(FindAction<String> findOthers) {
        addRule(new StringUniqueRule(findOthers));
        return this;
    }

    private class StringUniqueRule extends Rule {

        private final FindAction<String> findOthers;

        public StringUniqueRule(FindAction<String> findOthers) {
            super(getLabel() + " is not unique");
            this.findOthers = findOthers;
        }

        @Override
        protected boolean ruleCheck() {
            return findOthers.find().stream().noneMatch((s) -> (s.equals(get())));
        }
    }
    
    @Override
    void updateIfChange(String value) {
        if (!value.equals(lastvaluesetinfield)) {
            lastvaluesetinfield = value;
            if (checkRules()) {
                backingObject.set(value);
            }
        }
    }
}
