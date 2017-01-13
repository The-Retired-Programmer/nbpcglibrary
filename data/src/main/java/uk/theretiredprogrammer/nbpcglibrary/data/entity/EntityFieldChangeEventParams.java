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
package uk.theretiredprogrammer.nbpcglibrary.data.entity;

import uk.theretiredprogrammer.nbpcglibrary.api.EventParams;
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;

/**
 * The Parameter Class for a FieldChange listener.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <F> the Fields enum class
 */
public class EntityFieldChangeEventParams<F> implements EventParams {
    
    private final F field;

    /**
     * Constructor.
     *
     * @param field the entity field identifier
     */
    public EntityFieldChangeEventParams(F field) {
        this.field = field;
    }

    /**
     * Get the field Id
     *
     * @return the field identifier
     */
    public F get() {
        return field;
    }


    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, (field!=null?field.toString():"ALL")+ " change" );
    }
}
