/*
 * Copyright 2015-2017 Richard Linsdale.
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
package uk.theretiredprogrammer.nbpcglibrary.lifecycle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import uk.theretiredprogrammer.nbpcglibrary.common.Settings;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to request Application registration on
 * next restart
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@ActionID(category = "Tools",
        id = "uk.theretiredprogrammer.nbpcglibrary.lifecycle.NeedsRegistrationAction")
@ActionRegistration(iconBase = "uk/theretiredprogrammer/nbpcglibrary/lifecycle/key_go.png",
        displayName = "#CTL_NeedsRegistrationAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 1800)
})
@Messages("CTL_NeedsRegistrationAction=Re-register after restart")
public final class NeedsRegistrationAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Settings.set("NeedsRegistration", "yes");
    }
}
