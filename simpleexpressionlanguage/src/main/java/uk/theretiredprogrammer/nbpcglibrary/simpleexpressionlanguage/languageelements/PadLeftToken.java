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
 * The PadLeft Function "padleft(fromstring, size, padchar)". Increases the
 * length of a string to the required size by adding one or more padchars to the
 * left of the fromstring. If the fromstring is in excess of the required size,
 * the function just returns the fromstring (not truncated).
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class PadLeftToken extends FunctionToken {

    /**
     * Constructor.
     *
     * @param index the token's key
     */
    public PadLeftToken(int index) {
        super(index);
    }

    @Override
    public String getTokenString() {
        return "padleft";
    }

    @Override
    public ParseTree getParseTree(ParseTree lower, ParseTree upper) {
        extractParameters("padleft", 3, lower, upper);
        return new PadLeftFunction();
    }

    private class PadLeftFunction extends ParseTreeStringResult {

        @Override
        public String getResult(Map<String, String> parameters) {
            StringBuilder b = new StringBuilder();
            String s = ParseTreeStringResult.evaluate(p[1], parameters);
            int size = ParseTreeIntegerResult.evaluate(p[2], parameters);
            char pad = ParseTreeStringResult.evaluate(p[3], parameters).charAt(0);
            for (int i = s.length(); i < size; i++) {
                b.append(pad);
            }
            b.append(s);
            return b.toString();
        }
    }
}
