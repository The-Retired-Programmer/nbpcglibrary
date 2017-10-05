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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Manages a set of rules - usually associated with a field. Allows the rules to
 * be tested and error messages combined.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class Rules {

    private final List<Rule> rules = new ArrayList<>();

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
    public final void addRules(Rules newrules) {
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
     * @return true if all rules are valid
     */
    public final boolean checkRules(StringBuilder sb) {
        boolean valid = true;
        for (Rule rule : rules) {
            if (!rule.check(sb)) {
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
     * @return true if all rules (except unique) are valid
     */
    public final boolean checkRulesAtLoad(StringBuilder sb) {
        boolean valid = true;
        for (Rule rule : rules) {
            if (!(rule instanceof UniqueStringRule)) {
                if (!rule.check(sb)) {
                    valid = false;
                }
            }
        }
        return valid;
    }
    
    //  set of standard rule classes
    
    public class MinStringRule extends Rule<String> {

        private final int min;
        
        public MinStringRule(Supplier<String> provider, int min) {
            super(provider, "Too short");
            this.min = min;
        }
        
        @Override
        public boolean ruleCheck(Supplier<String> provider) {
            return provider.get().length() >= min;
        }
    }
    
    public class MaxStringRule extends Rule<String> {

        private final int max;
        
        public MaxStringRule(Supplier<String> provider, int max) {
            super(provider, "Too long");
            this.max = max;
        }
        
        @Override
        public boolean ruleCheck(Supplier<String> provider) {
            return provider.get().length() <= max;
        }
    }
    
    public class UniqueStringRule extends Rule<String> {
        
        private final Supplier<Stream<String>> itemprovider;
        
        public UniqueStringRule(Supplier<String> provider, Supplier<Stream<String>> itemprovider) {
            super(provider, "Is not unique");
            this.itemprovider = itemprovider;
        }
        
        @Override
        public boolean ruleCheck(Supplier<String> provider) {
            String value = provider.get();
            return itemprovider.get().noneMatch((s)-> s.equals(value));
        }
    }
    
    public class MinIntegerRule extends Rule<Integer> {

        private final int min;

        public MinIntegerRule(Supplier<Integer> provider, int min) {
            super(provider, "Too small");
            this.min = min;
        }

        @Override
        protected boolean ruleCheck(Supplier<Integer> provider) {
            return provider.get() >= min;
        }
    }
    
    public class MaxIntegerRule extends Rule<Integer> {

        private final int max;

        public MaxIntegerRule(Supplier<Integer> provider,int max) {
            super(provider, "Too large");
            this.max = max;
        }

        @Override
        protected boolean ruleCheck(Supplier<Integer> provider) {
            return provider.get() <= max;
        }
    }
    
    public class FilenameRule extends Rule<String> {

        public FilenameRule(Supplier<String> provider) {
            super(provider, "Not a filename");
        }

        @Override
        protected boolean ruleCheck(Supplier<String> provider) {
            return new File(provider.get()).isFile();
        }
    }
        
    public class FoldernameRule extends Rule<String> {

        public FoldernameRule(Supplier<String> provider) {
            super(provider, "Not a folder");
        }

        @Override
        protected boolean ruleCheck(Supplier<String> provider) {
            return new File(provider.get()).isDirectory();
        }
    }
    
    public class DefinedRule extends Rule<Integer> {

        public DefinedRule(Supplier<Integer> provider) {
            super(provider, "Not defined");
        }

        @Override
        public boolean ruleCheck(Supplier<Integer> provider) {
            return provider.get() != 0;
        }
    }
}
