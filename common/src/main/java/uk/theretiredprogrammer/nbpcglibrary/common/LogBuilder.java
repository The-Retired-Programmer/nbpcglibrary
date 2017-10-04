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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 * LogBuilder is used to create and write log messages.
 *
 * It also supports a registry of registered Logs and methods to support the
 * maintenance of the associated levels for these.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class LogBuilder {

    private final StringBuilder msgbuilder = new StringBuilder();
    @SuppressWarnings("NonConstantLogger")
    private final Logger log;
    private final Level level;
    private final boolean shouldBuild;
    private Exception ex = null;

    private LogBuilder(String logname, Level level) {
        log = Logger.getLogger(logname);
        this.level = level;
        shouldBuild = log.isLoggable(level);
    }

    /**
     * Create a new instance of a LogBuilder for a defined log at the requested
     * level.
     *
     * @param logname the log name
     * @param level the level for this log message
     * @return the LogBuilder
     */
    public static LogBuilder create(String logname, Level level) {
        return new LogBuilder(logname, level);
    }

    /**
     * Write the current Log message (as built using this LogBuilder).
     */
    public void write() {
        if (shouldBuild) {
            if (ex == null) {
                log.log(level, msgbuilder.toString());
            } else {
                log.log(level, msgbuilder.toString(), ex);
            }
        }
    }

    /**
     * Add a message to the log message
     *
     * @param msg the message format string (using MessageFormat standards)
     * @param parameters the parameters to be used to substitute into the
     * message format string
     * @return the LogBuilder
     */
    public LogBuilder addMsg(String msg, Object... parameters) {
        if (shouldBuild) {
            msgbuilder.append(' ').append(MessageFormat.format(msg, parameters));
        }
        return this;
    }

    /**
     * Add a message to the log message
     *
     * @param msg the message
     * @return the LogBuilder
     */
    public LogBuilder addMsg(String msg) {
        if (shouldBuild) {
            msgbuilder.append(' ').append(msg);
        }
        return this;
    }

    /**
     * Add an exception reporting request to the Log message
     *
     * @param ex the exception
     * @return the LogBuilder
     */
    public LogBuilder addException(Exception ex) {
        if (shouldBuild) {
            this.ex = ex;
        }
        return this;
    }
    
    /**
     * Add an exception message to the Log message
     *
     * @param ex the exception
     * @return the LogBuilder
     */
    public LogBuilder addExceptionMessage(Exception ex) {
        if (shouldBuild) {
            msgbuilder.append(' ').append(ex.getMessage());
        }
        return this;
    }

    /**
     * Create an instance description from a class name and its instance name
     *
     * @param instance the instance
     * @param instanceName the instance name
     * @return the instance description
     */
    public static String instanceDescription(Object instance, String instanceName) {
        return instance.getClass().getSimpleName() + "[" + instanceName + "]";
    }

    private static final String DEFAULTLEVELTEXT = "MINIMUM";
    private static final String[] CHOICEOFLEVELTEXT = new String[]{"ALL", "Omit FINEST", "Omit FINEST and FINER", "MINIMUM"};
    private static final LogRegistry REGISTRY = new LogRegistry();

    /**
     * Get the set of descriptions associated with logging levels
     *
     * @return the set of descriptions texts
     */
    public static String[] getLevelTexts() {
        return CHOICEOFLEVELTEXT;
    }

    /**
     * Get the description for the current level associated with a logger.
     *
     * @param name the logger name
     * @return the level description
     */
    public static String getLevelTextfromName(String name) {
        Level level = REGISTRY.get(name).getLevel();
        if (level == Level.ALL || level == Level.FINEST) {
            return CHOICEOFLEVELTEXT[0];
        } else if (level == Level.FINER) {
            return CHOICEOFLEVELTEXT[1];
        } else if (level == Level.FINE) {
            return CHOICEOFLEVELTEXT[2];
        } else {
            return CHOICEOFLEVELTEXT[3];
        }
    }

    /**
     * Get a level value from standard description level text.
     *
     * @param val from standard description level text
     * @return the level value
     */
    public static Level getLevelfromLevelText(String val) {
        if (val.equals(CHOICEOFLEVELTEXT[0])) {
            return Level.ALL;
        } else if (val.equals(CHOICEOFLEVELTEXT[1])) {
            return Level.FINER;
        } else if (val.equals(CHOICEOFLEVELTEXT[2])) {
            return Level.FINE;
        } else {
            return Level.CONFIG;
        }
    }

    /**
     * Set a logger's level based on standard descriptions for levels.
     *
     * @param name the logger name
     * @param val the level description
     */
    public static void setLevelfromText(String name, String val) {
        REGISTRY.get(name).setLevel(getLevelfromLevelText(val));
        Settings.set("LOG-" + name, val);
    }

    /**
     * Set all loggers to a common level.
     *
     * @param level the common logging level
     */
    public static void setAllLevel(Level level) {
        REGISTRY.setAllLevel(level);
    }

    /**
     * Get the set of registered logger names.
     *
     * @return the set of registered logger names
     */
    public static List<String> getLoggerNames() {
        return REGISTRY.getLoggerNames();
    }

    private static class LogRegistry {

        private final HashMap<String, Logger> loggers;

        private LogRegistry() {
            loggers = new HashMap<>();
            register("nbpcglibrary.common");
            FileObject ff = FileUtil.getConfigFile("nbpcglibrary/common/LogBuilder/"); // get folder
            if (ff != null) {
                for (FileObject cfo : ff.getChildren()) {
                    register(cfo.getName().replace('-', '.'));
                }
            }
        }

        private void register(String name) {
            Logger l = Logger.getLogger(name);
            Level lev = getLevelfromLevelText(Settings.get("LOG-" + name, DEFAULTLEVELTEXT));
            l.setLevel(lev);
            loggers.put(name, l);
        }

        /**
         * Get a logger
         *
         * @param name the log name
         * @return a Logger instance
         */
        public Logger get(String name) {
            Logger l = loggers.get(name);
            if (l == null) {
                throw new LogicException("Requested logger not registered " + name);
            }
            return l;
        }

        /**
         * Get the set of registered logger names.
         *
         * @return the set of registered logger names
         */
        public List<String> getLoggerNames() {
            List<String> res = new ArrayList<>();
            loggers.entrySet().stream().forEach((e) -> {
                res.add(e.getKey());
            });
            return res;
        }

        /**
         * Set all loggers to a common level.
         *
         * @param level the common logging level
         */
        public void setAllLevel(Level level) {
            loggers.entrySet().forEach((e) -> {
                e.getValue().setLevel(level);
            });
        }
    }
}
