/*
 * Copyright 2017 richard.
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
 *
 * @author richard
 */
public class MinIntegerRule<E> extends Rule<E, Integer> {

    private final int min;

    public MinIntegerRule(Function<E, Integer> provider, int min) {
        super(provider, "Too small");
        this.min = min;
    }

    @Override
    protected boolean ruleCheck(Function<E, Integer> provider, E be) {
        return provider.apply(be) >= min;
    }
}
