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

import java.util.function.Supplier;

/**
 * Defines a rule to be associated with a field.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <T> the class of the object being tested
 */
public abstract class Rule<T> {

    private final String failuremessage;
    private final Supplier<T> provider;

    /**
     * Constructor
     *
     * @param provider the provider of the value
     * @param failuremessage the failure message to be displayed when this rule
     * is broken
     */
    public Rule(Supplier<T> provider,String failuremessage) {
        this.failuremessage = failuremessage;
        this.provider = provider;
    }

    /**
     * Test if the rule is passing.
     *
     * @param sb the StringBuilder collecting failure messages
     * @return true if rule is passing
     */
    public final boolean check(StringBuilder sb) {
        if (ruleCheck(provider)) {
            return true;
        }
        sb.append(failuremessage);
        return false;
    }

    /**
     * Test if the rule is passing.
     *
     * @param provider  the provider of the value
     * @return true if rule is passing
     */
    protected abstract boolean ruleCheck(Supplier<T> provider);
}
