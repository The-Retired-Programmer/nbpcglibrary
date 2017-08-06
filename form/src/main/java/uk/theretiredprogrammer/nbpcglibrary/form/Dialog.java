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
import java.util.logging.Level;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import uk.theretiredprogrammer.nbpcglibrary.common.Event;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.api.HasInstanceDescription;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import uk.theretiredprogrammer.nbpcglibrary.common.SimpleEventParams;

/**
 * the Dialog Class. Dialogs are constructed with one or more panels.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class Dialog implements HasInstanceDescription {

    private static Dialog instance;
    private final DialogDescriptor dd;
    private final PanePresenter presenter;
    private final String title;
    private final Event<SimpleEventParams> cancellationEvent;

    /**
     * Display the dialog.
     *
     * @param title the dialog title
     * @param presenter the controller used to create the body of the dialog
     */
    public static void show(String title, PanePresenter presenter) {
        instance = new Dialog(title, presenter, false, null);
    }
    
    /**
     * Display the modal dialog.
     *
     * @param title the dialog title
     * @param presenter the presenter used to create the body of the dialog
     */
    public static void showModal(String title, PanePresenter presenter) {
        instance = new Dialog(title, presenter, true, null);
    }

    /**
     * Display the modal dialog.
     *
     * @param title the dialog title
     * @param presenter the presenter used to create the body of the dialog
     * @param onCancellation listener for dialog completion
     */
    public static void showModal(String title, PanePresenter presenter, Listener<SimpleEventParams> onCancellation) {
        instance = new Dialog(title, presenter, true, onCancellation);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private Dialog(String title, PanePresenter presenter, boolean isModal, Listener<SimpleEventParams> onCancellation) {
        this.presenter = presenter;
        presenter.enableView();
        PaneView view = (PaneView) this.presenter.getView();
        this.title = title;
        cancellationEvent = new Event<>("DialogueCancellation");
        cancellationEvent.addListener(onCancellation);
        LogBuilder.writeConstructorLog("nbpcglibrary.form", this, title, this.presenter, isModal, onCancellation);
        dd = new DialogDescriptor(
                view,
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
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, title);
    }

    private class DialogDoneListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == DialogDescriptor.OK_OPTION) {
                StringBuilder sb = new StringBuilder();
                if (presenter.test(sb)) {
                    if (presenter.save(sb)) {
                        LogBuilder.create("nbpcglibrary.form", Level.FINEST).addMethodName(this, "actionPerformed")
                                .addMsg("Dialog {0}: OK response, form is valid, save() processed", title).write();
                        dd.setClosingOptions(null); // and allow closing
                        instance = null;
                    } else {
                        LogBuilder.create("nbpcglibrary.form", Level.FINEST).addMethodName(this, "actionPerformed")
                                .addMsg("Dialog {0}: OK response, form is valid, save() failed - {1}", title, sb.toString()).write();
                    }
                } else {
                    LogBuilder.create("nbpcglibrary.form", Level.FINEST).addMethodName(this, "actionPerformed")
                            .addMsg("Dialog {0}: OK response but Form is invalid - {1}", title, sb.toString()).write();
                }
            } else {
                LogBuilder.create("nbpcglibrary.form", Level.FINEST).addMethodName(this, "actionPerformed")
                        .addMsg("Dialog {0}: CANCEL response", title).write();
                formCancel();
            }
        }
    }

    private class CloseChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            if (pce.getPropertyName().equals(DialogDescriptor.PROP_VALUE)
                    && pce.getNewValue() == DialogDescriptor.CLOSED_OPTION) {
                LogBuilder.create("nbpcglibrary.form", Level.FINEST).addMethodName(this, "propertyChange")
                        .addMsg("Dialog {0}: window closed - setting CLOSED response", title).write();
                formCancel();
            }
        }
    }

    private void formCancel() {
        dd.setClosingOptions(null); // and allow closing
        instance = null;
        cancellationEvent.fire(new SimpleEventParams());
    }
}
