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

package lv.kristsk.k5z.plugin.intellij.psi.element;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import lv.kristsk.k5z.plugin.intellij.K5zTokenTypes;
import lv.kristsk.k5z.plugin.intellij.parser.K5zLexer;
import lv.kristsk.k5z.plugin.intellij.psi.PsiElementFactory;
import org.jetbrains.annotations.NotNull;

public class FunctionIdentifierElement extends K5zPsiElement {

    public static class Factory implements PsiElementFactory {

        public static Factory INSTANCE = new Factory();

        @Override
        public PsiElement createElement(ASTNode node) {

            return new FunctionIdentifierElement(node);
        }
    }

    public FunctionIdentifierElement(@NotNull ASTNode node) {

        super(node);
    }

    public FunctionIdentifierNameElement getNameElement() {

        return PsiTreeUtil.findChildOfType(this, FunctionIdentifierNameElement.class);
    }

    public FunctionIdentifierLibraryAliasElement getAliasElement() {

        return PsiTreeUtil.findChildOfType(this, FunctionIdentifierLibraryAliasElement.class);
    }

    public String getName() {

        FunctionIdentifierNameElement nameElement = getNameElement();

        return nameElement.getText();
    }

    public String getAlias() {

        FunctionIdentifierLibraryAliasElement aliasElement = getAliasElement();

        return aliasElement.getText();
    }

    public Boolean isDeclaredLocally() {

        return getFirstChild().getNode().getElementType().equals(K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zLexer
            .DOUBLECOLON));
    }

    public Boolean isDeclaredInCore() {

        return getChildren().length == 1;
    }
}
