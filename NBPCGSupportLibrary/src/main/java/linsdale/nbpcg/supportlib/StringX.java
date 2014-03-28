/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package linsdale.nbpcg.supportlib;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class StringX {

    public static String padLeft(String s, int requiredLength, char padchar) {
        int l = s.length();
        if (l >= requiredLength) {
            return s;
        }
        StringBuilder sb = new StringBuilder(requiredLength);
        for (int i = 0; i < (requiredLength - l); i++) {
            sb.append(padchar);
        }
        sb.append(s);
        return sb.toString();
    }

    public static String padLeftIfInt(String s, int requiredLength, char padchar) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return s;
        }
        return padLeft(s, requiredLength, padchar);
    }

    public static String[] splitAndTrim(String s, String regex) {
        String[] split = s.split(regex);
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
        }
        return split;
    }

    public static String[] splitAndTrim(String s, String regex, int l) {
        String[] res = new String[l];
        String[] split = splitAndTrim(s, regex);
        String missing = split.length > 0 ? split[0] : "";
        for (int i = 0; i < res.length; i++) {
            res[i] = i < split.length ? split[i] : missing;
        }
        return res;
    }

    public static String[] splitAndTrim(String s, String regex, int l, String missing) {
        String[] res = new String[l];
        if ("".equals(s.trim())) {
            for (int i = 0; i < l; i++) {
                res[i] = missing;
            }
        } else {
            String[] split = splitAndTrim(s, regex);
            for (int i = 0; i < l; i++) {
                res[i] = i < split.length ? split[i] : missing;
            }
        }
        return res;
    }

    public static String[] splitAndTrim(String s) {
        return splitAndTrim(s, ",");
    }

    public static String[] splitAndTrim(String s, int l) {
        return splitAndTrim(s, ",", l);
    }

    public static String[] splitAndTrim(String s, int l, String missing) {
        return splitAndTrim(s, ",", l, missing);
    }
}
