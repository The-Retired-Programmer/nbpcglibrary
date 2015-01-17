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
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.LogHelper;

/**
 * the Dialog Class. Dialogs are constructed with one or more panels.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class Dialog implements LogHelper {

    private static Dialog instance;
    private DialogDescriptor dd;
    private Form form;
    private String title;

    /**
     * Display the dialog.
     *
     * @param title the dialog title
     * @param form the form used to create the body of the dialog
     */
    public static void show(String title, Form form) {
        instance = new Dialog(title, form);
    }

    /**
     * Display the dialog.
     *
     * @param title the dialog title
     * @param isModal true if this to be a modal dialog
     * @param form the form used to create the body of the dialog
     */
    public static void show(String title, boolean isModal, Form form) {
        instance = new Dialog(title, isModal, form);
    }

    private Dialog(String title, Form form) {
        this(title, false, form);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private Dialog(String title, boolean isModal, Form form) {
        this.form = form;
        this.title = title;
        LogBuilder.writeConstructorLog("nbpcglibrary.form", this, title, isModal, form);
        dd = new DialogDescriptor(
                form,
                title,
                isModal,
                DialogDescriptor.OK_CANCEL_OPTION,
                DialogDescriptor.OK_OPTION,
                new DialogDoneListener());
        dd.setClosingOptions(new Object[]{});
        dd.addPropertyChangeListener(new CloseChangeListener());
        DialogDisplayer.getDefault().notifyLater(dd);
    }

    @Override
    public String classDescription() {
        return LogBuilder.classDescription(this, title);
    }

    private class DialogDoneListener implements ActionListener, LogHelper {
        
        @Override
        public String classDescription() {
            return LogBuilder.classDescription(this);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == DialogDescriptor.OK_OPTION) {
                switch (form.save()) {
                    case SAVESUCCESS:
                        LogBuilder.create("nbpcglibrary.form", Level.FINEST).addMethodName(this, "actionPerformed")
                                .addMsg("Dialog {0}: OK response, form is valid, save() processed", title).write();
                        dd.setClosingOptions(null); // and allow closing
                        instance = null;
                        break;
                    case SAVEVALIDATIONFAIL:
                        LogBuilder.create("nbpcglibrary.form", Level.FINEST).addMethodName(this, "actionPerformed")
                                .addMsg("Dialog {0}: OK response but Form is invalid", title).write();
                        break;
                    case SAVEFAIL:
                        LogBuilder.create("nbpcglibrary.form", Level.FINEST).addMethodName(this, "actionPerformed")
                                .addMsg("Dialog {0}: OK response, form is valid, save() failed", title).write();
                        instance = null;
                        break;
                }
            } else {
                form.cancel();
                LogBuilder.create("nbpcglibrary.form", Level.FINEST).addMethodName(this, "actionPerformed")
                        .addMsg("Dialog {0}: CANCEL response", title).write();
                dd.setClosingOptions(null); // and allow closing
                instance = null;
            }
        }
    }

    private class CloseChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            if (pce.getPropertyName().equals(DialogDescriptor.PROP_VALUE)
                    && pce.getNewValue() == DialogDescriptor.CLOSED_OPTION) {
                LogBuilder.create("nbpcglibrary.form", Level.FINEST).addMethodName(this, "propertyChange")
                        .addMsg("Dialog {0}: window closed - setting CANCEL response", title).write();
                dd.setClosingOptions(null); // and allow closing
                form.cancel();
                instance = null;
            }
        }
    }
}
