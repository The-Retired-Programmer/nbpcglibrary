#
# Copyright (C) 2014-2016 Richard Linsdale (richard at theretiredprogrammer.uk)
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
# MA 02110-1301  USA
#
#
# Backup Script for authentication Database
#
# Script generated by NetBeans Platform Code Generator tools using script.xml. 
# Do not edit this file.  Apply any changes to the definition file and
# regenerate all files.
#
# @package generated-scripts
# @author Richard Linsdale (richard at theretiredprogrammer.uk)
#
mysqldump --user=developer --password=dev --single-transaction authentication2 > backup_authentication2.sql

