/*
 * Copyright 2014-2017 Richard Linsdale.
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
package uk.theretiredprogrammer.nbpcglibrary.data.onstop;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import org.netbeans.api.actions.Savable;
import uk.theretiredprogrammer.nbpcglibrary.common.ConfirmationDialog;
import org.openide.modules.OnStop;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;

/**
 * The Standard On Stop action - tests if forms have error, and dialogs with
 * user to check if close is to continue.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@OnStop
public class LibraryOnStop implements Callable<Boolean> {
    
    private static boolean enableSavable = false;

    @Override
    public Boolean call() {
        return hasErrors()
                ? ConfirmationDialog.show("Close down request", "Do you want to continue with close down while you have entities with errors which cannot be saved at the present time?")
                : true;
    }

    @SuppressWarnings("CallToThreadYield")
    private boolean hasErrors() {
        return outstandingSavableRegistrations > 0 || Savable.REGISTRY.lookup(Savable.class) != null;
    }

    private static int outstandingSavableRegistrations = 0;

    /**
     * Increment the saveables outstanding
     */
    public static void incRegisterOutstanding() {
        outstandingSavableRegistrations++;
        LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMsg("LibraryOnStop.incRegisterOutstanding() - count is now {0}", outstandingSavableRegistrations).write();
    }

    /**
     * Decrement the saveables outstanding
     */
    public static void decRegisterOutstanding() {
        outstandingSavableRegistrations--;
        LogBuilder.create("nbpcglibrary.data", Level.FINEST).addMsg("LibraryOnStop.decRegisterOutstanding() - count is now {0}", outstandingSavableRegistrations).write();
    }
    
    /**
     * Test if Savable to be implemented for entities in this application
     * 
     * @return true if Savable to be enabled
     */
    public static boolean isSavableEnabled() {
        return enableSavable;
    }
    
    /**
     * Set the state of Savable Enabled to true
     */
    public static void setSavableEnabled() {
        enableSavable = true;
    }
    
    /**
     * Set the state of Savable Enabled to false
     */
    public static void setSavableDisabled() {
        enableSavable = false;
    }
}
