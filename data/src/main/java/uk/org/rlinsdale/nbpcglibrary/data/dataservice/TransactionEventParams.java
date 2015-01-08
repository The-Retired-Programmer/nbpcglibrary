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

import uk.org.rlinsdale.nbpcglibrary.common.IntWithDescription;
import uk.org.rlinsdale.nbpcglibrary.common.EventParams;

/**
 * The Parameter Object passed when a Transaction Listener action is fired.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class TransactionEventParams implements EventParams {

    /**
     * Id for Begin Transaction
     */
    public static final IntWithDescription BEGIN = new IntWithDescription(1, "Begin");

    /**
     * Id for Commit Transaction
     */
    public static final IntWithDescription COMMIT = new IntWithDescription(2, "Commit");

    /**
     * Id for Rollback Transaction
     */
    public static final IntWithDescription ROLLBACK = new IntWithDescription(3, "Rollback");
    static final TransactionEventParams BEGINListenerParams = new TransactionEventParams(BEGIN);
    static final TransactionEventParams COMMITListenerParams = new TransactionEventParams(COMMIT);
    static final TransactionEventParams ROLLBACKListenerParams = new TransactionEventParams(ROLLBACK);
    //
    private final IntWithDescription type;

    /**
     * Constructor.
     *
     * @param type the transaction type
     */
    public TransactionEventParams(IntWithDescription type) {
        this.type = type;
    }

    /**
     * Get the Transaction Type.
     *
     * @return the transaction type
     */
    public IntWithDescription get() {
        return type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof TransactionEventParams) {
            return this.type == ((TransactionEventParams) obj).type;
        }
        if (obj instanceof IntWithDescription) {
            return this.type == (IntWithDescription) obj;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Transaction: " + type;
    }
}
