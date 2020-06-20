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
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import lv.kristsk.k5z.plugin.intellij.K5zTokenTypes;
import lv.kristsk.k5z.plugin.intellij.parser.K5zParser;
import lv.kristsk.k5z.plugin.intellij.psi.PsiElementFactory;
import lv.kristsk.k5z.plugin.intellij.psi.reference.LibraryAliasRef;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ImportItemLibraryAliasElement extends K5zPsiElement implements PsiNamedElement {

    protected String name;

    public static class Factory implements PsiElementFactory {

        public static Factory INSTANCE = new Factory();

        @Override
        public PsiElement createElement(ASTNode node) {

            return new ImportItemLibraryAliasElement(node);
        }
    }

    public ImportItemLibraryAliasElement(@NotNull ASTNode node) {

        super(node);
    }

    @Override
    public PsiReference getReference() {

        return new LibraryAliasRef(this);
    }

    @Override
    public String getName() {

        if (name != null) {
            return name;
        }
        else {
            return getText();
        }
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {

        PsiElement firstChild = this.getFirstChild();

        PsiElement newNameLeaf = K5zPsiUtils.createLeafFromText(
            getProject(),
            getContext(),
            name,
            K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zParser.CAMEL_ID)
        );

        firstChild.replace(newNameLeaf);

        this.name = name;

        return this;
    }

    public ImportLineElement getLineElement() {

        return PsiTreeUtil.getParentOfType(this, ImportLineElement.class);
    }

    public ImportItemElement getItemElement() {

        return PsiTreeUtil.getParentOfType(this, ImportItemElement.class);
    }
}
