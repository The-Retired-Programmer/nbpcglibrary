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

import uk.theretiredprogrammer.nbpcglibrary.htmlrest.AandA;
import java.io.InputStream;
import org.openide.windows.WindowManager;
import uk.theretiredprogrammer.nbpcglibrary.api.Settings;
import uk.theretiredprogrammer.nbpcglibrary.form.Dialog;
import uk.theretiredprogrammer.nbpcglibrary.api.ApplicationProperties;
import uk.theretiredprogrammer.nbpcglibrary.lifecycle.BasicLifeCycle;

/**
 * Abstract Class to handle Application Initialisation
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public abstract class AuthLifeCycle extends BasicLifeCycle implements Runnable {

    private static int authStatus;
    private static String authMessage;
    private String jwtoken;
    
    /**
     * Constructor.
     *
     * @param in the Application Properties File inputstream
     */
    protected AuthLifeCycle(InputStream in) {
        super(in);
    }
    
    @Override
    public void run() {
            Dialog.showModal(
                    ApplicationProperties.getDefault().get("application.title"),
                    new LoginPresenter((user,pwd)-> processAuthData(user,pwd)));
    }
    
    private void processAuthData(String user, String pwd){
        AandA auth = new AandA(
                Settings.get("auth.server"),
                ApplicationProperties.getDefault().get("jwt.claims.prefix")
                );
        auth.authenticate(user,pwd);
        authStatus = auth.getLastAuthStatus();
        authMessage = auth.getLastAuthMessage();
        jwtoken = auth.getJWToken();
        WindowManager.getDefault().setRole(isProblem() ? "PROBLEMS" : isWarning() ? "WARNINGS" : "OPERATIONAL");
        postInitialisation();
    } 

     /**
     * Test if Settings have been saved by the user - ie some
     * settings initialisation has occurred.
     * 
     * @return true if the Setting appear to be initialised 
     */
    static boolean areSettingsSaved() {
        String prefix = ApplicationProperties.getDefault().get("jwt.claims.prefix");
        return Settings.get(prefix+".settings.saved") != null;
    }
    
    /**
     * Get the result of the  User authentication.
     * 
     * @return authentication result
     */
    static int authenticationResult() {
        return authStatus;
    }
    
    /**
     * Get the error message if authentication problem.
     * 
     * @return authentication message
     */
    static String authenticationMessage() {
        return authMessage;
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
     * @return the jwtoken
     */
    public String getJwtoken() {
        return jwtoken;
    }
}
