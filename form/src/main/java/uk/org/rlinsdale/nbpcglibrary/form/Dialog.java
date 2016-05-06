/*
 * Copyright (C) 2014-2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
import uk.org.rlinsdale.nbpcglibrary.common.Event;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.api.HasInstanceDescription;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.SimpleEventParams;

/**
 * the Dialog Class. Dialogs are constructed with one or more panels.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class Dialog implements HasInstanceDescription {

    private static Dialog instance;
    private final DialogDescriptor dd;
    private final JPanelPresenter presenter;
    private final String title;
    private final Event<SimpleEventParams> cancellationEvent;

    /**
     * Display the dialog.
     *
     * @param title the dialog title
     * @param presenter the controller used to create the body of the dialog
     */
    public static void show(String title, JPanelPresenter presenter) {
        instance = new Dialog(title, presenter, false, null);
    }

    /**
     * Display the modal dialog.
     *
     * @param title the dialog title
     * @param presenter the presenter used to create the body of the dialog
     * @param onCancellation listener for dialog completion
     */
    public static void showModal(String title, JPanelPresenter presenter, Listener<SimpleEventParams> onCancellation) {
        instance = new Dialog(title, presenter, true, onCancellation);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private Dialog(String title, JPanelPresenter presenter, boolean isModal, Listener<SimpleEventParams> onCancellation) {
        this.presenter = presenter;
        presenter.enableView();
        JPanelView view = (JPanelView) this.presenter.getView();
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
