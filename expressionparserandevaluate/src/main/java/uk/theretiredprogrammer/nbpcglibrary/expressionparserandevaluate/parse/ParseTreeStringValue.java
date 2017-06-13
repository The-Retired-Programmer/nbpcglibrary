/*
 * Copyright 2015-2017 Richard Linsdale.
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
package uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parse;

import java.util.Map;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTreeStringResult;

/**
 * A Parse Tree holding a String value.
 * 
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class ParseTreeStringValue extends ParseTreeStringResult {

    private final String value;

    /**
     * Constructor.
     * 
     * @param value the value
     */
    public ParseTreeStringValue(String value) {
        this.value = value;
    }

    @Override
    public String getResult(Map<String, String> parameters) {
        return value;
    }
}
