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

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A Class representing an error marker on a form
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class ErrorMarker extends JLabel {

    /**
     * Constructor
     */
    public ErrorMarker() {
        clearError();
    }

    /**
     * Switch on the error marker icon and associated tooltiptext
     *
     * @param errormessages error messages to be displayed
     */
    public final void setError(String errormessages) {
        setIcon(new ImageIcon(getClass().getResource("error.png")));
        setToolTipText(errormessages);
    }

    /**
     * Switch off the error marker icon and associated tooltiptext
     */
    public final void clearError() {
        setIcon(new ImageIcon(getClass().getResource("empty.png")));
        setToolTipText(null);
    }
}
