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
import static uk.theretiredprogrammer.nbpcglibrary.lifecycle.LifeCycle.stop;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.common.Settings;
import uk.theretiredprogrammer.nbpcglibrary.form.PanePresenter;

/**
 * Core Routines to handle Application Initialisation
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public abstract class SimpleLifeCycle extends LifeCycle implements Runnable {

    private final PanePresenter presenter;

    /**
     * Constructor.
     *
     * @param in the Application Properties File inputstream
     * @param presenter the model required for the registration form
     * @throws ApplicationPropertiesException if problems reading/parsing
     * properties
     */
    protected SimpleLifeCycle(InputStream in, PanePresenter presenter) throws ApplicationPropertiesException {
        super(in);
        this.presenter = presenter;
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException ex) {
            LogBuilder.create("nbpcglibrary.lifecycle", Level.INFO).addMethodName(this, "run")
                    .addMsg("Application Start-up failure - data source(s) failure").write();
            stop("Data Access problem", "Connection to required datasources is not available");
        }
        if (!areAllDataAccessManagersOperational()) {
            LogBuilder.create("nbpcglibrary.lifecycle", Level.INFO).addMethodName(this, "run")
                    .addMsg("Application Start-up failure - not all data sources are available").write();
            stop("Data Access problem", "Connection to required datasources is not available");
        }
        if ("yes".equals(Settings.get("NeedsRegistration", "yes"))) {
            displayRegistrationForm(presenter);
        }
    }
}
