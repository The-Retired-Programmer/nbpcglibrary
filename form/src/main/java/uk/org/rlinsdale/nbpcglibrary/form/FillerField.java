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

import javax.swing.JLabel;

/**
 * A Field which creates a blank empty row in the form, which can be decorated
 * with text labels, so could be used to create a column header row.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class FillerField extends Field<String> {

    /**
     * Constructor
     *
     * @param labeltext text to be placed in label column
     * @param fieldtext text to be placed in the field column
     * @param additionalfieldtext text to be placed in the additionalfield
     * column
     */
    protected FillerField(String labeltext, String fieldtext, String additionalfieldtext) {
        super("", fieldtext == null ? null : new JLabel(fieldtext),
                additionalfieldtext == null ? null : new JLabel(additionalfieldtext), null);
    }
    
    /**
     * Constructor
     *
     * @param labeltext text to be placed in label column
     * @param fieldtext text to be placed in the field column
     * @param additionalfieldtext text to be placed in the additionalfield
     * column
     * @param errormarker the error marker
     */
    protected FillerField(String labeltext, String fieldtext, String additionalfieldtext, ErrorMarker errormarker) {
        super("", fieldtext == null ? null : new JLabel(fieldtext),
                additionalfieldtext == null ? null : new JLabel(additionalfieldtext), errormarker);
    }

    @Override
    protected void setFieldValue(String value) {
        // null action as no field
    }

    @Override
    protected String getSourceValue() {
        return "";
    }
}
