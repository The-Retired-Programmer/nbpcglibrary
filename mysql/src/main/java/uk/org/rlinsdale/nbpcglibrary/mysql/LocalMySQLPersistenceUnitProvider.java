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
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterLog;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.localdatabaseaccess.LocalSQLPersistenceUnitProvider;

/**
 * The implementation of the DB class for MySql Database connections.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
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
