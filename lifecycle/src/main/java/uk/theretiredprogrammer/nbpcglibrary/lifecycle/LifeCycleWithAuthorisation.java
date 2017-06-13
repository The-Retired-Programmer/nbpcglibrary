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

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import org.openide.windows.WindowManager;
import uk.theretiredprogrammer.nbpcglibrary.authentication.Authentication;
import static uk.theretiredprogrammer.nbpcglibrary.lifecycle.LifeCycle.stop;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.common.Settings;
import uk.theretiredprogrammer.nbpcglibrary.form.PanePresenter;

/**
 * Core Routines to handle Application Initialisation with Authorisation
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public abstract class LifeCycleWithAuthorisation extends LifeCycle implements Runnable {

    private final PanePresenter presenter;

    /**
     * Constructor.
     *
     * @param in the Application Properties File inputstream
     * @param presenter the additional presenter required for the registration
     * form
     * @throws ApplicationPropertiesException if problems reading/parsing
     * properties
     */
    protected LifeCycleWithAuthorisation(InputStream in, PanePresenter presenter) throws ApplicationPropertiesException {
        super(in);
        this.presenter = presenter;
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException ex) {
            LogBuilder.create("nbpcglibrary.lifecycle", Level.INFO).addMethodName(this, "run")
                    .addExceptionMessage(ex).write();
            stop("Data Access problem", "Connection to required datasources is not available");
        }
        if (areAllDataAccessManagersOperational()) {
            if ("yes".equals(Settings.get("NeedsRegistration", "yes"))) {
                displayRegistrationForm(new UserRegistrationPresenter(), presenter);
            } else { // do a quick authentication against the current username
                if (Authentication.authenticate(ApplicationProperties.get().get("application.key"), Settings.get("Username"))) {
                    String role = Authentication.getRole();
                    LogBuilder.create("nbpcglibrary.lifecycle", Level.INFO).addMethodName(this, "run")
                            .addMsg("Quick Authentication: User Role is {0}", role).write();
                    WindowManager.getDefault().setRole(role);
                } else {
                    LogBuilder.create("nbpcglibrary.lifecycle", Level.INFO).addMethodName(this, "run")
                            .addMsg("Application Registration Failure - quick authentication failure").write();
                    stop("Authentication Failure", "Access to application denied");
                }
            }
        } else {
            LogBuilder.create("nbpcglibrary.lifecycle", Level.INFO).addMethodName(this, "run")
                    .addMsg("Application Start-up failure - not all data sources are available").write();
            stop("Data Access problem", "Connection to required datasources is not available");
        }
    }
}
