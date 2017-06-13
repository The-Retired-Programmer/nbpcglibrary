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
package uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate;

import java.util.Map;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterLog;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parse.SyntaxAnalyser;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTree;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTreeBooleanResult;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTreeIntegerResult;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTreeStringResult;

/**
 * Parser and Evaluator for a expression language.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@RegisterLog("nbpcglibrary.expressionparserandevaluate")
public class ParseAndEvaluate {

    private final LanguageDefinition languagedefinition;
    private ParseTree parsetreeroot;

    /**
     * Constructor.
     *
     * @param languagedefinition the expression language definition.
     */
    public ParseAndEvaluate(LanguageDefinition languagedefinition) {
        this.languagedefinition = languagedefinition;
    }

    // parse phase
    /**
     * Parse the given expression. Exact parameters match is not required.
     *
     * @param expression the expression string
     */
    public void parse(String expression) {
        parse(expression, false);
    }

    /**
     * Parse the given expression. The parameter match can be defined as:
     *
     * exact ie exact same form including embedded spaces, upper/lower case etc.
     *
     * not exact ie upper/lower case are treated the same, embedded spaces and
     * other non alpha numeric characters can be replaced with underbar.
     *
     * @param expression the expression string
     * @param exactMatch true if exact match is required
     */
    public void parse(String expression, boolean exactMatch) {
        SyntaxAnalyser sa = new SyntaxAnalyser(languagedefinition);
        parsetreeroot = sa.analysis(expression, exactMatch);
    }

    // evaluation phase
    /**
     * Evaluate the previously parsed expression using a set of parameters
     * resulting in a boolean result.
     *
     * @param parameters the set of parameters (keys and values)
     * @return the evaluated expression's boolean result
     */
    public boolean evaluateAsBoolean(Map<String, String> parameters) {
        return ParseTreeBooleanResult.evaluate(parsetreeroot, parameters);
    }

    /**
     * Evaluate the previously parsed expression using a set of parameters
     * resulting in a String result.
     *
     * @param parameters the set of parameters (keys and values)
     * @return the evaluated expression's String result
     */
    public String evaluateAsString(Map<String, String> parameters) {
        return ParseTreeStringResult.evaluate(parsetreeroot, parameters);
    }

    /**
     * Evaluate the previously parsed expression using a set of parameters
     * resulting in an integer result.
     *
     * @param parameters the set of parameters (keys and values)
     * @return the evaluated expression's integer result
     */
    public int evaluateAsInteger(Map<String, String> parameters) {
        return ParseTreeIntegerResult.evaluate(parsetreeroot, parameters);
    }

    /**
     * Convert a parameter name to its non exact form.
     *
     * @param name the original parameter name
     * @return the "non exact" parameter name
     */
    public static String nonExactName(String name) {
        char[] chars = name.toLowerCase().toCharArray();
        int cnt = 0;
        for (char c : chars) {
            if (!((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_')) {
                chars[cnt] = '_';
            }
            cnt++;
        }
        return String.valueOf(chars);
    }
}
