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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 * Logging implementation Class
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class Log {

    private static final Log instance = new Log();
    private static HashMap<String, Logger> loggers;
    @SuppressWarnings("NonConstantLogger")
    private static Logger globalLogger;
    private static String defaultleveltext;
    private static String[] choiceofLevelText;

    private Log() {
        loggers = new HashMap<>();
        globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        defaultleveltext = "MINIMUM";
        choiceofLevelText = new String[]{"ALL", "Omit FINEST", "Omit FINEST and FINER", "MINIMUM"};
        FileObject ff = FileUtil.getConfigFile("/nbpcg/logs"); // get folder
        if (ff != null) {
            for (FileObject cfo : ff.getChildren()) {
                registerLog(cfo.getName().replace('-', '.'));
            }
        }
    }

    /**
     * Register a Logger
     *
     * @param name the name of the log
     * @param level the level for the log
     */
    private void registerLog(String name) {
        Logger l = Logger.getLogger(name);
        Level lev = getLevelfromLevelText(Settings.get("LOG-" + name, defaultleveltext));
        l.setLevel(lev);
        l.log(Level.CONFIG, "Logger {0} registered: {1}", new Object[]{name, lev});
        loggers.put(name, l);
    }

    public static String[] getLevelTexts() {
        return choiceofLevelText;
    }

    /**
     * Get a logger
     *
     * @param name the log name
     * @return a Logger instance
     */
    public static Logger get(String name) {
        Logger l = loggers.get(name);
        if (l == null) {
            l = globalLogger;
        }
        return l;
    }

    public static List<String> getLoggerNames() {
        List<String> res = new ArrayList<>();
        for (Map.Entry<String, Logger> e : loggers.entrySet()) {
            res.add(e.getKey());
        }
        return res;
    }

    public static void setAllLevel(Level level) {
        for (Map.Entry<String, Logger> e : loggers.entrySet()) {
            e.getValue().setLevel(level);
        }
    }

    public static void setLevelfromText(String name, String val) {
        get(name).setLevel(getLevelfromLevelText(val));
        Settings.set("LOG-" + name, val);
    }

    public static String getLevelTextfromName(String name) {
        Level level = Log.get(name).getLevel();
        if (level == Level.ALL || level == Level.FINEST) {
            return choiceofLevelText[0];
        } else if (level == Level.FINER) {
            return choiceofLevelText[1];
        } else if (level == Level.FINE) {
            return choiceofLevelText[2];
        } else {
            return choiceofLevelText[3];
        }
    }

    public static Level getLevelfromLevelText(String val) {
        if (val.equals(choiceofLevelText[0])) {
            return Level.ALL;
        } else if (val.equals(choiceofLevelText[1])) {
            return Level.FINER;
        } else if (val.equals(choiceofLevelText[2])) {
            return Level.FINE;
        } else {
            return Level.CONFIG;
        }
    }
}
