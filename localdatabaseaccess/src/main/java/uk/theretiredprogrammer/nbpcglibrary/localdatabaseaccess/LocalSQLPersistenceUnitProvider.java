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
package uk.theretiredprogrammer.nbpcglibrary.localdatabaseaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import static java.sql.Types.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityFields;
import uk.theretiredprogrammer.nbpcglibrary.api.PersistenceUnitProvider;
import uk.theretiredprogrammer.nbpcglibrary.common.Event;
import uk.theretiredprogrammer.nbpcglibrary.common.Listener;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;
import static uk.theretiredprogrammer.nbpcglibrary.localdatabaseaccess.TransactionEventParams.TransactionRequest.BEGIN;
import static uk.theretiredprogrammer.nbpcglibrary.localdatabaseaccess.TransactionEventParams.TransactionRequest.COMMIT;
import static uk.theretiredprogrammer.nbpcglibrary.localdatabaseaccess.TransactionEventParams.TransactionRequest.ROLLBACK;

/**
 * PersistenceUnit Provider for any local SQL database.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public abstract class LocalSQLPersistenceUnitProvider implements PersistenceUnitProvider {

    private Connection conn;
    private final Event<TransactionEventParams> transactionEvent;
    private boolean inTransaction = false;
    private final String name;
    private boolean operational = false;

    /**
     * Constructor.
     *
     * @param name the PersistenceUnitProvider name
     */
    public LocalSQLPersistenceUnitProvider(String name) {
        transactionEvent = new Event<>("transactions:" + name);
        this.name = name;
    }
    
    /**
     * set this PUP as operational
     */
    protected void setOperational() {
        operational = true;
    }
    
    @Override
    public boolean isOperational() {
        return operational;
    }
    
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the database connection being used for this PersistenceUnitProvider.
     *
     * @param conn the JDBC connection
     * @throws SQLException if problems in accessing database
     */
    protected final void setConnection(Connection conn) throws SQLException {
        this.conn = conn;
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
        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "begin");
        if (inTransaction) {
            throw new LogicException("begin() failed - already in transaction");
        } else {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException ex) {
                LogBuilder.create("nbpcglib.localdatabaseaccess", Level.SEVERE).addMethodName(this, "begin")
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
        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "commit");
        if (inTransaction) {
            try {
                conn.commit();
            } catch (SQLException ex) {
                LogBuilder.create("nbpcglib.localdatabaseaccess", Level.SEVERE).addMethodName(this, "commit")
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
        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "rollback");
        if (inTransaction) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LogBuilder.create("nbpcglib.localdatabaseaccess", Level.SEVERE).addMethodName(this, "rollback")
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
            LogBuilder.create("nbpcglib.localdatabaseaccess", Level.SEVERE).addMethodName(this, "disconnect")
                    .addExceptionMessage(ex).write();
        }
    }
    
    /**
     * Execute an Insert, Update or Delete query on the database.
     *
     * @param sql the SQL statement
     * @return the number of records changed due to the query
     * @throws SQLException if problems
     */
    public synchronized int execute(String sql) throws SQLException {
        int res;
        try (Statement stat = conn.createStatement()) {
            res = stat.executeUpdate(sql);
        }
        return res;
    }
    
    /**
     * Execute a query and return the columns returned as a set of EntityFields
     * @param sql the SQL statement to be executed
     * @return a list of EntityFields
     * @throws SQLException if problems
     */
    public synchronized List<EntityFields> query(String sql) throws SQLException {
        List<EntityFields> efs = new ArrayList<>();
        try (Statement stat = conn.createStatement()) {
            ResultSet rs = stat.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                efs.add(createEntityFields(rs,rsmd));
            }
        }
        return efs;
    }
    
    private EntityFields createEntityFields(ResultSet rs, ResultSetMetaData meta) throws SQLException {
        EntityFields ef = new EntityFields();
        int colcount = meta.getColumnCount();
        for (int i = 1; i<= colcount; i++){
            switch (meta.getColumnType(i)) {
                case CHAR:
                case VARCHAR:
                    ef.put(meta.getColumnName(i), rs.getString(i));
                    break;
                case BOOLEAN:
                case TINYINT:
                case BIT:
                    ef.put(meta.getColumnName(i), rs.getBoolean(i));
                    break;
                case BIGINT:
                    ef.put(meta.getColumnName(i), rs.getLong(i));
                    break;
                case DECIMAL:
                    ef.put(meta.getColumnName(i), rs.getBigDecimal(i));
                    break;
                default:
                    int val = rs.getInt(i);
                    if (rs.wasNull()) {
                        ef.put(meta.getColumnName(i), null);
                    } else {
                        ef.put(meta.getColumnName(i), val);
                    }
            }
        }
        return ef;
    }
}
