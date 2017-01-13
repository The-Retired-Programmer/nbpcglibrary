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
package uk.theretiredprogrammer.nbpcglibrary.localdatabaseaccess;

import uk.theretiredprogrammer.nbpcglibrary.api.EventParams;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;

/**
 * The Parameter Object passed when a Transaction Listener action is fired.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
