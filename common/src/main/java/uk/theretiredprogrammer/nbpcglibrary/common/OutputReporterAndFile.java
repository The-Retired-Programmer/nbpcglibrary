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

package uk.theretiredprogrammer.nbpcglibrary.common;

import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.openide.filesystems.FileObject;

/**
 * Provides convenience implementation for writing messages and errors to the
 * output window, and also to a recording file.
 * 
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class OutputReporterAndFile extends OutputReporter {

    private PrintWriter logwriter;

    /**
     * Constructor
     * 
     * @param tabtitle the title to be inserted into the tab (within the output
     * window) 
     * @param folder the folder into which the reporting file is to be written
     * @param logfilebasename the base filename for the reporting file
     */
    public OutputReporterAndFile(String tabtitle, FileObject folder, String logfilebasename) {
        super(tabtitle, null);
        FileObject logfo;
        int index = 0;
        String indextext = "";
        String logfilename;
        while (true) {
            logfilename = logfilebasename + indextext + ".txt";
            logfo = folder.getFileObject(logfilename);
            if (logfo == null) {
                break;
            }
            index++;
            indextext = "_" + Integer.toString(index);
        }
        super.writeMsg("Logging to file: " + logfilename);
        try {
            logwriter = new PrintWriter(new OutputStreamWriter(folder.createAndOpen(logfilename)));
        } catch (IOException ex) {
            throw new LogicException("IO Exception when opening reporting File", ex);
        }
    }

    @Override
    public void writeMsg(String message) {
        super.writeMsg(message);
        if (logwriter != null) {
            logwriter.println(message);
        }
    }

    @Override
    public void writeMsg() {
        super.writeMsg();
        if (logwriter != null) {
            logwriter.println();
        }
    }

    @Override
    public void writeMsgText(String message) {
        super.writeMsgText(message);
        if (logwriter != null) {
            logwriter.print(message);
        }
    }

    /**
     * Write a message to the output window ONLY (a newline is added to the output)
     * 
     * @param message the message
     */
    public void writeMsgToDisplayOnly(String message) {
        super.writeMsg(message);
    }

    @Override
    public void writeErr(String message) {
        super.writeErr(message);
        if (logwriter != null) {
            logwriter.println(message);
        }
    }

    @Override
    public void close() {
        super.close();
        if (logwriter != null) {
            logwriter.close();
        }
    }
}
