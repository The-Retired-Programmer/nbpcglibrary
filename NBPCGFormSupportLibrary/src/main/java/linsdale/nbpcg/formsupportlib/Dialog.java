/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package linsdale.nbpcg.formsupportlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import linsdale.nbpcg.supportlib.Log;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

/**
 * the Dialog Class. Dialog are constructed with one or more panels.
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class Dialog {

    private static Dialog instance;
    private DialogDescriptor dd;
    private Form form;
    private String title;
    public static final int OK = -1;
    public static final int CANCEL = -2;
    public static final int CLOSE = -3;

    public static void show(String title, Form form) {
        instance = new Dialog(title, form);
    }

    public static void show(String title, boolean isModal, Form form) {
        instance = new Dialog(title, isModal, form);
    }

    private Dialog(String title, Form form) {
        this(title, false, form);
    }

    private Dialog(String title, boolean isModal, Form form) {
        this.form = form;
        this.title = title;
        Log.get("linsdale.nbpcg.formsupportlib").log(Level.FINE, "Dialogue {0} open", title);
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

    private class DialogDoneListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            Logger log = Log.get("linsdale.nbpcg.formsupportlib");
            if (ae.getSource() == DialogDescriptor.OK_OPTION) {
                switch (form.save()) {
                    case Form.SAVESUCCESS:
                        log.log(Level.FINEST, "Dialog {0}: OK response, form is valid, save() processed", title);
                        dd.setClosingOptions(null); // and allow closing
                        instance = null;
                        break;
                    case Form.SAVEVALIDATIONFAIL:
                        log.log(Level.FINEST, "Dialog {0}: OK response but Form is invalid", title);
                        break;
                    case Form.SAVEFAIL:
                        log.log(Level.FINEST, "Dialog {0}: OK response, form is valid, save() failed", title);
                        instance = null;
                        break;
                }
            } else {
                form.reset();
                log.log(Level.FINEST, "Dialog {0}: CANCEL response, reset() processed", title);
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
                Log.get("linsdale.nbpcg.formsupportlib").log(Level.FINEST, "Dialog {0}: window close - treat as CANCEL response", title);
                dd.setClosingOptions(null); // and allow closing
                form.reset();
                instance = null;
            }
        }
    }
}
