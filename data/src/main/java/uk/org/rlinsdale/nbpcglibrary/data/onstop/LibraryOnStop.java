/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package uk.org.rlinsdale.nbpcglibrary.data.onstop;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import org.netbeans.api.actions.Savable;
import uk.org.rlinsdale.nbpcglibrary.common.ConfirmationDialog;
import org.openide.modules.OnStop;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * The Standard On Stop action - tests if forms have error, and dialogs with
 * user to check if close is to continue.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
