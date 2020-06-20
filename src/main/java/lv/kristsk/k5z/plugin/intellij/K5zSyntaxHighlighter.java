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

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import lv.kristsk.k5z.plugin.intellij.parser.K5zLexer;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

public class K5zSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey KEYWORD = createTextAttributesKey("KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey VARIABLE_NAME = createTextAttributesKey("VARIABLE_NAME", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
    public static final TextAttributesKey FUNCTION_NAME = createTextAttributesKey("FUNCTION_NAME", DefaultLanguageHighlighterColors.FUNCTION_CALL);
    public static final TextAttributesKey NUMBER = createTextAttributesKey("NUMBER", DefaultLanguageHighlighterColors.NUMBER);

    public static final TextAttributesKey STRING = createTextAttributesKey("STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey COMMENT = createTextAttributesKey("COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);

    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] VARIABLE_NAME_KEYS = new TextAttributesKey[]{VARIABLE_NAME};
    private static final TextAttributesKey[] FUNCTION_NAME_KEYS = new TextAttributesKey[]{FUNCTION_NAME};
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};

    private static final TextAttributesKey[] BAD_CHARACTER_KEYS = new TextAttributesKey[]{HighlighterColors.BAD_CHARACTER};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {

        K5zTokenTypes.initialize();

        K5zLexer lexer = new K5zLexer(null);
        return new ANTLRLexerAdaptor(K5zLanguage.INSTANCE, lexer);
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {

        if (!(tokenType instanceof TokenIElementType)) {
            return EMPTY_KEYS;
        }

        if (K5zTokenTypes.KEYWORDS.contains(tokenType)) {
            return KEYWORD_KEYS;
        }
        else if (tokenType == K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zLexer.SNAKE_ID)) {
            return VARIABLE_NAME_KEYS;
        }
        else if (tokenType == K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zLexer.CAMEL_ID)) {
            return FUNCTION_NAME_KEYS;
        }
        else if (tokenType == K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zLexer.QUOTED_STRING)) {
            return STRING_KEYS;
        }
        else if (K5zTokenTypes.COMMENTS.contains(tokenType)) {
            return COMMENT_KEYS;
        }
        else if (K5zTokenTypes.NUMBERS.contains(tokenType)) {
            return NUMBER_KEYS;
        }
        else if (tokenType == K5zTokenTypes.BAD_TOKEN_TYPE) {
            return BAD_CHARACTER_KEYS;
        }
        else {
            return EMPTY_KEYS;
        }
    }
}
