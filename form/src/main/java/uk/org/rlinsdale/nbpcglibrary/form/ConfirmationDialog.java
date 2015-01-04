/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.util.logging.Level;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * A basic Confirmation Dialog (YES / NO)
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class ConfirmationDialog {

    /**
     * Display the dialog, wait for button response and return its selection.
     *
     * @param title the dialog title
     * @param message the dialog message
     * @return true if YES is pressed, otherwise false
     */
    public static boolean show(String title, String message) {
        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(message,
                title, NotifyDescriptor.YES_NO_OPTION);
        boolean res = DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.YES_OPTION;
        LogBuilder.create("nbpcglibrary.form", Level.FINER).addMethodName("ConfirmationDialog", "show", title, message)
                            .addMsg("responce is {0}", res).write();
        return res;
    }
}
