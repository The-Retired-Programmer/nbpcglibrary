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
package uk.theretiredprogrammer.nbpcglibrary.lifecycle;

import uk.theretiredprogrammer.nbpcglibrary.api.ApplicationProperties;
import java.io.IOException;
import java.io.InputStream;
import org.openide.LifecycleManager;
import uk.theretiredprogrammer.nbpcglibrary.form.ErrorInformationDialog;

/**
 * Abstract Class to handle Application Initialisation
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public abstract class BasicLifeCycle  implements Runnable {

    private static final ExitApplication EXITAPPLICATION = new ExitApplication();

    /**
     * Constructor.
     *
     * @param in the Application Properties File inputstream
     */
    protected BasicLifeCycle(InputStream in) {
        try {
            ApplicationProperties.set(in);
        } catch (IOException ex) {
            stop(ex);
        }
    }
    
    public void postInitialisation() {
    };
    
    @Override
    public void run() {
        postInitialisation();
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
     * Close down the application.
     */
    private static class ExitApplication implements Runnable {

        @Override
        public void run() {
            LifecycleManager.getDefault().exit();
        }
    }
}
