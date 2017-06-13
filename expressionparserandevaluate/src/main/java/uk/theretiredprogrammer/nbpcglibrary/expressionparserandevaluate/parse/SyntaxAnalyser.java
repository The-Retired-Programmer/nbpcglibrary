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

import java.util.ArrayList;
import java.util.List;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.LanguageDefinition;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.parsetree.ParseTree;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.OperatorToken;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.Token;

/**
 * The Syntax Analyser for an Operator precedence expression.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class SyntaxAnalyser {

    /**
     * The action to be taken following operator comparison
     */
    public enum TestResult {

        /**
         * Insert the object (push?)
         */
        INSERT,
        /**
         * Parse finished
         */
        FINISH,
        /**
         * Continue processing
         */
        CONTINUE
    };
    private final LanguageDefinition languagedefinition;
    private final List<SAItem> stack = new ArrayList<>();
    private LexicalAnalyser lex;

    /**
     * Constructor.
     *
     * @param languagedefinition the expression language definition
     */
    public SyntaxAnalyser(LanguageDefinition languagedefinition) {
        this.languagedefinition = languagedefinition;
    }

    /**
     * Complete the syntax analysis of an expression.
     *
     * @param expression the expression string
     * @param exactMatch true if exact match parameter handling is required
     * @return the expression Parse Tree
     */
    public ParseTree analysis(String expression, Boolean exactMatch) {
        lex = new LexicalAnalyser(languagedefinition, expression, exactMatch);
        Token t = lex.nextToken();
        while (true) {
            if (!(t instanceof OperatorToken)) {
                lex.throwParseException("expecting Operator; found a terminal");
            }
            OperatorToken ot = (OperatorToken) t;
            TestResult res = testPrecidence(ot);
            if (res == TestResult.FINISH) {
                return stack.get(0).pt;
            }
            if (res == TestResult.INSERT) {
                SAItem si = new SAItem();
                si.op = ot;
                t = lex.nextToken();
                if (t instanceof TerminalToken) {
                    TerminalToken tt = (TerminalToken) t;
                    si.pt = tt.getParseTree();
                    t = lex.nextToken();
                }
                stack.add(si);
            }
        }
    }

    private TestResult testPrecidence(OperatorToken ot) {
        int l = stack.size() - 1;
        if (l >= 0) {
            switch (languagedefinition.getOperatorPrecedence(ot, stack.get(l).op)) {
                case COMPLETE:
                    return TestResult.FINISH;
                case END:
                    extractParseTree();
                    return testPrecidence(ot);
                case START:
                    break; // do nothing
                case IN:
                    break; // do nothing
                case ERROR:
                    lex.throwParseException("illegal syntax in expression");
            }
        }
        return TestResult.INSERT;
    }

    private void extractParseTree() {
        int l = stack.size() - 1;
        int s = l;
        while (s > 0) {
            SAItem higher = stack.get(s);
            SAItem lower = stack.get(s - 1);
            switch (languagedefinition.getOperatorPrecedence(higher.op, lower.op)) {
                case START:
                    lower.pt = higher.op.getParseTree(lower.pt, higher.pt);
                    for (int i = l; i >= s; i--) {
                        stack.remove(i);//pop
                    }
                    return;
                case IN:
                    s--;
                    break;
                case ERROR:
                    lex.throwParseException("illegal operator precidence during handle extraction");
            }
        }
        lex.throwParseException("illegal operator precidence during handle extraction - no start handle found");
    }

    private class SAItem {

        public OperatorToken op;
        public ParseTree pt;
    }
}
