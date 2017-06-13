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

import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parse.ParseTreeFunctionParameter;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTree;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;

/**
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public abstract class FunctionToken extends OperatorToken {

    private final static int MAXPARAMETERS = 20;

    /**
     * The set of parameter for the function - each represented by its parse tree.
     */
    protected ParseTree[] p = new ParseTree[MAXPARAMETERS + 1];

    /**
     * Constructor.
     * 
     * @param index the token's key
     */
    public FunctionToken(int index) {
        super(index);
    }

    /**
     * Extract Function Parameters from the upper parse tree.
     * 
     * @param fname the function name
     * @param no the number of expected parameters
     * @param lower the lower parse tree - should be null
     * @param upper the upper parse tree - contains the set of parameter parse trees.
     */
    protected void extractParameters(String fname, int no, ParseTree lower, ParseTree upper) {
        if (lower != null) {
            throw new LogicException("Failure: " + fname + "() is not a diadic operator");
        }
        ParseTree pt = upper;
        if (no > MAXPARAMETERS) {
            throw new LogicException("Failure: " + fname + "() - too many parameters expected - language supports a maximum of " + MAXPARAMETERS);
        } else if (no == 0) {
            if (pt != null) {
                throw new LogicException("Failure: " + fname + "() should not have any parameters");
            }
        } else {
            int i = 1;
            while (i < no) {
                pt = extractNextParameter(fname, i, no, pt);
                i++;
            }
            p[no] = extractLastParameter(fname, no, pt);
        }
    }

    private ParseTree extractLastParameter(String fname, int no, ParseTree pt) {
        if (pt == null) {
            throw new LogicException("Failure: " + fname + "() - too few parameters - " + no + " expected");
        }
        if (pt instanceof ParseTreeFunctionParameter) {
            throw new LogicException("Failure: " + fname + "() - too many parameters - " + no + " expected");
        }
        return pt;
    }

    private ParseTree extractNextParameter(String fname, int index, int no, ParseTree pt) {
        if (pt == null) {
            throw new LogicException("Failure: " + fname + "() - too few parameters - " + no + " expected");
        }
        if (!(pt instanceof ParseTreeFunctionParameter)) {
            throw new LogicException("Failure: " + fname + "() - too few parameters - " + no + " expected");
        }
        ParseTreeFunctionParameter pf = (ParseTreeFunctionParameter) pt;
        ParseTree pl = pf.getNext();
        if (pl == null) {
            throw new LogicException("Failure: " + fname + "(,) - parameter " + index + " is undefined");
        }
        p[index] = pl;
        return pf.getRest();
    }
}
