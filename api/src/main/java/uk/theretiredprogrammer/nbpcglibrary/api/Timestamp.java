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
package uk.theretiredprogrammer.nbpcglibrary.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple Timestamp Class
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
     * @param date the initial value of the timestamp
     */
    public Timestamp(Date date) {
        this.date = date;
    }

    /**
     * Constructor
     *
     * @param datestring the initial value of the timestamp (formatted in
     * display format ("dd-MMM-yyyy-HH:mm:ss"))
     * @throws BadFormatException when datestring is badly formatted
     */
    public Timestamp(String datestring) throws BadFormatException {
        userformat.setLenient(true);
        try {
            date = userformat.parse(datestring);
        } catch (ParseException ex) {
            throw new BadFormatException("Bad entry format - expected a date and time (dd-MMM-yyyy-HH:mm:ss)");
        }
    }

    /**
     * Set the date using an SQL formatted datestring ("yyyyMMddHHmmss")
     *
     * @param sqlstring the value (formatted in SQL format)
     * @throws BadFormatException when when sqlstring is badly formatted
     */
    public void setDateUsingSQLString(String sqlstring) throws BadFormatException {
        sqlformat.setLenient(false);
        try {
            date = sqlformat.parse(sqlstring);
        } catch (ParseException ex) {
            throw new BadFormatException("Timestamp:setDateUsinSQLString() - Illegal sqlstring format \"" + sqlstring + "\"");
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
