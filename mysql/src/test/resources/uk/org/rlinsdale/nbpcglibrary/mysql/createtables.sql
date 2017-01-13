--
-- Copyright 2015-2017 Richard Linsdale.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--      http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
-- CREATE TABLES & INSERTS
--
-- @package generated-scripts
-- @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
