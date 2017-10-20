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

import java.io.File;
import java.util.function.Function;

/**
 *
 * @author richard
 */
public class FilenameRule<E> extends Rule<E, String> {

    public FilenameRule(Function<E, String> provider) {
        super(provider, "Not a filename");
    }

    @Override
    protected boolean ruleCheck(Function<E, String> provider, E be) {
        return new File(provider.apply(be)).isFile();
    }
}
