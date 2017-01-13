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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterLog;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityFields;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.api.EntityPersistenceProvider;
import uk.theretiredprogrammer.nbpcglibrary.api.PersistenceUnitProvider;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;

/**
 * EntityPersistenceProvider Class for access localSQL databases
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <K> the Primary Key Class
 */
@RegisterLog("nbpcglib.localdatabaseaccess")
public abstract class LocalSQLEntityPersistenceProvider<K> implements EntityPersistenceProvider<K> {

    private String tablename;
    private LocalSQLPersistenceUnitProvider persistenceUnitProvider;
    private String idx;

    @Override
    public void init(String tablename, Properties properties, PersistenceUnitProvider pup) {
        this.tablename = tablename;
        this.persistenceUnitProvider = (LocalSQLPersistenceUnitProvider) pup;
        this.idx = null;
    }

    @Override
    public void init(String tablename, String idx, Properties properties, PersistenceUnitProvider pup) {
        this.tablename = tablename;
        this.persistenceUnitProvider = (LocalSQLPersistenceUnitProvider) pup;
        this.idx = idx;
    }
    
    @Override
    public void close(){
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, LogBuilder.instanceDescription(persistenceUnitProvider) + "-" + tablename);
    }

    @Override
    public EntityFields get(K pkey) {
        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "get", pkey);
        try {
            List<EntityFields> response = persistenceUnitProvider.query(buildsql("SELECT * from " + tablename + " WHERE id={P}", pkey));
            if (response.size() != 1) {
                throw new LogicException("Single row expected");
            }
            return response.get(0);
        } catch (SQLException ex) {
            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "get", pkey);
            throw new LogicException(ex.getMessage());
        }
    }

    @Override
    public final List<EntityFields> get() {
        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "get");
        String sql = idx == null
                ? "SELECT * from " + tablename
                : "SELECT * from " + tablename + " ORDER BY " + idx;
        try {
            return persistenceUnitProvider.query(sql);
        } catch (SQLException ex) {
            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "get");
            throw new LogicException(ex.getMessage());
        }
    }

    @Override
    public final List<K> find() {
        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "find");
        String sql = idx == null
                ? "SELECT id from " + tablename
                : "SELECT id from " + tablename + " ORDER BY " + idx;
        List<K> result = new ArrayList<>();
        try {
            persistenceUnitProvider.query(sql).stream().forEach((ef) -> {
                result.add((K) ef.get("id"));
            });
        } catch (SQLException ex) {
            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "find");
            throw new LogicException(ex.getMessage());
        }
        return result;
    }

    @Override
    public final List<EntityFields> get(String parametername, Object parametervalue) {
        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "get", parametername, parametervalue);
        String sql = idx == null
                ? buildsql("SELECT * from " + tablename + " where " + parametername + "={P}", parametervalue)
                : buildsql("SELECT * from " + tablename + " where " + parametername + "={P} ORDER BY " + idx, parametervalue);
        try {
            return persistenceUnitProvider.query(sql);
        } catch (SQLException ex) {
            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "get", parametername, parametervalue);
            throw new LogicException(ex.getMessage());
        }
    }

    @Override
    public final List<K> find(String parametername, Object parametervalue) {
        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "find", parametername, parametervalue);
        String sql = idx == null
                ? buildsql("SELECT id from " + tablename + " where " + parametername + "={P}", parametervalue)
                : buildsql("SELECT id from " + tablename + " where " + parametername + "={P} ORDER BY " + idx, parametervalue);
        List<K> result = new ArrayList<>();
        try {
            persistenceUnitProvider.query(sql).stream().forEach((ef) -> {
                result.add((K) ef.get("id"));
            });
        } catch (SQLException ex) {
            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "find", parametername, parametervalue);
            throw new LogicException(ex.getMessage());
        }
        return result;
    }

    @Override
    public final EntityFields getOne(String parametername, Object parametervalue) {
        try {
            List<EntityFields> get = persistenceUnitProvider.query(buildsql("SELECT * from " + tablename + " where " + parametername + "={P}", parametervalue));
            if (get.size() != 1) {
                throw new LogicException("Single row expected");
            }
            return get.get(0);
        } catch (SQLException ex) {
            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "getOne", parametername, parametervalue);
            throw new LogicException(ex.getMessage());
        }
    }

    @Override
    public final K findOne(String parametername, Object parametervalue) {
        try {
            List<EntityFields> find = persistenceUnitProvider.query(buildsql("SELECT id from " + tablename + " where " + parametername + "={P}", parametervalue));
            if (find.size() != 1) {
                throw new LogicException("Single row expected");
            }
            return (K) find.get(0).get("id");
        } catch (SQLException ex) {
            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "findOne", parametername, parametervalue);
            throw new LogicException(ex.getMessage());
        }
    }

    @Override
    public final int findNextIdx() {
        try {
            if (idx == null) {
                throw new LogicException("findNextIdx() should not be called if the entity is not ordered");
            }
            List<EntityFields> findidx = persistenceUnitProvider.query("SELECT max(" + idx + ")+1 as nextidx from " + tablename);
            if (findidx.size() != 1) {
                throw new LogicException("Single row expected");
            }
            EntityFields idxrec = findidx.get(0);
            return ((Long) idxrec.get("nextidx")).intValue();
        } catch (SQLException ex) {
            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "findNextIdx");
            throw new LogicException(ex.getMessage());
        }
    }

    @Override
    public final EntityFields insert(EntityFields values) {
        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "insert", values);
        addTimestampInfo(values);
        try {
            persistenceUnitProvider.execute(buildsql("INSERT INTO " + tablename + " ({$KEYLIST}) VALUES ({$VALUELIST})", values));
            List<EntityFields> findpkey = persistenceUnitProvider.query("SELECT LAST_INSERT_ID() as id");
            if (findpkey.size() != 1) {
                throw new LogicException("Single row expected");
            }
            EntityFields pkeyrec = findpkey.get(0);
            K pkey = (K) pkeyrec.get("id");
            List<EntityFields> updated = persistenceUnitProvider.query(buildsql("SELECT * FROM " + tablename + " WHERE id = {P}", pkey));
            if (updated.size() != 1) {
                throw new LogicException("Single row expected");
            }
            LogBuilder.writeExitingLog("nbpcglib.localdatabaseaccess", this, "insert", updated);
            return updated.get(0);
        } catch (SQLException ex) {
            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "insert", values);
            throw new LogicException(ex.getMessage());
        }
    }

    @Override
    public final EntityFields update(K pkey, EntityFields diff) {
        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "update", pkey, diff);
        updateTimestampInfo(diff);
        try {
            persistenceUnitProvider.execute(buildsql(buildsql("UPDATE " + tablename + " SET {$KEYVALUELIST} WHERE id= {P}", pkey), diff));
            List<EntityFields> updated = persistenceUnitProvider.query(buildsql("SELECT * FROM " + tablename + " WHERE id = {P}", pkey));
            if (updated.size() != 1) {
                throw new LogicException("Single row expected");
            }
            LogBuilder.writeExitingLog("nbpcglib.localdatabaseaccess", this, "update", updated);
            return updated.get(0);
        } catch (SQLException ex) {
            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "update", pkey, diff);
            throw new LogicException(ex.getMessage());
        }
    }

    @Override
    public final void delete(K pkey) {
        LogBuilder.writeLog("nbpcglib.localdatabaseaccess", this, "delete", pkey);
        try {
            persistenceUnitProvider.execute(buildsql("DELETE from " + tablename + " WHERE id = {P}", pkey));
        } catch (SQLException ex) {
            LogBuilder.writeExceptionLog("nbpcglib.localdatabaseaccess", ex, this, "delete", pkey);
            throw new LogicException(ex.getMessage());
        }
    }

    private String buildsql(String sql, EntityFields parameters) {
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
        LogBuilder.writeExitingLog("nbpcglib.localdatabaseaccess", this, "buildsql", sql);
        return sql;
    }

    private String buildsql(String sql, Object parameter) {
        sql = sql.replace("{P}", format(parameter));
        LogBuilder.writeExitingLog("nbpcglib.localdatabaseaccess", this, "buildsql", sql);
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
