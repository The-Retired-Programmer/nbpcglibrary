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

package uk.theretiredprogrammer.nbpcglibrary.api;

import java.io.IOException;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;

/**
 * Provides convenience implementation for writing messages and errors to the
 * output window.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
