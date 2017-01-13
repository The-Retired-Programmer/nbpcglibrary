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

package uk.theretiredprogrammer.nbpcglibrary.common;

import uk.theretiredprogrammer.nbpcglibrary.api.EventParams;
import uk.theretiredprogrammer.nbpcglibrary.api.HasInstanceDescription;

/**
 * A Standard Listener - defined with a generic class for use as parameters
 * passed on action.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <P> the Class of the listener parameter
 */
public abstract class Listener<P extends EventParams> implements HasInstanceDescription {

    private final String description;

    /**
     * Constructor
     *
     * @param description the listener's descriptive name - for use in error
     * /log reporting
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public Listener(String description) {
        this.description = description;
        LogBuilder.writeConstructorLog("nbpcglibrary.common", this, description);
    }
    
    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, description);
    }

    /**
     * Fired on Action.
     *
     * @param p the listener parameters
     */
    public void actionPerformed(P p) {
        LogBuilder.writeLog("nbpcglibrary.common",this, "actionPerformed", p);
        action(p);
    }

    @Override
    public String toString() {
        return description;
    }

    /**
     * The action processing method.
     *
     * @param p the listener parameters
     */
    public abstract void action(P p);
}
