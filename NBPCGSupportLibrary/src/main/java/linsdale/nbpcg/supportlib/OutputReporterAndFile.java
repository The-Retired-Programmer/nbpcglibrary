/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package linsdale.nbpcg.supportlib;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class OutputReporterAndFile extends OutputReporter {

    private PrintWriter logwriter;

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
