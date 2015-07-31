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
package uk.org.rlinsdale.nbpcglibrary.api;

/**
 * LogicException Class, a general exception class for use when throwing logic
 * errors.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class LogicException extends RuntimeException {

    /**
     * Constructor
     *
     * @param msg the exception message
     */
    public LogicException(String msg) {
        super(msg);
    }

    /**
     * Constructor
     *
     * @param msg the exception message
     * @param ex the causal exception
     */
    public LogicException(String msg, Exception ex) {
        super(msg, ex);
    }
}
