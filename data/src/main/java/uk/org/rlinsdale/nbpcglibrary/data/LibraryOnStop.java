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
package uk.org.rlinsdale.nbpcglibrary.data;

import java.util.concurrent.Callable;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRegistry;
import uk.org.rlinsdale.nbpcglibrary.form.ConfirmationDialog;
import org.openide.modules.OnStop;

/**
 * The Standard On Stop action - tests if forms have error entries and dialogs
 * with user to check if close is to continue.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
@OnStop
public class LibraryOnStop implements Callable<Boolean> {

    @Override
    public Boolean call() {
        return EntityRegistry.hasErrors()
                ? ConfirmationDialog.show("Close down request", "Do you want to continue with close down while you have entities with errors which cannot be saved at the present time?")
                : true;
    }
}
