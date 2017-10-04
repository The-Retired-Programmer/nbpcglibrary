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

import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.LanguageDefinition;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.ParseAndEvaluate;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.EOSToken;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.FunctionToken;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.OperatorToken;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.SOSToken;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.Token;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;

/**
 * The Lexical Analyser for the Expression language.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class LexicalAnalyser {

    private final LanguageDefinition languagedefinition;
    // token extraction reader
    private final String expression;
    private int ptr;
    private final int expressionlength;
    private final boolean exactMatch;

    /**
     * Constructor.
     *
     * @param languagedefinition the expression language definition
     * @param expression the expression to parse
     * @param exactMatch true if exact parameter matching is required
     */
    public LexicalAnalyser(LanguageDefinition languagedefinition, String expression, boolean exactMatch) {
        this.languagedefinition = languagedefinition;
        this.expression = SOSToken.getChar() + expression + EOSToken.getChar();
        expressionlength = this.expression.length();
        ptr = 0;
        this.exactMatch = exactMatch;
    }

    /**
     * Get the next token from the expression.
     *
     * @return the next token
     */
    public Token nextToken() {
        Token t;
        StringBuilder buffer;
        String name;
        char c = nextNonWSChar();
        // first - the built in value extraction
        switch (c) {
            case '\"': // string literal follows
                buffer = new StringBuilder();
                while (true) {
                    c = nextChar();
                    if (c == EOSToken.getChar()) {
                        throwParseException("bad literal string - not terminated by \"");
                    }
                    if (c == '"') {
                        break;
                    }
                    if (c == '\\') { // we only support passthrough at present
                        c = nextChar();
                    }
                    buffer.append(c);
                }
                t = new StringToken(buffer.toString());
                return t;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': // first digit of integer literal
                buffer = new StringBuilder();
                buffer.append(c);
                while (isIntegerChar(c = nextChar())) {
                    buffer.append(c);
                }
                ptr--;
                int ival = 0;
                try {
                    ival = Integer.parseInt(buffer.toString());
                } catch (NumberFormatException ex) {
                    throwParseException("bad literal integer - illegal character - should never happen!");
                }
                t = new IntegerToken(ival);
                return t;
            case '[':
                buffer = new StringBuilder();
                while (true) {
                    c = nextChar();
                    if (c == EOSToken.getChar()) {
                        throwParseException("bad special literal - not terminated by ]");
                    }
                    if (c == ']') {
                        break;
                    }
                    buffer.append(c);
                }
                String special = buffer.toString();
                if ("true".equalsIgnoreCase(special) || "yes".equalsIgnoreCase(special) || "y".equalsIgnoreCase(special)
                        || "set".equalsIgnoreCase(special) || "on".equalsIgnoreCase(special)
                        || "OK".equalsIgnoreCase(special)) {
                    t = new BooleanToken(Boolean.TRUE);
                    return t;
                }
                if ("false".equalsIgnoreCase(special) || "no".equalsIgnoreCase(special) || "n".equalsIgnoreCase(special)
                        || "unset".equalsIgnoreCase(special) || "off".equalsIgnoreCase(special)) {
                    t = new BooleanToken(Boolean.FALSE);
                    return t;
                }
                throwParseException("unknown special literal - " + special);
            case '`':
                buffer = new StringBuilder();
                while (true) {
                    c = nextChar();
                    if (c == EOSToken.getChar()) {
                        throwParseException("bad parameter - not terminated by `");
                    }
                    if (c == '`') {
                        break;
                    }
                    buffer.append(c);
                }
                name = buffer.toString();
                if (!exactMatch) {
                    name = ParseAndEvaluate.nonExactName(name);
                }
                t = new ParameterToken(name);
                return t;
        }
        ptr--;
        for (OperatorToken ot : languagedefinition.getTokens()) {
            if (tokenMatcher(ot)) {
                ptr += ot.getTokenString().length();
                return ot;
            }
        }
        ptr++;
        // and now attempt to process text for simple parameternames or functionnames
        buffer = new StringBuilder();
        if (!isNameChar(c)) {
            throwParseException("illegal name character - found " + c);
        }
        buffer.append(c);
        while (isNameChar(c = nextChar())) {
            buffer.append(c);
        }
        ptr--;
        name = buffer.toString();
        FunctionToken function = functionMatch(name);
        if (function != null) {
            c = nextNonWSChar();
            if (c == languagedefinition.getFunctionBra()) {
                return function;
            } else {
                throwParseException("illegal function construct - expected " + languagedefinition.getFunctionBra() + " - found " + c);
                return null;
            }
        } else {
            if (!exactMatch) {
                name = ParseAndEvaluate.nonExactName(name);
            }
            t = new ParameterToken(name);
            return t;
        }

    }

    private boolean tokenMatcher(OperatorToken t) {
        String s = t.getTokenString();
        int l = s.length();
        if (ptr + l > expressionlength) {
            return false;
        }
        return expression.substring(ptr, ptr + l).equals(s);
    }

    private boolean isNameChar(char c) {
        return (c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'));
    }

    private boolean isIntegerChar(char c) {
        return (c >= '0' && c <= '9');
    }

    private char nextChar() {
        return expression.charAt(ptr++);
    }

    private char nextNonWSChar() {
        char c;
        do {
            c = nextChar();
        } while (c == ' ' || c == '\t');
        return c;
    }

    private FunctionToken functionMatch(String name) {
        String namelc = name.toLowerCase();
        for (FunctionToken f : languagedefinition.getFunctionTokens()) {
            if (f.getTokenString().equals(namelc)) {
                return f;
            }
        }
        return null;
    }

    /**
     * Throw a Parse Exception.
     *
     * @param msg the basic message to be inserted into the exception
     */
    public void throwParseException(String msg) {
        String displaystring = '<' + expression.substring(1, expressionlength - 1) + '>';
        throw new LogicException("ParseFailure: " + msg + '\n' + displaystring
                + '\n' + (ptr == 0 ? "" : displaystring.substring(0, ptr - 1)) + '^');
    }
}
