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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.Event;
import uk.org.rlinsdale.nbpcglibrary.common.SimpleEventParams;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * A Standard Error Information Dialog Display
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class ErrorInformationDialog {

    private final DialogDescriptor dd;
    private final Event<SimpleEventParams> dialogDone = new Event<>("ErrorInformationDialogDone");
    private static ErrorInformationDialog instance;

    /**
     * Display the error information dialog.
     *
     * @param title the dialog title
     * @param message the dialog message
     * @param l a listener which will be fired when the dialog is closed
     */
    public static void show(String title, String message, Listener<SimpleEventParams> l) {
        instance = new ErrorInformationDialog(title, message, l);
    }

    private ErrorInformationDialog(String title, String message, Listener<SimpleEventParams> l) {
        dialogDone.addListener(l);
        dd = new DialogDescriptor(
                message,
                title,
                true,
                new Object[]{DialogDescriptor.OK_OPTION},
                DialogDescriptor.DEFAULT_OPTION,
                DialogDescriptor.RIGHT_ALIGN,
                null,
                new DialogDoneListener());
        dd.setMessageType(NotifyDescriptor.ERROR_MESSAGE);
        dd.setClosingOptions(new Object[]{});
        dd.addPropertyChangeListener(new CloseChangeListener());
        DialogDisplayer.getDefault().notifyLater(dd);
    }

    private class DialogDoneListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            LogBuilder.writeLog("nbpcglibrary.form", this, "actionPerformed");
            dd.setClosingOptions(null); // and allow closing
            dialogDone.fire(new SimpleEventParams());
            instance = null;
        }
    }

    private class CloseChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            if (pce.getPropertyName().equals(DialogDescriptor.PROP_VALUE)
                    && pce.getNewValue() == DialogDescriptor.CLOSED_OPTION) {
                 LogBuilder.create("nbpcglibrary.form", Level.FINEST).addMethodName(this, "propertyChange")
                                .addMsg("Window closed").write();
                dd.setClosingOptions(null); // and allow closing
                dialogDone.fire(new SimpleEventParams());
                instance = null;
            }
        }
    }
}
