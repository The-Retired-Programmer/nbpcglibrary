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
package uk.org.rlinsdale.nbpcglibrary.data.dataservice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.Listening;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;

/**
 * Abstract Class implementing Database access using JDBC.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class DBDataService implements DataService {

    private Connection conn;
    private final Listening<TransactionListenerParams> transactionListening;
    private boolean inTransaction = false;

    /**
     * Constructor.
     *
     * @param name the dataservice name
     */
    public DBDataService(String name) {
        transactionListening = new Listening<>(name + " transactions");
    }

    /**
     * Set the JDBC connection to be used for this dataservice.
     *
     * @param conn the JDBC connection
     */
    protected final void setConnection(Connection conn) {
        this.conn = conn;
    }

    /**
     * Add a listener for Transaction events (Begin, Commit and Rollback). The
     * listener will be called on the EventQueue.
     *
     * @param l the listener
     */
    public void addListener(Listener<TransactionListenerParams> l) {
        transactionListening.addListener(l);
    }

    /**
     * Remove a listener for Transaction events.
     *
     * @param l the listener
     */
    public void removeListener(Listener<TransactionListenerParams> l) {
        transactionListening.removeListener(l);
    }

    /**
     * Add a listener for Transaction events (Begin, Commit and Rollback).
     *
     * @param l the listener
     * @param flags the listener control flags (fire or event queue or
     * immediately; priority)
     */
    public void addListener(Listener<TransactionListenerParams> l, int flags) {
        transactionListening.addListener(l, flags);
    }

    /**
     * Mark the start of a Transaction unit.
     */
    public void begin() {
        LogBuilder.writeEnteringLog("nbpcglibrary.data", "DBDataService", "begin");
        if (inTransaction) {
            throw new LogicException("begin() failed - already in transaction");
        } else {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException ex) {
                LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName("DBDataService", "begin")
                        .addException(ex).write();
            }
            inTransaction = true;
            transactionListening.fire(TransactionListenerParams.BEGINListenerParams);
        }
    }

    /**
     * Mark the end of a transaction unit, commit all changes.
     */
    public void commit() {
        LogBuilder.writeEnteringLog("nbpcglibrary.data", "DBDataService", "commit");
        if (inTransaction) {
            try {
                conn.commit();
            } catch (SQLException ex) {
                LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName("DBDataService", "commit")
                        .addException(ex).write();
            }
            transactionListening.fire(TransactionListenerParams.COMMITListenerParams);
            inTransaction = false;
        } else {
            throw new LogicException("commit() failed - not in transaction");
        }
    }

    /**
     * Mark the end of a transaction unit, rollback all changes.
     */
    public void rollback() {
        LogBuilder.writeEnteringLog("nbpcglibrary.data", "DBDataService", "rollback");
        if (inTransaction) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName("DBDataService", "rollback")
                        .addException(ex).write();
            }
            transactionListening.fire(TransactionListenerParams.ROLLBACKListenerParams);
            inTransaction = false;
        } else {
            throw new LogicException("rollback() failed - not in transaction");
        }
    }

    /**
     * Test if we are currently within an active transaction unit.
     *
     * @return true if in transaction
     */
    public boolean isInTransaction() {
        return inTransaction;
    }

    /**
     * Disconnect from the current database connection.
     */
    public void disconnect() {
        try {
            conn.close();
        } catch (SQLException ex) {
            LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName("DBDataService", "disconnect")
                        .addException(ex).write();
        }
    }

    /**
     * Execute a Select query on the database to extract the list of Entity Ids
     * which meet selection criteria.
     *
     * The SQL template includes {P} and this will be substituted by the
     * parameter which will be presented according to its data type (e.g.
     * strings will be quoted, dates will be formatted etc, as per specific
     * database standards
     *
     * @param sql the SQL template for the query
     * @param parameter the single parameter used in the query
     * @return list of ids
     */
    public synchronized List<Integer> query(String sql, Object parameter) {
        List<Integer> li = new ArrayList<>();
        try {
            try (Statement stat = conn.createStatement()) {
                ResultSet rs = stat.executeQuery(buildsql(sql, parameter));
                while (rs.next()) {
                    li.add(rs.getInt("id"));
                }
            }
        } catch (SQLException ex) {
            LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName("DBDataService", "query",sql, parameter)
                        .addException(ex).write();
        }
        return li;
    }

    /**
     * Execute a Select query on the database to extract the list of Entity ids.
     *
     * @param sql the SQL for the query
     * @return list of ids
     */
    public synchronized List<Integer> query(String sql) {
        List<Integer> li = new ArrayList<>();
        LogBuilder.writeEnteringLog("nbpcglibrary.data", "DBDataService", "query", sql);
        try {
            try (Statement stat = conn.createStatement()) {
                ResultSet rs = stat.executeQuery(sql);
                while (rs.next()) {
                    li.add(rs.getInt("id"));
                }
            }
        } catch (SQLException ex) {
            LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName("DBDataService", "query", sql)
                        .addException(ex).write();
        }
        return li;
    }

    /**
     * Execute a select query and extract the int value from the defined column
     * of the first row returned.
     *
     * @param sql the SQL for the query
     * @param columnname the column name from which to extract result
     * @return the value of the column
     */
    public synchronized int simpleIntQuery(String sql, String columnname) {
        int res = 0;
        LogBuilder.writeEnteringLog("nbpcglibrary.data", "DBDataService", "simpleIntQuery", sql, columnname);
        try {
            try (Statement stat = conn.createStatement()) {
                ResultSet rs = stat.executeQuery(sql);
                rs.first();
                res = rs.getInt(columnname);
            }
        } catch (SQLException ex) {
            LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName("DBDataService", "simpleIntQuery", sql, columnname)
                        .addException(ex).write();
        }
        return res;
    }

    /**
     * Execute a select query and insert the values from the first row returned
     * into an entity, using the provided loader.
     *
     * @param sql the SQL for the query
     * @param rsl the loader to be used
     */
    public synchronized void simpleQuery(String sql, ResultSetLoader rsl) {
        LogBuilder.writeEnteringLog("nbpcglibrary.data", "DBDataService", "simpleQuery", sql, rsl);
        try {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            if (!rs.first()) {
                throw new LogicException("DBDataService.simpleQuery resulted in an empty ResultSet");
            }
            rsl.load(rs);
        } catch (SQLException ex) {
            LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName("DBDataService", "simpleQuery", sql, rsl)
                        .addException(ex).write();
        }
    }

    /**
     * Execute an Insert, Update or Delete query on the database.
     *
     * The SQL template includes {xxx} where xxx is the key in the parameter
     * map. Parameters will be presented according to their data types (e.g.
     * strings will be quoted, dates will be formatted etc, as per specific
     * database standards. There are also some special parameter formats
     * {$KEYLIST}, $VALUELIST},{$KEYVALUELIST} which will return comma separated
     * lists of keys, formatted values or key=formatted value.
     *
     * @param sql the SQL template for the query
     * @param parameters the parameters to be inserted into the template to
     * create the executable SQL
     * @return the number of records changed due to the query
     */
    public synchronized int execute(String sql, Map<String, Object> parameters) {
        int res = 0;
        try {
            try (Statement stat = conn.createStatement()) {
                res = stat.executeUpdate(buildsql(sql, parameters));
            }
        } catch (SQLException ex) {
            LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName("DBDataService", "execute", sql, parameters)
                        .addException(ex).write();
        }
        return res;
    }

    /**
     * Execute an Insert, Update or Delete query on the database.
     *
     * The SQL template includes {P} and this will be substituted by the
     * parameter which will be presented according to its data type (e.g.
     * strings will be quoted, dates will be formatted etc, as per specific
     * database standards
     *
     * @param sql the SQL template for the query
     * @param parameter the single parameter used in the query
     * @return the number of records changed due to the query
     */
    public synchronized int execute(String sql, Object parameter) {
        int res = 0;
        try {
            try (Statement stat = conn.createStatement()) {
                res = stat.executeUpdate(buildsql(sql, parameter));
            }
        } catch (SQLException ex) {
            LogBuilder.create("nbpcglibrary.data", Level.SEVERE).addMethodName("DBDataService", "execute", sql, parameter)
                        .addException(ex).write();
        }
        return res;
    }

    private String buildsql(String sql, Map<String, Object> parameters) {
        if (sql.contains("{$KEYLIST}") || sql.contains("{$VALUELIST}") || sql.contains("{$KEYVALUELIST}")) {
            // special code to generate and substitute keylists and valuelists
            String keylist = "";
            String valuelist = "";
            String keyvaluelist = "";
            String prefix = "";
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                String fp = format(parameter.getValue());
                keylist += (prefix + parameter.getKey());
                valuelist += (prefix + fp);
                keyvaluelist += (prefix + parameter.getKey() + "=" + fp);
                prefix = ",";
            }
            sql = sql.replace("{$KEYLIST}", keylist);
            sql = sql.replace("{$VALUELIST}", valuelist);
            sql = sql.replace("{$KEYVALUELIST}", keyvaluelist);
        }
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            sql = sql.replace("{" + parameter.getKey() + "}", format(parameter.getValue()));
        }
        LogBuilder.writeExitingLog("nbpcglibrary.data", "DBDataService", "buildsql", sql);
        return sql;
    }

    private String buildsql(String sql, Object parameter) {
        sql = sql.replace("{P}", format(parameter));
        LogBuilder.writeExitingLog("nbpcglibrary.data", "DBDataService", "buildsql", sql);
        return sql;
    }

    /**
     * Format a data value for insertion into an SQL string.
     *
     * Handles quoting, escaping and specific formated keyword (eg true false
     * etc)
     *
     * @param value the data value
     * @return the string formated data value
     */
    protected abstract String format(Object value);
}
