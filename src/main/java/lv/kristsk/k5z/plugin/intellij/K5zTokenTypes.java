/*
 * Copyright (c) 2014, Krists Krigers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of  nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific
 *    prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package lv.kristsk.k5z.plugin.intellij;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import lv.kristsk.k5z.plugin.intellij.parser.K5zLexer;
import lv.kristsk.k5z.plugin.intellij.parser.K5zParser;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;

import java.util.List;

public class K5zTokenTypes {

    public static String[] tokenNames;

    static {
        tokenNames = new String[K5zParser.VOCABULARY.getMaxTokenType()];

        for (int i = 0; i != K5zParser.VOCABULARY.getMaxTokenType(); i++) {

            tokenNames[i] = K5zParser.VOCABULARY.getLiteralName(i);

            if (tokenNames[i] == null) {

                tokenNames[i] = K5zParser.VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {

                tokenNames[i] = "<INVALID>";
            }
        }

        PSIElementTypeFactory.defineLanguageIElementTypes(K5zLanguage.INSTANCE,
            K5zTokenTypes.tokenNames,
            K5zParser.ruleNames
        );
    }

    public static IElementType BAD_TOKEN_TYPE = new IElementType("BAD_TOKEN", K5zLanguage.INSTANCE);

    public static final List<TokenIElementType> TOKEN_ELEMENT_TYPES =
        PSIElementTypeFactory.getTokenIElementTypes(K5zLanguage.INSTANCE);

    public static final List<RuleIElementType> RULE_ELEMENT_TYPES =
        PSIElementTypeFactory.getRuleIElementTypes(K5zLanguage.INSTANCE);

    public static final TokenSet KEYWORDS =
        PSIElementTypeFactory.createTokenSet(
            K5zLanguage.INSTANCE,
            K5zLexer.K_PROGRAM, K5zLexer.K_LIBRARY,
            K5zLexer.K_IMPORT, K5zLexer.K_INCLUDE,
            K5zLexer.K_AS, K5zLexer.K_FROM,
            K5zLexer.K_PHP, K5zLexer.K_PHTML,
            K5zLexer.K_WITH, K5zLexer.K_FUNCTION,
            K5zLexer.K_VAL, K5zLexer.K_REF, K5zLexer.K_OPT,
            K5zLexer.K_RETURN, K5zLexer.K_FALSE, K5zLexer.K_TRUE,
            K5zLexer.K_AND, K5zLexer.K_OR, K5zLexer.K_NOT,
            K5zLexer.K_IF, K5zLexer.K_ELSE,
            K5zLexer.K_FOREACH, K5zLexer.K_FOR, K5zLexer.K_WHILE,
            K5zLexer.K_DIRTY,
            K5zLexer.K_BREAK, K5zLexer.K_CONTINUE
        );

    public static final TokenSet NUMBERS =
        PSIElementTypeFactory.createTokenSet(
            K5zLanguage.INSTANCE,
            K5zLexer.INTEGER,
            K5zLexer.FLOAT
        );

    public static final TokenSet COMMENTS =
        PSIElementTypeFactory.createTokenSet(
            K5zLanguage.INSTANCE,
            K5zLexer.LINE_COMMENT,
            K5zLexer.BLOCK_COMMENT
        );

    public static final TokenSet WHITESPACES =
        PSIElementTypeFactory.createTokenSet(
            K5zLanguage.INSTANCE,
            K5zLexer.WS
        );

    public static void initialize() {

    }
}
