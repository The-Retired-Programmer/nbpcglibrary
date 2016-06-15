--
-- Copyright (C) 2016 Richard Linsdale (richard.linsdale at blueyonder.co.uk)
--
-- This library is free software; you can redistribute it and/or
-- modify it under the terms of the GNU Lesser General Public
-- License as published by the Free Software Foundation; either
-- version 2.1 of the License, or (at your option) any later version.
--
-- This library is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
-- Lesser General Public License for more details.
--
-- You should have received a copy of the GNU Lesser General Public
-- License along with this library; if not, write to the Free Software
-- Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
-- MA 02110-1301  USA
--
--
-- CREATE TABLES & INSERTS
--
-- @package generated-scripts
-- @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
--
DROP DATABASE IF EXISTS testingmysqllibrary;
CREATE DATABASE testingmysqllibrary CHARACTER SET utf8 COLLATE utf8_bin;
USE testingmysqllibrary;
--
CREATE TABLE TestTable(
    application  VARCHAR(100) UNIQUE NOT NULL
    ,id MEDIUMINT UNSIGNED AUTO_INCREMENT PRIMARY KEY
    ,createdby CHAR(4) NOT NULL
    ,createdon CHAR(14) NOT NULL
    ,updatedby CHAR(4) NOT NULL
    ,updatedon CHAR(14) NOT NULL
) ENGINE = INNODB;

INSERT INTO TestTable (
    application
    ,createdby
    ,createdon
    ,updatedby
    ,updatedon
)
VALUES (
    'app1'
    ,'test'
    ,'20000101000000'
    ,'test'
    ,'20000101000000'
);

INSERT INTO TestTable (
    application
    ,createdby
    ,createdon
    ,updatedby
    ,updatedon
)
VALUES (
    'app2'
    ,'test'
    ,'20000101000000'
    ,'test'
    ,'20000101000000'
);

INSERT INTO TestTable (
    application
    ,createdby
    ,createdon
    ,updatedby
    ,updatedon
)
VALUES (
    'app3'
    ,'test'
    ,'20000101000000'
    ,'test'
    ,'20000101000000'
);
