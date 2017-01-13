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
package uk.theretiredprogrammer.nbpcglibrary.data.entityreferences;

import uk.theretiredprogrammer.nbpcglibrary.api.EventParams;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;

/**
 * The Listener Parameter Object - used to transfer state when listener is
 * fired.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <K> the primary key class
 */
public class PrimaryKeyChangeEventParams<K> implements EventParams {

    private final K pkey;
    
    /**
     * Constructor
     * 
     * @param pkey the primary key (new value)
     */
    public PrimaryKeyChangeEventParams(K pkey){
        this.pkey = pkey;
    }
    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this,pkey.toString());
    }
    
    /**
     * Get the new primary key value for this change
     * 
     * @return the new primary key value
     */
    public K getNewPKey() {
        return pkey;
    }
}
