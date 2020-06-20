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

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import lv.kristsk.k5z.plugin.intellij.parser.K5zParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class K5zBraceMatcher implements PairedBraceMatcher {

    private static final BracePair[] PAIRS = {
        new BracePair(
            K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zParser.LPAREN),
            K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zParser.RPAREN),
            false
        ),
        new BracePair(
            K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zParser.LBRACK),
            K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zParser.RBRACK),
            false
        ),
        new BracePair(
            K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zParser.LBRACE),
            K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zParser.RBRACE),
            true
        )
    };

    @NotNull
    public BracePair[] getPairs() {

        return PAIRS;
    }

    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType braceType, @Nullable IElementType tokenType) {

        return true;
    }

    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {

        return openingBraceOffset;
    }
}
