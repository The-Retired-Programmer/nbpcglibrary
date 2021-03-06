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
package uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens;

import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTree;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;

/**
 * The Opening Bracket Token.
 * 
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class BraToken extends OperatorToken {

    /**
     * Constructor.
     * 
     * @param index the token's key
     */
    public BraToken(int index){
        super(index);
    }
    
    @Override
    public String getTokenString() {
        return "(";
    }
    
    @Override
    public ParseTree getParseTree(ParseTree lower, ParseTree upper) {
        if (lower != null) {
            throw new LogicException("Failure: ( is not a diadic operator");
        }
        return upper;
    }
}
