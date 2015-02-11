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

import java.io.File;
import uk.org.rlinsdale.nbpcglibrary.common.Rule;
import uk.org.rlinsdale.nbpcglibrary.common.Rules;

/**
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FileBackingObject implements FieldBackingObject<String> {

    private String backingString;
    private final String label;
    private final Rules rules = new Rules();

    /**
     * Constructor
     *
     * @param label the fields label - for reporting
     * @param initialvalue the initial value of the text variable
     */
    public FileBackingObject(String label, String initialvalue) {
        backingString = initialvalue;
        this.label = label;
        rules.addRule(new FileExistsRule());
    }

    /**
     * Constructor
     *
     * @param label the fields label - for reporting
     */
    public FileBackingObject(String label) {
        this(label, "");
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

    private class FileExistsRule extends Rule {

        public FileExistsRule() {
            super(label + " - file does not exist or is a folder");
        }

        @Override
        public boolean ruleCheck() {
            File file = new File(backingString);
            return file.exists() && file.isFile();
        }
    }
}
