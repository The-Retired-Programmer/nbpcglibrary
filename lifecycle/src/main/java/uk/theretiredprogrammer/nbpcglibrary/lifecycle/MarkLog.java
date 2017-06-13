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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.api.Timestamp;
import uk.theretiredprogrammer.nbpcglibrary.form.BasicFieldModel;
import uk.theretiredprogrammer.nbpcglibrary.form.Dialog;
import uk.theretiredprogrammer.nbpcglibrary.form.FieldPresenter;
import uk.theretiredprogrammer.nbpcglibrary.form.FormPresenter;
import uk.theretiredprogrammer.nbpcglibrary.form.LabelDecorator;
import uk.theretiredprogrammer.nbpcglibrary.form.TextField;

/**
 * Write a Marker into the Log.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@ActionID(category = "Tools",
        id = "uk.theretiredprogrammer.nbpcglibrary.lifecycle.MarkLog")
@ActionRegistration(iconBase = "uk/theretiredprogrammer/nbpcglibrary/lifecycle/comment_add.png",
        displayName = "#CTL_MarkLog")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 2000)
})
@Messages("CTL_MarkLog=Write Comment into Log")
public final class MarkLog implements ActionListener {

    private BasicFieldModel<String> commentModel;

    @Override
    public void actionPerformed(ActionEvent e) {
        FormPresenter form = new FormPresenter();
        form.setSaveFunction(sb -> save(sb));
        form.setGetChildPresentersFunction(() -> getFieldPresenters());
        Dialog.show("Write Comment into Log", form);
    }

    private List<FieldPresenter> getFieldPresenters() {
        return Arrays.asList(
                new FieldPresenter<>("Comment",
                        new LabelDecorator("Comment", new TextField()),
                        commentModel = new BasicFieldModel<>(""))
        );
    }

    private boolean save(StringBuilder sb) {
        LogBuilder.create("nbpcglibrary.lifecycle", Level.INFO)
                .addMsg("******** USER MARK @ {0} - {1}",
                        (new Timestamp()).toString(), commentModel.get()).write();
        return true;
    }
}
