/*
 * Copyright 2015-2017 Richard Linsdale.
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
package uk.theretiredprogrammer.nbpcglibrary.mysql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterLog;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;
import uk.theretiredprogrammer.nbpcglibrary.localdatabaseaccess.LocalSQLPersistenceUnitProvider;

/**
 * The implementation of the DB class for MySql Database connections.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@RegisterLog("nbpcglibrary.mysql")
public class LocalMySQLPersistenceUnitProvider extends LocalSQLPersistenceUnitProvider {
    
    /**
     * Constructor
     * 
     * @param p the db connection parameters
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public LocalMySQLPersistenceUnitProvider(Properties p) {
        super("local-mysql-"+p.getProperty("key"));
        try {
            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection(p.getProperty("connection"), p.getProperty("user"), p.getProperty("password")));
            setOperational();
        } catch (ClassNotFoundException | SQLException ex) {
             LogBuilder.create("nbpcglibrary.mysql", Level.SEVERE).addConstructorName(this, p)
                .addExceptionMessage(ex).write();
        }
    }
    
    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, getName());
    }
}
