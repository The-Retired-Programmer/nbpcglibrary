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
package uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage;

import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.LanguageDefinition;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.LanguageDefinition.Op;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.BraToken;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.CommaToken;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.EOSToken;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.FunctionToken;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.KetToken;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.OperatorToken;
import uk.theretiredprogrammer.nbpcglibrary.expressionparserandevaluate.tokens.SOSToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.AddToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.AndToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.DivToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.DotToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.EqualsIgnoreCaseToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.EqualsToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.ExtractToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.GreaterThanEqualsToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.GreaterThanToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.IfToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.LeftToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.LessThanEqualsToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.LessThanToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.LowercaseToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.ModToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.MultToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.NotEqualsIgnoreCaseToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.NotEqualsToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.NotToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.OrToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.PadLeftToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.PadRightToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.RightToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.SizeToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.SubToken;
import uk.theretiredprogrammer.nbpcglibrary.simpleexpressionlanguage.languageelements.UppercaseToken;

/**
 * The definition for a simple expression language.
 * 
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class SimpleExpressionLanguage implements LanguageDefinition {

    @Override
    public OperatorToken[] getTokens() {
        return new OperatorToken[]{
        new SOSToken(0), new EOSToken(1), new BraToken(2), new KetToken(3), new CommaToken(11),
        new OrToken(4), new AndToken(5),
        new EqualsToken(7), new EqualsIgnoreCaseToken(7), new NotEqualsToken(7), new NotEqualsIgnoreCaseToken(7),
        new GreaterThanEqualsToken(7), new GreaterThanToken(7), new LessThanEqualsToken(7), new LessThanToken(7),
        new NotToken(6), new DotToken(8), new MultToken(9), new DivToken(9), new ModToken(9),
        new AddToken(8), new SubToken(8)};
    }
    //
    @Override
    public FunctionToken[] getFunctionTokens() {
        return  new FunctionToken[]{
        new LowercaseToken(10), new UppercaseToken(10), new LeftToken(10), new RightToken(10),
        new PadLeftToken(10), new PadRightToken(10), new ExtractToken(10), new SizeToken(10),
        new IfToken(10)};
    }
    //
    @Override
    public char getFunctionBra() {
        return '(';
    }
    // operator precedence matrix - describes the syntax
    private final static Op[][] OPMATRIX = new Op[][]{
        /* next=                        sos         eos             (           )           |           &           !           comp        + - .       * / %       function    ,           */                
        /* previous = sos */        {   Op.ERROR,   Op.COMPLETE,    Op.START,   Op.ERROR,   Op.START,   Op.START,   Op.START,   Op.START,   Op.START,   Op.START,   Op.START,  Op.ERROR    },
        /* previous = eos */        {   Op.ERROR,   Op.ERROR,       Op.ERROR,   Op.ERROR,   Op.ERROR,   Op.ERROR,   Op.ERROR,   Op.ERROR,   Op.ERROR,   Op.ERROR,   Op.ERROR,   Op.ERROR    },
        /* previous = ( */          {   Op.ERROR,   Op.ERROR,       Op.START,   Op.IN,      Op.START,   Op.START,   Op.START,   Op.START,   Op.START,   Op.START,   Op.START,  Op.ERROR    },
        /* previous = ) */          {   Op.ERROR,   Op.END,         Op.ERROR,   Op.END,     Op.END,     Op.END,     Op.ERROR,   Op.END,     Op.END,     Op.END,     Op.ERROR,   Op.END      },
        /* previous = | */          {   Op.ERROR,   Op.END,         Op.START,   Op.END,     Op.END,     Op.START,   Op.START,   Op.START,   Op.START,   Op.START,   Op.START,   Op.END      },
        /* previous = & */          {   Op.ERROR,   Op.END,         Op.START,   Op.END,     Op.END,     Op.END,     Op.START,   Op.START,   Op.START,   Op.START,   Op.START,   Op.END      },
        /* previous = ! */          {   Op.ERROR,   Op.END,         Op.START,   Op.END,     Op.END,     Op.END,     Op.START,   Op.START,   Op.START,   Op.START,   Op.START,   Op.END      },
        /* previous = comp */       {   Op.ERROR,   Op.END,         Op.START,   Op.END,     Op.END,     Op.END,     Op.END,     Op.END,     Op.START,   Op.START,   Op.START,   Op.END      },
        /* previous = + - . */      {   Op.ERROR,   Op.END,         Op.START,   Op.END,     Op.END,     Op.END,     Op.END,     Op.END,     Op.END,     Op.START,   Op.START,   Op.END      },
        /* previous = * / % */      {   Op.ERROR,   Op.END,         Op.START,   Op.END,     Op.END,     Op.END,     Op.END,     Op.END,     Op.END,     Op.END,     Op.START,   Op.END      },
        /* previous = function */   {   Op.ERROR,   Op.ERROR,       Op.START,   Op.IN,      Op.START,   Op.START,   Op.START,   Op.START,   Op.START,   Op.START,   Op.START,   Op.START   },
        /* previous = , */          {   Op.ERROR,   Op.ERROR,       Op.START,   Op.END,      Op.START,   Op.START,   Op.START,   Op.START,   Op.START,   Op.START,  Op.START,   Op.START   }
    };
    
    @Override
    public Op getOperatorPrecedence(OperatorToken current, OperatorToken previous) {
        return OPMATRIX[previous.getIndex()][current.getIndex()];
    }
}