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
package uk.theretiredprogrammer.nbpcglibrary.dbrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import static java.sql.Types.BIGINT;
import static java.sql.Types.BIT;
import static java.sql.Types.BOOLEAN;
import static java.sql.Types.CHAR;
import static java.sql.Types.DECIMAL;
import static java.sql.Types.TINYINT;
import static java.sql.Types.VARCHAR;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;
import uk.theretiredprogrammer.nbpcglibrary.api.Rest;
import uk.theretiredprogrammer.nbpcglibrary.api.Settings;
import uk.theretiredprogrammer.nbpcglibrary.api.IdTimestamp;

/**
 * DBRest Class for access  to localSQL databases
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the Entity class being processed
 */
public class DBRest<E extends IdTimestamp> implements Rest<E> {

    private String tablename;
    private String ordercolumn;
    private Class<E> responseEntityClass;
    private Connection conn;
    private final String dbname;

    public DBRest(String dbname, String tablename, Class<E> responseEntityClass) {
        this(dbname, tablename, null, responseEntityClass);
    }

    public DBRest(String dbname, String tablename, String ordercolumn, Class<E> responseEntityClass) {
        this.responseEntityClass = responseEntityClass;
        this.dbname = dbname;
        this.tablename = tablename;
        this.ordercolumn = ordercolumn;
    }

    @Override
    public boolean open() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, "nbplatform", "netbeans");
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            return false;
        }
    }

    /**
     * Disconnect from the current database connection.
     *
     * @return true if disconnection OK - false if SQlExcetion during close
     */
    @Override
    public boolean close() {
        try {
            conn.close();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public E get(int id) {
        try {
            List<E> response = query(buildsql("SELECT * from " + tablename + " WHERE id={P}", id));
            if (response.size() != 1) {
                return null;
            }
            return response.get(0);
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public final List<E> getAll() {
        String sql = ordercolumn == null
                ? "SELECT * from " + tablename
                : "SELECT * from " + tablename + " ORDER BY " + ordercolumn;
        try {
            return query(sql);
        } catch (SQLException ex) {
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<E> getMany(String filtername, int filtervalue) {
        String sql = ordercolumn == null
                ? buildsql("SELECT * from " + tablename + " where " + filtername + "={P}", filtervalue)
                : buildsql("SELECT * from " + tablename + " where " + filtername + "={P} ORDER BY " + ordercolumn, filtervalue);
        try {
            return query(sql);
        } catch (SQLException ex) {
            return new ArrayList<>();
        }
    }

//    @Override
//    public final List<K> find() {
//        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "find");
//        String sql = ordercolumn == null
//                ? "SELECT id from " + tablename
//                : "SELECT id from " + tablename + " ORDER BY " + ordercolumn;
//        List<K> result = new ArrayList<>();
//        try {
//            persistenceUnitProvider.query(sql).stream().forEach((ef) -> {
//                result.add((K) ef.get("id"));
//            });
//        } catch (SQLException ex) {
//            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "find");
//            throw new LogicException(ex.getMessage());
//        }
//        return result;
//    }
//    @Override
//    public final List<EntityFields> get(String parametername, Object parametervalue) {
//        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "get", parametername, parametervalue);
//        String sql = ordercolumn == null
//                ? buildsql("SELECT * from " + tablename + " where " + parametername + "={P}", parametervalue)
//                : buildsql("SELECT * from " + tablename + " where " + parametername + "={P} ORDER BY " + ordercolumn, parametervalue);
//        try {
//            return persistenceUnitProvider.query(sql);
//        } catch (SQLException ex) {
//            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "get", parametername, parametervalue);
//            throw new LogicException(ex.getMessage());
//        }
//    }
//    @Override
//    public final List<K> find(String parametername, Object parametervalue) {
//        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "find", parametername, parametervalue);
//        String sql = ordercolumn == null
//                ? buildsql("SELECT id from " + tablename + " where " + parametername + "={P}", parametervalue)
//                : buildsql("SELECT id from " + tablename + " where " + parametername + "={P} ORDER BY " + ordercolumn, parametervalue);
//        List<K> result = new ArrayList<>();
//        try {
//            persistenceUnitProvider.query(sql).stream().forEach((ef) -> {
//                result.add((K) ef.get("id"));
//            });
//        } catch (SQLException ex) {
//            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "find", parametername, parametervalue);
//            throw new LogicException(ex.getMessage());
//        }
//        return result;
//    }
//    @Override
//    public final EntityFields getOne(String parametername, Object parametervalue) {
//        try {
//            List<EntityFields> get = persistenceUnitProvider.query(buildsql("SELECT * from " + tablename + " where " + parametername + "={P}", parametervalue));
//            if (get.size() != 1) {
//                throw new LogicException("Single row expected");
//            }
//            return get.get(0);
//        } catch (SQLException ex) {
//            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "getOne", parametername, parametervalue);
//            throw new LogicException(ex.getMessage());
//        }
//    }
//    @Override
//    public final K findOne(String parametername, Object parametervalue) {
//        try {
//            List<EntityFields> find = persistenceUnitProvider.query(buildsql("SELECT id from " + tablename + " where " + parametername + "={P}", parametervalue));
//            if (find.size() != 1) {
//                throw new LogicException("Single row expected");
//            }
//            return (K) find.get(0).get("id");
//        } catch (SQLException ex) {
//            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "findOne", parametername, parametervalue);
//            throw new LogicException(ex.getMessage());
//        }
//    }
//    @Override
//    public final int findNextIdx() {
//        try {
//            if (ordercolumn == null) {
//                throw new LogicException("findNextIdx() should not be called if the entity is not ordered");
//            }
//            List<EntityFields> findidx = persistenceUnitProvider.query("SELECT max(" + ordercolumn + ")+1 as nextidx from " + tablename);
//            if (findidx.size() != 1) {
//                throw new LogicException("Single row expected");
//            }
//            EntityFields idxrec = findidx.get(0);
//            return ((Long) idxrec.get("nextidx")).intValue();
//        } catch (SQLException ex) {
//            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "findNextIdx");
//            throw new LogicException(ex.getMessage());
//        }
//    }
    @Override
    public final E create(E entity) {
        addTimestampInfo(entity);
        try {
            execute(buildsql("INSERT INTO " + tablename + " ({$KEYLIST}) VALUES ({$VALUELIST})", entity));
            List<E> findpkey = query("SELECT LAST_INSERT_ID() as id");
            if (findpkey.size() != 1) {
                throw new LogicException("Single row expected");
            }
            E pkeyrec = findpkey.get(0);
            int id = pkeyrec.getId();
            List<E> updated = query(buildsql("SELECT * FROM " + tablename + " WHERE id = {P}", id));
            if (updated.size() != 1) {
                throw new LogicException("Single row expected");
            }
            return updated.get(0);
        } catch (SQLException ex) {
            throw new LogicException(ex.getMessage());
        }
    }

    private void addTimestampInfo(E entity) {
        String user = Settings.get("Usercode", "????");
        SimpleDateFormat datetime_ISO8601 = new SimpleDateFormat("yyyyMMddHHmmss");
        datetime_ISO8601.setLenient(false);
        String when = datetime_ISO8601.format(new Date());
        entity.setCreatedby(user);
        entity.setCreatedon(when);
        entity.setUpdatedby(user);
        entity.setUpdatedon(when);
    }

    @Override
    public final E update(int id, E entity) {
        updateTimestampInfo(entity);
        try {
            execute(buildsql(buildsql("UPDATE " + tablename + " SET {$KEYVALUELIST} WHERE id= {P}", id), entity));
            List<E> updated = query(buildsql("SELECT * FROM " + tablename + " WHERE id = {P}", id));
            if (updated.size() != 1) {
                throw new LogicException("Single row expected");
            }
            return updated.get(0);
        } catch (SQLException ex) {
            return null;
        }
    }

    private void updateTimestampInfo(E entity) {
        String user = Settings.get("Usercode", "user");
        SimpleDateFormat datetime_ISO8601 = new SimpleDateFormat("yyyyMMddHHmmss");
        datetime_ISO8601.setLenient(false);
        String when = datetime_ISO8601.format(new Date());
        entity.setUpdatedby(user);
        entity.setUpdatedon(when);
    }
    
    @Override
    public E patch(int id, Map<String, Object> updates) {
        // temporary until javaee8
        throw new RuntimeException("Patch not implemented over DB Rest");
    }

    @Override
    public final boolean delete(int id) {
        try {
            execute(buildsql("DELETE from " + tablename + " WHERE id = {P}", id));
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    private String buildsql(String sql, E entity) {
        if (sql.contains("{$KEYLIST}") || sql.contains("{$VALUELIST}") || sql.contains("{$KEYVALUELIST}")) {
            // special code to generate and substitute keylists and valuelists
            String jsonentity = toJson(entity);
            StringReader sr = new StringReader(jsonentity);
            JsonReader jr = Json.createReader(sr);
            JsonObject jo = jr.readObject();
            String keylist = jo.entrySet().stream()
                    .map((entry) -> entry.getKey())
                    .collect(Collectors.joining(", "));
            String valuelist = jo.entrySet().stream()
                    .map((entry) -> entry.getValue().toString())
                    .collect(Collectors.joining(", "));
            String keyvaluelist = jo.entrySet().stream()
                    .map((entry) -> entry.getKey() + " = " + entry.getValue().toString())
                    .collect(Collectors.joining(", "));
            sql = sql.replace("{$KEYLIST}", keylist);
            sql = sql.replace("{$VALUELIST}", valuelist);
            sql = sql.replace("{$KEYVALUELIST}", keyvaluelist);

            for (Entry<String, JsonValue> entry : jo.entrySet()) {
                sql = sql.replace("{" + entry.getKey() + "}", entry.getValue().toString());
            }
        }
        return sql;
    }

    private String toJson(E entity) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //Object to JSON in String
            return mapper.writeValueAsString(entity);
        } catch (IOException ex) {
            return null;
        }
    }

    private String buildsql(String sql, Object parameter) {
        sql = sql.replace("{P}", format(parameter));
        return sql;
    }
    
    private String buildsql(String sql, int parameter) {
        sql = sql.replace("{P}", Integer.toString(parameter));
        return sql;
    }

    /**
     * Execute an Insert, Update or Delete query on the database.
     *
     * @param sql the SQL statement
     * @return the number of records changed due to the query
     * @throws SQLException if problems
     */
    private synchronized int execute(String sql) throws SQLException {
        int res;
        try (Statement stat = conn.createStatement()) {
            res = stat.executeUpdate(sql);
        }
        return res;
    }

    /**
     * Execute a query and return the columns returned as a set of EntityFields
     *
     * @param sql the SQL statement to be executed
     * @return a list of EntityFields
     * @throws SQLException if problems
     */
    private synchronized List<E> query(String sql) throws SQLException {
        List<E> el = new ArrayList<>();
        try (Statement stat = conn.createStatement()) {
            ResultSet rs = stat.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                el.add(createEntity(rs, rsmd));
            }
        }
        return el;
    }

    private E createEntity(ResultSet rs, ResultSetMetaData meta) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //JSON from String to Object
            return mapper.readValue(createJsonDBRecord(rs, meta), responseEntityClass);
        } catch (SQLException | IOException ex) {
            return null;
        }
    }

    private String createJsonDBRecord(ResultSet rs, ResultSetMetaData meta) throws SQLException {
        JsonObjectBuilder job = Json.createObjectBuilder();
        int colcount = meta.getColumnCount();
        for (int i = 1; i <= colcount; i++) {
            switch (meta.getColumnType(i)) {
                case CHAR:
                case VARCHAR:
                    job.add(meta.getColumnName(i), rs.getString(i));
                    break;
                case BOOLEAN:
                case TINYINT:
                case BIT:
                    job.add(meta.getColumnName(i), rs.getBoolean(i));
                    break;
                case BIGINT:
                    job.add(meta.getColumnName(i), rs.getLong(i));
                    break;
                case DECIMAL:
                    job.add(meta.getColumnName(i), rs.getBigDecimal(i));
                    break;
                default:
                    int val = rs.getInt(i);
                    if (rs.wasNull()) {
                        job.addNull(meta.getColumnName(i));
                    } else {
                        job.add(meta.getColumnName(i), val);
                    }
            }
        }
        StringWriter sw = new StringWriter();
        JsonWriter jw = Json.createWriter(sw);
        jw.writeObject(job.build());
        return sw.toString();
    }

    /**
     * Format a java data value for insertion into an SQL string.
     *
     * Handles quoting, escaping and specific formated keyword (eg true false
     * etc)
     *
     * @param value the data value
     * @return the string formated data value
     */
    private String format(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof String) {
            return "'" + ((String) value).replace("\\", "\\\\").replace("'", "\\'").replace("\n", "\\n") + "'";
        }
        if (value instanceof Boolean) {
            return ((Boolean) value).toString();
        }
        if (value instanceof Integer) {
            return ((Integer) value).toString();
        }
        if (value instanceof Long) {
            return ((Long) value).toString();
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }
        return "NULL";
    }
}
