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

import java.util.List;
import javax.swing.JComponent;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;

/**
 * Field (on a Form) Interface
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public interface Field extends HasInstanceDescription {

    /**
     * Get an list of Components which make up the Field. The list will be in
     * left to right display order.
     *
     * @return an list of components
     */
    public List<JComponent> getComponents();
}
