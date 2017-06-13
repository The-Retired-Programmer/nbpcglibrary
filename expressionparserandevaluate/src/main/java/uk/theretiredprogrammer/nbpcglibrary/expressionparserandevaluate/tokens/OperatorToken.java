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
import uk.theretiredprogrammer.nbpcglibrary.common.LogBuilder;

/**
 * The Operator Token.
 * 
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public abstract class OperatorToken implements Token {

    private final int index;

    /**
     * Constructor.
     * 
     * @param index the token's key
     */
    public OperatorToken(int index) {
        this.index = index;
    }
    
    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this);
    }

    /**
     * Get the Token's key.
     * 
     * @return the token key
     */
    public final int getIndex() {
        return index;
    }

    /**
     * Get the Token String (ie it's form in an expression).
     * 
     * @return the token string
     */
    public abstract String getTokenString();

    /**
     * Get the Parse tree resulting from this operator acting on one or two parse trees.
     * 
     * @param lower if diadic operator then the lhs parse tree, else ignored if monadicif monadic then parse tree
     * @param upper if diadic operator then the rhs parse tree, if monadic then parse tree
     * @return the resulting parse tree
     */
    public abstract ParseTree getParseTree(ParseTree lower, ParseTree upper);
}
