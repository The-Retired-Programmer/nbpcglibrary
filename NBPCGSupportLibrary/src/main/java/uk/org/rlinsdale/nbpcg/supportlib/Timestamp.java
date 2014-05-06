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
package uk.org.rlinsdale.nbpcg.supportlib;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple Timestamp Class
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class Timestamp {

    private final static DateFormat sqlformat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static DateFormat userformat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    private Date date;

    /**
     * Constructor
     */
    public Timestamp() {
        date = new Date();
    }

    /**
     * Constructor
     *
     * @param datestring the initial value of the timestamp (formatted in
     * display format ("dd-MMM-yyyy-HH:mm:ss"))
     * @throws linsdale.nbpcg.supportlib.BadFormatException when datestring is badly formatted
     */
    public Timestamp(String datestring) throws BadFormatException {
        userformat.setLenient(true);
        try {
            date = userformat.parse(datestring);
        } catch (ParseException ex) {
            throw new BadFormatException("Timestamp:constructor - Illegal datestring format");
        }
    }

    /**
     * Set the date using an SQL formatted datestring ("yyyyMMddHHmmss")
     *
     * @param sqlstring the value (formatted in SQL format)
     */
    public void setDateUsingSQLString(String sqlstring) {
        sqlformat.setLenient(false);
        try {
            date = sqlformat.parse(sqlstring);
        } catch (ParseException ex) {
            throw new LogicException("Timestamp:setDateUsinSQLString() - Illegal sqlstring format \"" + sqlstring + "\"");
        }
    }

    /**
     * Get the SQl formated date ("yyyyMMddHHmmss")
     *
     * @return formatted timestamp
     */
    public String toSQLString() {
        return sqlformat.format(date);
    }

    /**
     * Get the display formatted date ("dd-MMM-yyyy-HH:mm:ss")
     *
     * @return formatted timestamp
     */
    @Override
    public String toString() {
        return userformat.format(date);
    }

    /**
     * Compare this timestamp to the current time.
     * 
     * @return +1 / 0 / -1 as a result of the compare
     */
    public int compareTo() {
        return compareTo(new Timestamp());
    }

    /**
     * Compare
     *
     * returns 1 if this date is greater that the target date; 0 if they are the
     * same and -1 if less that the target date
     * 
     * @param target the target Timestamp
     * @return +1 / 0 / -1 as a result of the compare
     */
    public int compareTo(Timestamp target) {
        return toSQLString().compareTo(target.toSQLString());
    }
}
