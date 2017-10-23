/*
 * Copyright 2014-2017 Richard Linsdale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.nbpcglibrary.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * A Standard Error Information Dialog Display
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class ErrorInformationDialog {

    private final DialogDescriptor dd;
    private final Runnable dialogDone;
    private static ErrorInformationDialog instance;

    /**
     * Display the error information dialog.
     *
     * @param title the dialog title
     * @param message the dialog message
     * @param l a listener which will be fired when the dialog is closed
     */
    public static void show(String title, String message, Runnable l) {
        instance = new ErrorInformationDialog(title, message, l);
    }

    private ErrorInformationDialog(String title, String message, Runnable l) {
        dialogDone = l;
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
            dd.setClosingOptions(null); // and allow closing
            dialogDone.run();
            instance = null;
        }
    }

    private class CloseChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            if (pce.getPropertyName().equals(DialogDescriptor.PROP_VALUE)
                    && pce.getNewValue() == DialogDescriptor.CLOSED_OPTION) {
                dd.setClosingOptions(null); // and allow closing
                dialogDone.run();
                instance = null;
            }
        }
    }
}
