/*
 * Copyright (C) 2014-2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package uk.org.rlinsdale.nbpcglibrary.mysql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterLog;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;
import uk.org.rlinsdale.nbpcglibrary.localdatabaseaccess.LocalSQLDataAccessManager;

/**
 * The implementation of the DB class for MySql Database connections.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
@RegisterLog("nbpcglibrary.mysql")
public class MySQLLocalDatabaseAccessManager extends LocalSQLDataAccessManager {
    
    private boolean operational = false;
    private final Properties p;

    /**
     * Constructor
     * 
     * @param p the db connection parameters
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public MySQLLocalDatabaseAccessManager(Properties p) {
        super("local-mysql-"+p.getProperty("key"));
        this.p = p;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            LogBuilder.create("nbpcglibrary.mysql", Level.SEVERE).addConstructorName(this, p)
                .addExceptionMessage(ex).write();
        }
        try {
            setConnection(DriverManager.getConnection(p.getProperty("connection"), p.getProperty("user"), p.getProperty("password")));
            operational = true;
        } catch (SQLException ex) {
        }
    }
    
    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, getName());
    }

    @Override
    protected String format(JsonValue value) {
        switch (value.getValueType()) {
            case NULL:
                return "NULL";
            case TRUE:
                return "true";
            case FALSE:
                return "false";
            case STRING:
                String v = ((JsonString)value).getString();
            return "'" + v.replace("\\", "\\\\").replace("'", "\\'").replace("\n","\\n") + "'";
            case NUMBER:
                return ((JsonNumber)value).toString();
            default:
                throw new LogicException("Unknown Object type in MySQLLocalDatabaseAccessManage:format()");
        }
    }

    @Override
    public boolean isOperational() {
        return operational;
    }
    
    @Override
    public String getName() {
        return "local-mysql-"+p.getProperty("key");
    }
}
