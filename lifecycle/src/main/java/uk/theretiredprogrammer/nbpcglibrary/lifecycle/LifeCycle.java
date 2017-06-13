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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.openide.LifecycleManager;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterLog;
import uk.theretiredprogrammer.nbpcglibrary.form.Dialog;
import uk.theretiredprogrammer.nbpcglibrary.form.ErrorInformationDialog;
import org.openide.windows.WindowManager;
import uk.theretiredprogrammer.nbpcglibrary.api.PersistenceUnitProvider;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityPersistenceProviderManager;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.common.SimpleEventParams;
import uk.theretiredprogrammer.nbpcglibrary.form.CompositePresenter;
import uk.theretiredprogrammer.nbpcglibrary.form.PanePresenter;

/**
 * Abstract Class to handle Application Initialisation
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@RegisterLog("nbpcglibrary.lifecycle")
public abstract class LifeCycle {

    private static final ExitApplication EXITAPPLICATION = new ExitApplication();
    private final OnCancellation onCancellation = new OnCancellation();

    /**
     * Constructor.
     *
     * @param in the Application Properties File inputstream
     * @throws ApplicationPropertiesException if problems
     */
    protected LifeCycle(InputStream in) throws ApplicationPropertiesException {
        ApplicationProperties.set(in);
    }

    /**
     * core initialisation
     *
     * @throws IOException if problems
     */
    protected void init() throws IOException {
        WindowManager.getDefault().setRole("NONE");
    }

    /**
     * Test if all required DataAccessManagers are Operational - ie available
     * and able to access data.
     *
     * @return true if all required DataAccessManager are operational
     */
    public boolean areAllDataAccessManagersOperational() {
        boolean allOperational = true;
        for (PersistenceUnitProvider ds : EntityPersistenceProviderManager.getAllPersistenceUnitProviders()) {
            if (ds.isOperational()) {
                LogBuilder.create("nbpcglibrary.lifecycle", Level.INFO).addMethodName(this, "areAllDataAccessManagersOperational")
                        .addMsg("Data Service {0} is operational", ds.getName()).write();
            } else {
                allOperational = false;
                LogBuilder.create("nbpcglibrary.lifecycle", Level.INFO).addMethodName(this, "areAllDataAccessManagersOperational")
                        .addMsg("Data Service {0} is not operational", ds.getName()).write();
            }
        }
        return allOperational;
    }

    /**
     * Display the registration form (if any models are defined).
     *
     * @param presenters a set of presenters for the registration form
     */
    protected final void displayRegistrationForm(PanePresenter... presenters) {
        List<PanePresenter> p = Arrays.asList(presenters).stream().filter(presenter -> presenter != null).collect(Collectors.toList());
        if (!p.isEmpty()) {
            CompositePresenter presenter = new CompositePresenter("Registration");
            presenter.setGetChildPresentersFunction(() -> p);
            Dialog.showModal(
                    ApplicationProperties.get().get("application.title"),
                    presenter, onCancellation);
        }
    }

    /**
     * Handler to exit application following a terminal exception catch. Close
     * down the application with a simple dialog and then stop
     *
     * @param ex the terminal exception
     */
    public static void stop(Exception ex) {
        ErrorInformationDialog.show("Fatal Program Failure", ex.toString(), EXITAPPLICATION);
    }

    /**
     * Handler to exit application. Close down the application with a simple
     * dialog and then stop
     *
     * @param title the error dialog title
     * @param message the error dialog message
     */
    public static void stop(String title, String message) {
        ErrorInformationDialog.show(title, message, EXITAPPLICATION);
    }

    /**
     * An Listener Class which when fired will close down the application.
     */
    private static class ExitApplication extends Listener<SimpleEventParams> {

        /**
         * Constructor.
         */
        public ExitApplication() {
            super("Exit_Application");
        }

        @Override
        public void action(SimpleEventParams lp) {
            LifecycleManager.getDefault().exit();
        }
    }

    /**
     * On Cancellation Class - listener class which is called on dialogue
     * cancellation
     */
    public final class OnCancellation extends Listener<SimpleEventParams> {

        /**
         * Constructor
         */
        public OnCancellation() {
            super("LifeCycle-OnCancellation");
        }

        @Override
        public void action(SimpleEventParams p) {
            ErrorInformationDialog.show("Terminating Application", "Initialisation cancelled/closed by User", EXITAPPLICATION);
        }
    }
}
