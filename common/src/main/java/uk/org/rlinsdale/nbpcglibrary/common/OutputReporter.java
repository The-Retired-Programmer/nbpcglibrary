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
package uk.org.rlinsdale.nbpcglibrary.common;

import java.io.IOException;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;

/**
 * Provides convenience implementation for writing messages and errors to the
 * output window.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class OutputReporter {

    private final OutputWriter msg;
    private final OutputWriter err;

    /**
     * Constructor
     *
     * @param tabtitle the title to be inserted into the tab (within the output
     * window)
     * @param initialmessage the initial message to be inserted into the window
     */
    public OutputReporter(String tabtitle, String initialmessage) {
        InputOutput io = IOProvider.getDefault().getIO(tabtitle, false);
        io.select();
        msg = io.getOut();
        err = io.getErr();
        try {
            msg.reset();
        } catch (IOException ex) {
            throw new LogicException("IO Exception when resetting Output", ex);
        }
        if (initialmessage != null) {
            msg.println(initialmessage);
        }
    }

    /**
     * Write a message to the output window (a newline is added to the output)
     * 
     * @param message the message
     */
    public void writeMsg(String message) {
        msg.println(message);
    }

    /**
     * Write a newline to the output window
     */
    public void writeMsg() {
        msg.println();
    }

    /**
     * Write a message to the output window
     * @param message the message
     */
    public void writeMsgText(String message) {
        msg.print(message);
    }

    /**
     * Write a error message to the output window (a newline is added to the output)
     * @param message the error message
     */
    public void writeErr(String message) {
        err.println(message);
    }

    /**
     * the close the output window
     */
    public void close() {
        err.close();
        msg.close();
    }
}
