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
package uk.org.rlinsdale.nbpcglibrary.data.dataservice;

import uk.org.rlinsdale.nbpcglibrary.common.EventParams;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;

/**
 * The Parameter Object passed when a Transaction Listener action is fired.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class TransactionEventParams implements EventParams {

    /**
     * Transaction State Requests
     */
    public enum TransactionRequest {

        /**
         * BEGIN transaction Request
         */
        BEGIN,
        /**
         * COMIT transaction Request
         */
        COMMIT,
        /**
         * ROLLBACK transaction Request
         */
        ROLLBACK
    }
    //
    private final TransactionRequest type;

    /**
     * Constructor.
     *
     * @param type the transaction type
     */
    public TransactionEventParams(TransactionRequest type) {
        this.type = type;
    }

    /**
     * Get the Transaction Type.
     *
     * @return the transaction request
     */
    public TransactionRequest get() {
        return type;
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, type.toString());
    }
}
