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
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTreeBooleanResult;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTreeStringResult;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.FunctionToken;

/**
 * The If Function "if(test, trueresult, falseresult)". Tests a boolean
 * expression and returns either the true reslt or falseresult depending on test
 * result.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class IfToken extends FunctionToken {

    /**
     * Constructor.
     *
     * @param index the token's key
     */
    public IfToken(int index) {
        super(index);
    }

    @Override
    public String getTokenString() {
        return "if";
    }

    @Override
    public ParseTree getParseTree(ParseTree lower, ParseTree upper) {
        extractParameters("if", 3, lower, upper);
        return new IfFunction();
    }

    private class IfFunction extends ParseTreeStringResult {

        @Override
        public String getResult(Map<String, String> parameters) {
            return ParseTreeBooleanResult.evaluate(p[1], parameters)
                    ? ParseTreeStringResult.evaluate(p[2], parameters)
                    : ParseTreeStringResult.evaluate(p[3], parameters);
        }
    }
}
