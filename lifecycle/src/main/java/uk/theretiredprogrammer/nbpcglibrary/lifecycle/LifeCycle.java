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

import uk.theretiredprogrammer.nbpcglibrary.htmlrest.AandA;
import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;
import org.openide.LifecycleManager;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterLog;
import uk.theretiredprogrammer.nbpcglibrary.form.ErrorInformationDialog;
import org.openide.windows.WindowManager;
import uk.theretiredprogrammer.nbpcglibrary.api.ApplicationLookup;
import uk.theretiredprogrammer.nbpcglibrary.api.Rest;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import uk.theretiredprogrammer.nbpcglibrary.common.Settings;
import uk.theretiredprogrammer.nbpcglibrary.form.Dialog;

/**
 * Abstract Class to handle Application Initialisation
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@RegisterLog("nbpcglibrary.lifecycle")
public abstract class LifeCycle implements Runnable {
    
    private static final ExitApplication EXITAPPLICATION = new ExitApplication();
    private static boolean authorisationRequired;
    private static List<Supplier<? extends Rest>> restclasscreators;

    /**
     * Constructor.
     *
     * @param in the Application Properties File inputstream
     * @param restclasscreators list of classes to be used to access data
     * @param authorisationRequired true if authorisation is required when opening this application
     */
    protected LifeCycle(InputStream in, List<Supplier<? extends Rest>> restclasscreators, boolean authorisationRequired) {
        LifeCycle.authorisationRequired = authorisationRequired;
        LifeCycle.restclasscreators = restclasscreators;
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
                    new LoginPresenter((user,pwd)-> processAuthData(user,pwd, restclasscreators)));
        }
    }
    
    private void processAuthData(String user, String pwd, List<Supplier<? extends Rest>> restclasscreators){
        AandA.authenticate(
                ApplicationProperties.getDefault().get("jwt.claims.prefix"),
                Settings.get("auth.server"),
                user,pwd);
        restclasscreators.forEach(creator-> ApplicationLookup.getDefault().add(creator.get()));
        WindowManager.getDefault().setRole(isProblem() ? "PROBLEMS" : isWarning() ? "WARNINGS" : "OPERATIONAL");
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
     * @return authentication result or 0 if no authentication required
     */
    static int authenticationResult() {
        return authorisationRequired ? AandA.getLastAuthStatus() : 0;
    }
    
    /**
     * Test if an authorisation problem has occurred.
     * 
     * @return true if an authorisation problem observed
     */
    static boolean isAuthorisationProblem() {
        return authenticationResult() != 200;
    }
    
    /**
     * Test if a startup problem has occurred.
     * 
     * @return true if startup problem observed
     */
    static boolean isProblem() {
        return  isAuthorisationProblem() ;
    }
    
    /**
     * Test if a startup warning has occurred.
     * 
     * @return true if startup warning observed
     */
    static boolean isWarning() {
        return (!areSettingsSaved());
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
    private static class ExitApplication extends Listener {

        @Override
        public void action(Object lp) {
            LifecycleManager.getDefault().exit();
        }
    }
}
