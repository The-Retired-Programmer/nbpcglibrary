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
package uk.org.rlinsdale.nbpcglibrary.common;

/**
 * A Text formatter - substitutes values into string.
 * like String.format - so should be replaced 
 * @deprecated 
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class Formatter {

    /**
     * Format a string using the pattern and a set in insert values to be used in subsitutions.
     * 
     * @param pattern the pattern string defining text and insert points
     * @param inserts the set of insert strings
     * @return the formatted string
     */
    public String format(String pattern, String[] inserts) {
        StringBuilder sb = new StringBuilder();
        int p = 0;
        while (p != -1) {
            int x = pattern.indexOf('{', p);
            if (x == -1) {
                sb.append(pattern.substring(p));
                p = x;
            } else {
                if (x > p) {
                    sb.append(pattern.substring(p, x));
                }
                int q = pattern.indexOf("}",x);
                int i = Integer.parseInt(pattern.substring(x+1,q));
                sb.append(inserts[i]);
                p = q+1;
            }
        }
        return sb.toString();
    }
}
