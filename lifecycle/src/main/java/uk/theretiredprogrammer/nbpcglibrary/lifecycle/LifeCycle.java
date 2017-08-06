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
import org.openide.LifecycleManager;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterLog;
import uk.theretiredprogrammer.nbpcglibrary.form.ErrorInformationDialog;
import org.openide.windows.WindowManager;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityPersistenceProviderManager;
import uk.theretiredprogrammer.nbpcglibrary.lifecycle.auth.AandA;
import uk.theretiredprogrammer.nbpcglibrary.lifecycle.auth.AandA.AUTHENTICATION_RESULT;
import uk.theretiredprogrammer.nbpcglibrary.lifecycle.auth.AandA.AuthData;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.common.Settings;
import uk.theretiredprogrammer.nbpcglibrary.common.SimpleEventParams;
import uk.theretiredprogrammer.nbpcglibrary.form.Dialog;

/**
 * Abstract Class to handle Application Initialisation
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@RegisterLog("nbpcglibrary.lifecycle")
public abstract class LifeCycle implements Runnable {
    
    private final InputStream ppconfig;
    private static long persistenceUnitProviderFailures = 0;
    private static AUTHENTICATION_RESULT authresult;

    private static final ExitApplication EXITAPPLICATION = new ExitApplication();
    private static boolean authorisationRequired;

    /**
     * Constructor.
     *
     * @param in the Application Properties File inputstream
     * @param ppconfig in persistence provider configuration inputstream
     * @param authorisationRequired true if authorisation is required when opening this application
     */
    protected LifeCycle(InputStream in, InputStream ppconfig, boolean authorisationRequired) {
        LifeCycle.authorisationRequired = authorisationRequired;
        this.ppconfig = ppconfig;
        try {
            ApplicationProperties.set(in);
            
        } catch (ApplicationPropertiesException ex) {
            stop(ex);
        }
    }
    
    @Override
    public void run() {
        if (authorisationRequired) {
            Dialog.showModal(
                    ApplicationProperties.getDefault().get("application.title"),
                    new LoginPresenter((authdata)-> processAuthData(authdata)));
        }
        if (!isAuthorisationProblem()) {
            if (ppconfig != null ) {
                try {
                    EntityPersistenceProviderManager.init(ppconfig);
                } catch (IOException ex) {
                    stop(ex);
                }
            }
            persistenceUnitProviderFailures = persistenceUnitProvidersOperationalCheck();
        }
        WindowManager.getDefault().setRole(isProblem() ? "PROBLEMS" : isWarning() ? "WARNINGS" : "OPERATIONAL");
    }
    
    private void processAuthData(AuthData authdata){
        authresult = AandA.authenticate(
                ApplicationProperties.getDefault().get("jwt.claims.prefix"),
                Settings.get("auth.server"),
                authdata);
    } 

    /**
     * Test if PersistenceUnitProviders are available.
     * 
     * @return true if all PersistenceUnitProviders appear to be available
     */
    static long getPersistenceUnitProviderFailures() {
        return persistenceUnitProviderFailures;
    }
    
     /**
     * Test if Settings have been saved by the user - ie some
     * settings initialisation has occurred.
     * 
     * @return true if the Setting appear to be initialised 
     */
    static boolean areSettingsSaved() {
        return Settings.get(ApplicationProperties.getDefault().get("jwt.claims.prefix")+"settings.saved") != null;
    }
    
    /**
     * Get the result of the  User authentication.
     * 
     * @return authentication result or null if no authentication resquired
     */
    static AUTHENTICATION_RESULT authenticationResult() {
        return authorisationRequired ? authresult : null;
    }
    
    /**
     * Test if an authorisation problem has occurred.
     * 
     * @return true if an authorisation problem observed
     */
    static boolean isAuthorisationProblem() {
        return authorisationRequired ? authresult != AUTHENTICATION_RESULT.OK : false;
    }
    
    /**
     * Test if a startup problem has occurred.
     * 
     * @return true if startup problem observed
     */
    static boolean isProblem() {
        return  isAuthorisationProblem() || (persistenceUnitProviderFailures > 0);
    }
    
    /**
     * Test if a startup warning has occurred.
     * 
     * @return true if startup warning observed
     */
    static boolean isWarning() {
        return (!areSettingsSaved());
    }

    private long persistenceUnitProvidersOperationalCheck() {
        return EntityPersistenceProviderManager.getAllPersistenceUnitProviders().stream().filter( (psp) -> {
            if (psp.isOperational()) {
                LogBuilder.create("nbpcglibrary.lifecycle", Level.INFO).addMethodName(this, "PersistenceUnitProvidersOperationalCheck")
                        .addMsg("PersistenceUnitProvider {0} is operational", psp.getName()).write();
                return false;
            } else {
                LogBuilder.create("nbpcglibrary.lifecycle", Level.INFO).addMethodName(this, "PersistenceUnitProvidersOperationalCheck")
                        .addMsg("PersistenceUnitProvider {0} is not operational", psp.getName()).write();
                return true;
            }
        }).count();
    }
    
    /**
     * Handler to exit application following a terminal exception catch. Close
     * down the application with a simple dialog and then stop
     *
     * @param ex the terminal exception
     */
    static void stop(Exception ex) {
        ErrorInformationDialog.show("Fatal Program Failure", ex.toString(), EXITAPPLICATION);
    }

    /**
     * Handler to exit application. Close down the application with a simple
     * dialog and then stop
     *
     * @param title the error dialog title
     * @param message the error dialog message
     */
    static void stop(String title, String message) {
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
}
