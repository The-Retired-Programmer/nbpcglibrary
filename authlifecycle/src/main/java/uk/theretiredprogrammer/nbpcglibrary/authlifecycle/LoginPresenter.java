/*
 * Copyright 2015-2018 Richard Linsdale.
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
package uk.theretiredprogrammer.nbpcglibrary.authlifecycle;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import uk.theretiredprogrammer.nbpcglibrary.form.BasicFieldModel;
import uk.theretiredprogrammer.nbpcglibrary.form.FieldPresenter;
import uk.theretiredprogrammer.nbpcglibrary.form.FormPresenter;
import uk.theretiredprogrammer.nbpcglibrary.form.LabelDecorator;
import uk.theretiredprogrammer.nbpcglibrary.form.PasswordField;
import uk.theretiredprogrammer.nbpcglibrary.form.TextField;

/**
 * User registration Presenter. This will be used in the Registration dialog for
 * applications, if lifecycle with authorisation is utilised.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
class LoginPresenter extends FormPresenter {

    private BasicFieldModel<String> usernamemodel;
    private BasicFieldModel<String> passwordmodel;
    private final BiConsumer<String,String> responsehandler;

    /**
     * Constructor
     *
     * @param responsehandler
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    LoginPresenter(BiConsumer<String, String> responsehandler) {
        setSaveFunction(sb -> loginSave(sb));
        setGetChildPresentersFunction(() -> getFieldPresenters());
        this.responsehandler = responsehandler;
    }

    private List<FieldPresenter> getFieldPresenters() {
        return Arrays.asList(
                new FieldPresenter<>("Username",
                        new LabelDecorator("Username", new TextField()),
                        usernamemodel = new BasicFieldModel<>("").addMinStringRule(1).addMaxStringRule(100)
                ),
                new FieldPresenter<>(("Password"),
                        new LabelDecorator("Password", new PasswordField()),
                        passwordmodel = new BasicFieldModel<>("").addMinStringRule(1).addMaxStringRule(100))
        );
    }

    private Boolean loginSave(StringBuilder sb) {
        boolean res;
        if (res = test(sb)) {
            responsehandler.accept(usernamemodel.get(), passwordmodel.get());
        }
        return res;
    }
}
