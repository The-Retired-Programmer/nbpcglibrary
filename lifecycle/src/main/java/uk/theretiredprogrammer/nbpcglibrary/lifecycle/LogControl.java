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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.form.BasicFieldModel;
import uk.theretiredprogrammer.nbpcglibrary.form.ChoiceField;
import uk.theretiredprogrammer.nbpcglibrary.form.Dialog;
import uk.theretiredprogrammer.nbpcglibrary.form.FieldPresenter;
import uk.theretiredprogrammer.nbpcglibrary.form.FormPresenter;
import uk.theretiredprogrammer.nbpcglibrary.form.LabelDecorator;
import uk.theretiredprogrammer.nbpcglibrary.form.NullField;
import uk.theretiredprogrammer.nbpcglibrary.form.NullFieldModel;

/**
 * Action to control the logging levels for the Application.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@ActionID(category = "Tools",
        id = "uk.theretiredprogrammer.nbpcglibrary.lifecycle.LogControl")
@ActionRegistration(iconBase = "uk/theretiredprogrammer/nbpcglibrary/lifecycle/wrench_orange.png",
        displayName = "#CTL_LogControl")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 1900)
})
@Messages("CTL_LogControl=Vary Logging levels")
public final class LogControl implements ActionListener {

    private final List<LogField> logfields = new ArrayList<>();
    private FieldPresenter<String> changeall;

    @Override
    public void actionPerformed(ActionEvent e) {
        FormPresenter form = new FormPresenter("Log Control");
        List<String> choices = Arrays.asList(LogBuilder.getLevelTexts());
        List<FieldPresenter> fpl = new ArrayList<>();
        fpl.add(changeall = new FieldPresenter<>("Change All",
                new LabelDecorator("Change All Logging Levels", new ChoiceField()),
                new BasicFieldModel<>(null, choices, false, value -> onChangeToChangeall(value))
        ));
        fpl.add(new FieldPresenter(new NullField(), new NullFieldModel())); // create an empty row as a spacer
        LogBuilder.getLoggerNames().stream().forEach((name) -> {
            LogField lv = new LogField();
            logfields.add(lv);
            FieldPresenter fp = new FieldPresenter<>(name,
                    new LabelDecorator(name, new ChoiceField()),
                    lv.createModel(name, choices)
            );
            fpl.add(fp);
            lv.setPresenter(fp);
        });
        form.setGetChildPresentersFunction(() -> fpl);
        Dialog.show("Vary Logging levels", form);
    }

    private void onChangeToChangeall(String value) {
        if (value != null) {
            logfields.stream().forEach(logfield -> logfield.update(value));
            changeall.set(null);
        }
    }

    private class LogField {

        private FieldPresenter presenter;
        private BasicFieldModel<String> model;
        private String name;

        public BasicFieldModel<String> createModel(String name, List<String> choices) {
            this.name = name;
            return model = new BasicFieldModel<>(LogBuilder.getLevelTextfromName(name), choices, false, value -> onChangeToLogField(value));
        }

        public void setPresenter(FieldPresenter presenter) {
            this.presenter = presenter;
        }

        private boolean onChangeToLogField(String value) {
            LogBuilder.setLevelfromText(name, value);
            return true;
        }

        public void update(String value) {
            presenter.set(value);
        }
    }
}
