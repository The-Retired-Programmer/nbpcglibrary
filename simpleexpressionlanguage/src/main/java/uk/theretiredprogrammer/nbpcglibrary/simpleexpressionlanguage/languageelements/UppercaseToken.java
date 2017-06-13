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
package uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements;

import java.util.Map;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTree;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTreeStringResult;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.FunctionToken;

/**
 * The Uppercase Function "uppercase(string)". Translates the given string into
 * an uppercase version of the string.
 * 
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class UppercaseToken extends FunctionToken {

    /**
     * Constructor.
     * 
     * @param index the token's key
     */
    public UppercaseToken(int index) {
        super(index);
    }

    @Override
    public String getTokenString() {
        return "uppercase";
    }

    @Override
    public ParseTree getParseTree(ParseTree lower, ParseTree upper) {
        extractParameters("uppercase", 1, lower, upper);
        return new UppercaseFunction();
    }

    private class UppercaseFunction extends ParseTreeStringResult {

        @Override
        public String getResult(Map<String, String> parameters) {
            return ParseTreeStringResult.evaluate(p[1], parameters).toUpperCase();
        }
    }
}
