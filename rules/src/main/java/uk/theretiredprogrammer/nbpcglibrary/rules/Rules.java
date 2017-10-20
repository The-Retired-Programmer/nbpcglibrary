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

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a set of rules - usually associated with a field. Allows the rules to
 * be tested and error messages combined.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <E> the base entity type
 */
public class Rules<E> {

    private final List<Rule<E, ?>> rules = new ArrayList<>();

    /**
     * Add a rule to this Rule Set.
     *
     * @param rule the rule to add
     */
    public final void addRule(Rule rule) {
        rules.add(rule);
    }

    /**
     * Add rules to this Rule Set.
     *
     * @param newrules the rules to add
     */
    public final void addRules(Rules<E> newrules) {
        if (newrules != null) {
            newrules.rules.stream().forEach((rule) -> {
                rules.add(rule);
            });
        }
    }

    /**
     * Check if all rules in the set are valid.
     *
     * @param sb A string builder into which any error messages are placed if
     * test fails
     * @param be the base entity from which the value to test is to be extracted
     * @return true if all rules are valid
     */
    public final boolean checkRules(StringBuilder sb, E be) {
        boolean valid = true;
        for (Rule rule : rules) {
            if (!rule.check(sb, be)) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Check if all rules (except those marked as unique) in the set are valid.
     *
     * @param sb A string builder into which any error messages are placed if
     * test fails
     * @param be the base entity from which the value to test is to be extracted
     * @return true if all rules (except unique) are valid
     */
    public final boolean checkRulesAtLoad(StringBuilder sb, E be) {
        boolean valid = true;
        for (Rule rule : rules) {
            if (!(rule instanceof UniqueStringRule)) {
                if (!rule.check(sb, be)) {
                    valid = false;
                }
            }
        }
        return valid;
    }
}
