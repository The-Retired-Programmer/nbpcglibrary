/*
 * Copyright (C) 2014-20156 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * A General purpose Field for displaying a value which is a simple textual
 * string.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class ColumnLabelField extends FieldNonEditableImpl {
    
    private final boolean errormarkerused;
    
    /**
     * Constructor
     *
     * @param label the field initial value
     * @param errormarkerused true if error marker used with fields in this column
     */
    public ColumnLabelField(String label, boolean errormarkerused) {
        this(new JLabel(label),errormarkerused );
    }
    
    private ColumnLabelField(JLabel fieldcomponent, boolean errormarkerused) {
        super(fieldcomponent);
        this.errormarkerused = errormarkerused;
    }
    
    @Override
    public List<JComponent> getComponents() {
        List<JComponent> c = super.getComponents();
        if (errormarkerused) {
            c.add(null); 
        }
        return c;
    }
}
