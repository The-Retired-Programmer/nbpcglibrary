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

import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.FunctionToken;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.OperatorToken;

/**
 * The interface for the definition of a expression language.
 * 
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public interface LanguageDefinition {
    
    /**
     * The operator precedence comparison result
     */
    public enum Op {

        /**
         * Start of an expression segment (ie entering a higher priority subexpression)
         */
        START,

        /**
         * continue within this series of segments
         */
        IN,

        /**
         * End of an expression segment (ie entering a lower priority subexpression)
         */
        END,

        /**
         * Expression Error
         */
        ERROR,

        /**
         * Expression Complete
         */
        COMPLETE
    };
    
    /**
     * Get the set of operator tokens for the expression language being handled.
     * 
     * @return the set of operator tokens
     */
    public OperatorToken[] getTokens();
    
    /**
     * Get the set of function tokens for the expression language being handled.
     * 
     * @return the set of function tokens
     */
    public FunctionToken[] getFunctionTokens();
    
    /**
     * Get the Function Opening bracket character.
     * 
     * @return the function opening bracket character
     */
    public char getFunctionBra();
    
    /**
     * Get the operator precedence comparison result between two operators.
     * 
     * @param current the current operator
     * @param previous the previous operator
     * @return the operator precedence comparison result
     */
    public Op getOperatorPrecedence(OperatorToken current, OperatorToken previous);
}
