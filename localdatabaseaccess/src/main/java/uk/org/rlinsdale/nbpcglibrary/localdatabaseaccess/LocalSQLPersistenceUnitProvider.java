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
package uk.org.rlinsdale.nbpcglibrary.localdatabaseaccess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.sql.Types.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import static javax.json.JsonValue.ValueType.STRING;
import uk.org.rlinsdale.nbpcglibrary.api.PersistenceUnitProvider;
import uk.org.rlinsdale.nbpcglibrary.common.Event;
import uk.org.rlinsdale.nbpcglibrary.common.Listener;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.common.LogicException;
import uk.org.rlinsdale.nbpcglibrary.json.JsonConversionException;
import static uk.org.rlinsdale.nbpcglibrary.localdatabaseaccess.TransactionEventParams.TransactionRequest.BEGIN;
import static uk.org.rlinsdale.nbpcglibrary.localdatabaseaccess.TransactionEventParams.TransactionRequest.COMMIT;
import static uk.org.rlinsdale.nbpcglibrary.localdatabaseaccess.TransactionEventParams.TransactionRequest.ROLLBACK;

/**
 * Abstract Class implementing Local Database access using JDBC.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public abstract class LocalSQLPersistenceUnitProvider implements PersistenceUnitProvider {

    private Connection conn;
    private final Event<TransactionEventParams> transactionEvent;
    private boolean inTransaction = false;
    private DatabaseMetaData dbMetaData;

    /**
     * Constructor.
     *
     * @param name the dataservice name
     */
    public LocalSQLPersistenceUnitProvider(String name) {
        transactionEvent = new Event<>("transactions:" + name);
    }

    /**
     * Set the JDBC connection to be used for this PersistenceUnitProvider.
     *
     * @param conn the JDBC connection
     * @throws SQLException
     */
    protected final void setConnection(Connection conn) throws SQLException {
        this.conn = conn;
        dbMetaData = conn.getMetaData();
    }

    /**
     * Add a listener for Transaction events (Begin, Commit and Rollback). The
     * listener will be called on the EventQueue.
     *
     * @param l the listener
     */
    public void addListener(Listener<TransactionEventParams> l) {
        transactionEvent.addListener(l);
    }

    /**
     * Remove a listener for Transaction events.
     *
     * @param l the listener
     */
    public void removeListener(Listener<TransactionEventParams> l) {
        transactionEvent.removeListener(l);
    }

    /**
     * Add a listener for Transaction events (Begin, Commit and Rollback).
     *
     * @param l the listener
     * @param mode the listener mode (fire on event queue or immediately;
     * priority)
     */
    public void addListener(Listener<TransactionEventParams> l, Event.ListenerMode mode) {
        transactionEvent.addListener(l, mode);
    }

    /**
     * Mark the start of a Transaction unit.
     */
    public void begin() {
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "begin");
        if (inTransaction) {
            throw new LogicException("begin() failed - already in transaction");
        } else {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException ex) {
                LogBuilder.create("nbpcglib.localSQLPersistenceUnitProvider", Level.SEVERE).addMethodName(this, "begin")
                        .addExceptionMessage(ex).write();
            }
            inTransaction = true;
            transactionEvent.fire(new TransactionEventParams(BEGIN));
        }
    }

    /**
     * Mark the end of a transaction unit, commit all changes.
     */
    public void commit() {
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "commit");
        if (inTransaction) {
            try {
                conn.commit();
            } catch (SQLException ex) {
                LogBuilder.create("nbpcglib.localSQLPersistenceUnitProvider", Level.SEVERE).addMethodName(this, "commit")
                        .addExceptionMessage(ex).write();
            }
            transactionEvent.fire(new TransactionEventParams(COMMIT));
            inTransaction = false;
        } else {
            throw new LogicException("commit() failed - not in transaction");
        }
    }

    /**
     * Mark the end of a transaction unit, rollback all changes.
     */
    public void rollback() {
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "rollback");
        if (inTransaction) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LogBuilder.create("nbpcglib.localSQLPersistenceUnitProvider", Level.SEVERE).addMethodName(this, "rollback")
                        .addExceptionMessage(ex).write();
            }
            transactionEvent.fire(new TransactionEventParams(ROLLBACK));
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
            LogBuilder.create("nbpcglib.localSQLPersistenceUnitProvider", Level.SEVERE).addMethodName(this, "disconnect")
                    .addExceptionMessage(ex).write();
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
    public synchronized JsonArray query(String sql, JsonValue parameter) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        try {
            try (Statement stat = conn.createStatement()) {
                ResultSet rs = stat.executeQuery(buildsql(sql, parameter));
                while (rs.next()) {
                    jab.add(rs.getInt("id"));
                }
            }
        } catch (SQLException | IOException ex) {
            LogBuilder.create("nbpcglib.localSQLPersistenceUnitProvider", Level.SEVERE).addMethodName(this, "query", sql, parameter)
                    .addExceptionMessage(ex).write();
        }
        return jab.build();
    }

    /**
     * Execute a Select query on the database to extract the list of Entity ids.
     *
     * @param sql the SQL for the query
     * @return list of ids
     */
    public synchronized JsonArray query(String sql) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "query", sql);
        try {
            try (Statement stat = conn.createStatement()) {
                ResultSet rs = stat.executeQuery(sql);
                while (rs.next()) {
                    jab.add(rs.getInt("id"));
                }
            }
        } catch (SQLException ex) {
            LogBuilder.create("nbpcglib.localSQLPersistenceUnitProvider", Level.SEVERE).addMethodName(this, "query", sql)
                    .addExceptionMessage(ex).write();
        }
        return jab.build();
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
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "simpleIntQuery", sql, columnname);
        try {
            try (Statement stat = conn.createStatement()) {
                ResultSet rs = stat.executeQuery(sql);
                rs.first();
                res = rs.getInt(columnname);
            }
        } catch (SQLException ex) {
            LogBuilder.create("nbpcglib.localSQLPersistenceUnitProvider", Level.SEVERE).addMethodName(this, "simpleIntQuery", sql, columnname)
                    .addExceptionMessage(ex).write();
        }
        return res;
    }

    /**
     * Execute a select query and insert the values from the first row returned
     * into an JsonObject.
     *
     * @param tablename
     * @param sql the SQL for the query
     * @return the returned database record (in Json format)
     */
    public synchronized JsonObject simpleQuery(String tablename, String sql) {
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "simpleQuery", sql);
        JsonObjectBuilder job = Json.createObjectBuilder();
        try {
            try (Statement stat = conn.createStatement()) {
                ResultSet rs = stat.executeQuery(sql);
                if (!rs.first()) {
                    throw new LogicException("DBDataService.simpleQuery resulted in an empty ResultSet");
                }
                buildJsonRecord(tablename, rs, job);
            }
        } catch (SQLException | JsonConversionException ex) {
            LogBuilder.create("nbpcglib.localSQLPersistenceUnitProvider", Level.SEVERE).addMethodName(this, "simpleQuery", sql)
                    .addExceptionMessage(ex).write();
        }
        return job.build();
    }

    /**
     * Execute a select query and insert the values from the each row returned
     * into a JsonArray of JsonObjects.
     *
     * @param tablename
     * @param sql the SQL for the query
     * @return the returned database record (in Json format)
     */
    public synchronized JsonArray multiQuery(String tablename, String sql) {
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "simpleQuery", sql);
        JsonArrayBuilder jab = Json.createArrayBuilder();
        try {
            try (Statement stat = conn.createStatement()) {
                ResultSet rs = stat.executeQuery(sql);
                while (rs.next()) {
                    JsonObjectBuilder job = Json.createObjectBuilder();
                    buildJsonRecord(tablename, rs, job);
                    jab.add(job.build());
                }
            }
        } catch (SQLException | JsonConversionException ex) {
            LogBuilder.create("nbpcglib.localSQLPersistenceUnitProvider", Level.SEVERE).addMethodName(this, "multiQuery", sql)
                    .addExceptionMessage(ex).write();
        }
        return jab.build();
    }

    /**
     * Execute a select query and insert the values from the each row returned
     * into a JsonArray of JsonObjects.
     *
     * @param tablename
     * @param sql the SQL for the query
     * @param parameter the single parameter used in the query
     * @return the returned database record (in Json format)
     */
    public synchronized JsonArray multiQuery(String tablename, String sql, JsonValue parameter) {
        LogBuilder.writeLog("nbpcglib.localSQLPersistenceUnitProvider", this, "simpleQuery", sql);
        JsonArrayBuilder jab = Json.createArrayBuilder();
        try {
            try (Statement stat = conn.createStatement()) {
                ResultSet rs = stat.executeQuery(buildsql(sql, parameter));
                while (rs.next()) {
                    JsonObjectBuilder job = Json.createObjectBuilder();
                    buildJsonRecord(tablename, rs, job);
                    jab.add(job.build());
                }
            }
        } catch (SQLException | IOException ex) {
            LogBuilder.create("nbpcglib.localSQLPersistenceUnitProvider", Level.SEVERE).addMethodName(this, "multiQuery", sql)
                    .addExceptionMessage(ex).write();
        }
        return jab.build();
    }

    private final Map<String, Map<String, ValueType>> tableColumnInfo = new HashMap<>();

    private Map<String, ValueType> getColumnMetaData(String tablename) throws SQLException {
        Map<String, ValueType> columninfo = tableColumnInfo.get(tablename);
        if (columninfo == null) {
            columninfo = extractColumnMetaData(tablename);
            tableColumnInfo.put(tablename, columninfo);
        }
        return columninfo;
    }

    private Map<String, ValueType> extractColumnMetaData(String tablename) throws SQLException {
        Map<String, ValueType> columninfo = new HashMap<>();
        ResultSet result = dbMetaData.getColumns("", "", tablename, null);
        while (result.next()) {
            columninfo.put(result.getString(4), typeTranslate(result.getInt(5)));
        }
        return columninfo;
    }

    private ValueType typeTranslate(int sqltype) {
        switch (sqltype) {
            case CHAR:
            case VARCHAR:
                return ValueType.STRING;
            case BOOLEAN:
            case TINYINT:
            case BIT:
                return ValueType.TRUE;
            default:
                return ValueType.NUMBER;
        }
    }

    private void buildJsonRecord(String tablename, ResultSet rs, JsonObjectBuilder job) throws SQLException, JsonConversionException {
        Map<String, ValueType> columninfo = getColumnMetaData(tablename);
        for (Entry<String, ValueType> ks : columninfo.entrySet()) {
            String columnname = ks.getKey();
            switch (ks.getValue()) {
                case STRING:
                    job.add(columnname, rs.getString(columnname));
                    break;
                case TRUE:
                    job.add(columnname, rs.getBoolean(columnname));
                    break;
                case NUMBER:
                    job.add(columnname, rs.getInt(columnname));
                    break;
                default:
                    throw new JsonConversionException();
            }
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
    public synchronized int execute(String sql, JsonObject parameters) {
        int res = 0;
        try {
            try (Statement stat = conn.createStatement()) {
                res = stat.executeUpdate(buildsql(sql, parameters));
            }
        } catch (SQLException | IOException ex) {
            LogBuilder.create("nbpcglib.localSQLPersistenceUnitProvider", Level.SEVERE).addMethodName(this, "execute", sql, parameters)
                    .addExceptionMessage(ex).write();
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
    public synchronized int execute(String sql, JsonValue parameter) {
        int res = 0;
        try {
            try (Statement stat = conn.createStatement()) {
                res = stat.executeUpdate(buildsql(sql, parameter));
            }
        } catch (SQLException | IOException ex) {
            LogBuilder.create("nbpcglib.localSQLPersistenceUnitProvider", Level.SEVERE).addMethodName(this, "execute", sql, parameter)
                    .addExceptionMessage(ex).write();
        }
        return res;
    }

    private String buildsql(String sql, JsonObject parameters) throws IOException {
        if (sql.contains("{$KEYLIST}") || sql.contains("{$VALUELIST}") || sql.contains("{$KEYVALUELIST}")) {
            // special code to generate and substitute keylists and valuelists
            String keylist = "";
            String valuelist = "";
            String keyvaluelist = "";
            String prefix = "";
            for (Map.Entry<String, JsonValue> parameter : parameters.entrySet()) {
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
        for (Map.Entry<String, JsonValue> parameter : parameters.entrySet()) {
            sql = sql.replace("{" + parameter.getKey() + "}", format(parameter.getValue()));
        }
        LogBuilder.writeExitingLog("nbpcglib.localSQLPersistenceUnitProvider", this, "buildsql", sql);
        return sql;
    }

    private String buildsql(String sql, JsonValue parameter) throws IOException {
            sql = sql.replace("{P}", format(parameter));
            LogBuilder.writeExitingLog("nbpcglib.localSQLPersistenceUnitProvider", this, "buildsql", sql);
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
     * @throws IOException
     */
    protected abstract String format(JsonValue value) throws IOException;
}
