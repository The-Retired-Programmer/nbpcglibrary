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
package uk.theretiredprogrammer.nbpcglibrary.rules;

import java.util.function.Function;

/**
 * Defines a rule to be associated with a field.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the base entity type
 * @param <T> the class of the object being tested
 */
public abstract class Rule<E, T> {

    private final String failuremessage;
    private final Function<E, T> provider;

    /**
     * Constructor
     *
     * @param provider the provider of the value
     * @param failuremessage the failure message to be displayed when this rule
     * is broken
     */
    public Rule(Function<E, T> provider, String failuremessage) {
        this.failuremessage = failuremessage;
        this.provider = provider;
    }

    /**
     * Test if the rule is passing.
     *
     * @param sb the StringBuilder collecting failure messages
     * @param be the base entity from which the value to test is to be extracted
     * @return true if rule is passing
     */
    public final boolean check(StringBuilder sb, E be) {
        if (ruleCheck(provider, be)) {
            return true;
        }
        sb.append(failuremessage);
        return false;
    }

    /**
     * Test if the rule is passing.
     *
     * @param provider the provider of the value
     * @param be the base entity from which the value to test is to be extracted
     * @return true if rule is passing
     */
    protected abstract boolean ruleCheck(Function<E, T> provider, E be);
}
