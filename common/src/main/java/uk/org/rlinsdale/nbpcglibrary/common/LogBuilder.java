/*
 * Copyright (C) 2015 Richard Linsdale.
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 * LogBuilder is used to create and write log messages.
 * 
 * It also supports a registry of registered Logs and methods to support
 * the maintenance of the associated levels for these.
 * 
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
     * Create a new instance of a LogBuilder for a defined log at the requested level.
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
            if (ex != null) {
                log.log(level, msgbuilder.toString());
            } else {
                log.log(level, msgbuilder.toString(), ex);
            }
        }
    }

    /**
     * Add a method string to the log message
     * (format is space classname dot methodname).
     * @param className the class name
     * @param methodName the method name
     * @return the LogBuilder
     */
    public LogBuilder addMethodName(String className, String methodName) {
        if (shouldBuild) {
            msgbuilder.append(' ').append(className).append('.').append(methodName);
        }
        return this;
    }

    /**
     * Add a constructor string to the log message
     * (format is space classname).
     * @param className the class name
     * @return the LogBuilder
     */
    public LogBuilder addConstructorName(String className) {
        if (shouldBuild) {
            msgbuilder.append(' ').append(className);
        }
        return this;
    }

    /**
     * Add a method string with parameters to the log message
     * (format is space classname dot methodname bra [param [comma param]] ket).
     * @param className the class name
     * @param methodName the method name
     * @param parameters the parameters for this method call
     * @return the LogBuilder
     */
    public LogBuilder addMethodName(String className, String methodName, Object... parameters) {
        addMethodName(className, methodName);
        return addMethodParameters(parameters);
    }

    /**
     * Add a constructor string with parameters to the log message
     * (format is space classname bra [param [comma param]] ket).
     * @param className the class name
     * @param parameters the parameters for this constructor
     * @return the LogBuilder
     */
    public LogBuilder addConstructorName(String className, Object... parameters) {
        addConstructorName(className);
        return addMethodParameters(parameters);
    }

    private LogBuilder addMethodParameters(Object... parameters) {
        if (shouldBuild) {
            boolean first = true;
            msgbuilder.append('(');
            for (Object p : parameters) {
                if (!first) {
                    msgbuilder.append(", ");
                }
                msgbuilder.append(p);
            }
            msgbuilder.append(')');
        }
        return this;
    }

    /**
     * Add a message to the log message
     * @param msg the message format string (using MessageFormat standards)
     * @param parameters the parameters to be used to substitute into the message format string
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
     * @param ex the exception
     * @return the LogBuilder
     */
    public LogBuilder addException(Exception ex) {
        if (shouldBuild) {
            this.ex = ex;
        }
        return this;
    }

    /* convenience methods */

    /**
     * Convenience method - write a standard log message on entering a method (level is FINER)
     * @param logname  the log name
     * @param className the class name
     * @param methodName the method name
     * @param parameters the method parameters
     */
    
    public static void writeEnteringLog(String logname, String className, String methodName, Object... parameters) {
        new LogBuilder(logname, Level.FINER).addMsg("Entering").addMethodName(className, methodName, parameters).write();
    }

    /**
     * Convenience method - write a standard log message on entering a constructor (level is FINER)
     * @param logname  the log name
     * @param className the class name
     * @param parameters the constructor parameters
     */
    public static void writeEnteringConstructorLog(String logname, String className, Object... parameters) {
        new LogBuilder(logname, Level.FINER).addMsg("Entering").addConstructorName(className, parameters).write();
    }

    /**
     * Convenience method - write a standard log message on exiting a method (level is FINER)
     * @param logname  the log name
     * @param className the class name
     * @param methodName the method name
     */
    public static void writeExitingLog(String logname, String className, String methodName) {
        new LogBuilder(logname, Level.FINER).addMsg("Exiting").addMethodName(className, methodName).write();
    }

    /**
     * Convenience method - write a standard log message on exiting a method (level is FINER)
     * @param logname  the log name
     * @param className the class name
     * @param methodName the method name
     * @param result the result being returned by this method
     */
    public static void writeExitingLog(String logname, String className, String methodName, Object result) {
        new LogBuilder(logname, Level.FINER).addMsg("Exiting").addMethodName(className, methodName).addMsg("with result {0}", result).write();
    }

    /**
     * Convenience method - write a standard log message.
     * Includes the class name and method name (but no parameters) and message text 
     * with optional parameters.
     * @param logname  the log name
     * @param level  the level for this log message
     * @param className the class name
     * @param methodName the method name
     * @param msg the message format string (using MessageFormat standards)
     * @param parameters the parameters to be used to substitute into the message format string
     */
    public static void writeLog(String logname, Level level, String className, String methodName, String msg, Object... parameters) {
        new LogBuilder(logname, level).addMethodName(className, methodName).addMsg(msg, parameters).write();
    }

    private static final String defaultleveltext = "MINIMUM";
    private static final String[] choiceofLevelText = new String[]{"ALL", "Omit FINEST", "Omit FINEST and FINER", "MINIMUM"};
     private static final LogRegistry registry = new LogRegistry();

    /**
     * Get the set of descriptions associated with logging levels
     *
     * @return the set of descriptions texts
     */
    public static String[] getLevelTexts() {
        return choiceofLevelText;
    }

    /**
     * Get the description for the current level associated with a logger.
     *
     * @param name the logger name
     * @return the level description
     */
    public static String getLevelTextfromName(String name) {
        Level level = registry.get(name).getLevel();
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

    /**
     * Get a level value from standard description level text.
     *
     * @param val from standard description level text
     * @return the level value
     */
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

    /**
     * Set a logger's level based on standard descriptions for levels.
     *
     * @param name the logger name
     * @param val the level description
     */
    public static void setLevelfromText(String name, String val) {
        registry.get(name).setLevel(getLevelfromLevelText(val));
        Settings.set("LOG-" + name, val);
    }

    /**
     * Set all loggers to a common level.
     *
     * @param level the common logging level
     */
    public static void setAllLevel(Level level) {
        registry.setAllLevel(level);
    }

    /**
     * Get the set of registered logger names.
     *
     * @return the set of registered logger names
     */
    public static List<String> getLoggerNames() {
        return registry.getLoggerNames();
    }

    private static class LogRegistry {

        private static HashMap<String, Logger> loggers;

        private LogRegistry() {
            loggers = new HashMap<>();
            register("nbpcglibrary.common");
            FileObject ff = FileUtil.getConfigFile("/nbpcg/logs"); // get folder
            if (ff != null) {
                for (FileObject cfo : ff.getChildren()) {
                    register(cfo.getName().replace('-', '.'));
                }
            }
        }

        private void register(String name) {
            Logger l = Logger.getLogger(name);
            Level lev = getLevelfromLevelText(Settings.get("LOG-" + name, defaultleveltext));
            l.setLevel(lev);
            loggers.put(name, l);
            LogBuilder.create("nbpcglibrary.common", Level.CONFIG).addMethodName("LogRegistry", "register", name)
                    .addMsg("level is {0}", lev).write();
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
            for (Map.Entry<String, Logger> e : loggers.entrySet()) {
                e.getValue().setLevel(level);
            }
        }
    }
}
