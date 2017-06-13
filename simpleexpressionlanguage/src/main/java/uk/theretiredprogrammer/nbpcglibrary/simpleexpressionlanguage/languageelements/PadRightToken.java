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
 * The PadRight Function "padright(fromstring, size, padchar)". Increases the
 * length of a string to the required size by adding one or more padchars to the
 * right of the fromstring. If the fromstring is in excess of the required size,
 * the function just returns the fromstring (not truncated).
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class PadRightToken extends FunctionToken {

    /**
     * Constructor.
     *
     * @param index the token's key
     */
    public PadRightToken(int index) {
        super(index);
    }

    @Override
    public String getTokenString() {
        return "padright";
    }

    @Override
    public ParseTree getParseTree(ParseTree lower, ParseTree upper) {
        extractParameters("padright", 3, lower, upper);
        return new PadRightFunction();
    }

    private class PadRightFunction extends ParseTreeStringResult {

        @Override
        public String getResult(Map<String, String> parameters) {
            StringBuilder b = new StringBuilder();
            String s = ParseTreeStringResult.evaluate(p[1], parameters);
            int size = ParseTreeIntegerResult.evaluate(p[2], parameters);
            char pad = ParseTreeStringResult.evaluate(p[3], parameters).charAt(0);
            b.append(s);
            for (int i = s.length(); i < size; i++) {
                b.append(pad);
            }
            return b.toString();
        }
    }
}
