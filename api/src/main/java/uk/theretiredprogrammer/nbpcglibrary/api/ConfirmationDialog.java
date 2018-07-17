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
package uk.theretiredprogrammer.nbpcglibrary.api;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * A basic Confirmation Dialog (YES / NO)
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
        return res;
    }
}
