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
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTreeIntegerResult;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTreeStringResult;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.FunctionToken;

/**
 * The Extract Function "extract(fromstring, start, length)". Extract a
 * substring from a string, from start position and of length. The start
 * position starts from 1.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class ExtractToken extends FunctionToken {

    /**
     * Constructor.
     *
     * @param index the token's key
     */
    public ExtractToken(int index) {
        super(index);
    }

    @Override
    public String getTokenString() {
        return "extract";
    }

    @Override
    public ParseTree getParseTree(ParseTree lower, ParseTree upper) {
        extractParameters("extract", 3, lower, upper);
        return new ExtractFunction();
    }

    private class ExtractFunction extends ParseTreeStringResult {

        @Override
        public String getResult(Map<String, String> parameters) {
            String s = ParseTreeStringResult.evaluate(p[1], parameters);
            int start = ParseTreeIntegerResult.evaluate(p[2], parameters) - 1;
            if (start < 0) {
                start = 0;
            }
            int end = ParseTreeIntegerResult.evaluate(p[3], parameters) + start;
            return end > s.length() ? s.substring(start) : s.substring(start, end);
        }
    }
}
