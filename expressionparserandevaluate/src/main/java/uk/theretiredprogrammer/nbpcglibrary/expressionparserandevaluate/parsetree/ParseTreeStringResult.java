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
package uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree;

import java.util.Map;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;

/**
 * The Parse Tree delivering a String result.
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public abstract class ParseTreeStringResult extends ParseTree {

    /**
     * Evaluate the parse tree returning a String result.
     *
     * @param parameters the set of parameters (keys and values)
     * @return the result of the evaluation
     */
    public abstract String getResult(Map<String, String> parameters);

    /**
     * Evaluate a parse tree ,with a given set of parameters, generating a
     * String result.
     *
     * @param parsetree the parse tree
     * @param parameters the set of parameters (keys and values)
     * @return the result of the evaluation
     */
    public static String evaluate(ParseTree parsetree, Map<String, String> parameters) {
        if (parsetree instanceof ParseTreeStringResult) {
            return ((ParseTreeStringResult) parsetree).getResult(parameters);
        }
        if (parsetree instanceof ParseTreeIntegerResult) {
            return Integer.toString(((ParseTreeIntegerResult) parsetree).getResult(parameters));
        }
        if (parsetree instanceof ParseTreeBooleanResult) {
            return ((ParseTreeBooleanResult) parsetree).getResult(parameters) ? "True" : "False";
        }
        throw new LogicException("EvalFailure: unknown parsetree datatype");
    }
}
