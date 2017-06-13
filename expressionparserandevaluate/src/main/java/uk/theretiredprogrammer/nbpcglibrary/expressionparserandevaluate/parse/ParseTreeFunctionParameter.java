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
package uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parse;

import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTree;

/**
 * A Parse Tree holding a Function Parameter.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class ParseTreeFunctionParameter extends ParseTree {

    private final ParseTree next;
    private final ParseTree rest;

    /**
     * Constructor.
     *
     * @param next this parameter parse tree
     * @param rest the parse tree for the remaining parameters
     */
    public ParseTreeFunctionParameter(ParseTree next, ParseTree rest) {
        this.next = next;
        this.rest = rest;
    }

    /**
     * Get this parameter parse tree.
     *
     * @return this parameter parse tree
     */
    public ParseTree getNext() {
        return next;
    }

    /**
     * Get the parse tree representing the remaining parameters.
     *
     * @return the parse tree representing the remaining parameters
     */
    public ParseTree getRest() {
        return rest;
    }
}
