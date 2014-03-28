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
package linsdale.nbpcg.mysql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import linsdale.nbpcg.annotations.RegisterLog;
import linsdale.nbpcg.datasupportlib.dataservice.DBDataService;
import linsdale.nbpcg.supportlib.DateOnly;
import linsdale.nbpcg.supportlib.DbConnectionParameters;
import linsdale.nbpcg.supportlib.Log;
import linsdale.nbpcg.supportlib.LogicException;
import linsdale.nbpcg.supportlib.Timestamp;

/**
 * The implementation of the DB class for MySql Database connections.
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
@RegisterLog("linsdale.mysql")
public class MySQLDataService extends DBDataService {
    
    private boolean operational = false;
    private final DbConnectionParameters p;

    public MySQLDataService(DbConnectionParameters p) {
        super(p.key+"_DBService");
        this.p = p;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Log.get("linsdale.mysql").log(Level.SEVERE, null, ex);
        }
        try {
            setConnection(DriverManager.getConnection(p.connection, p.user, p.password));
            operational = true;
        } catch (SQLException ex) {
        }
    }

    @Override
    protected String format(Object value) {
        if (value == null){
            return "NULL";
        }
        if (value instanceof String) {
            String v = (String)value;
            return "'" + v.replace("\\", "\\\\").replace("'", "\\'").replace("\n","\\n") + "'";
        }
        if (value instanceof Boolean) {
            return ((Boolean) value) ? "true" : "false";
        }
        if (value instanceof Integer) {
            return value.toString();
        }
        if (value instanceof Long) {
            return value.toString();
        }
        if (value instanceof DateOnly) {
            return "'" + ((DateOnly) value).toSQLString() + "'";
        }

        if (value instanceof Timestamp) {
            return "'" + ((Timestamp) value).toSQLString() + "'";
        }
        throw new LogicException("Unknown Object type in MySqlService:format()");
    }

    @Override
    public boolean isOperational() {
        return operational;
    }
    
    @Override
    public String getName() {
        return "/MySQL/" + p.key + "/"+p.description;
    }
}
