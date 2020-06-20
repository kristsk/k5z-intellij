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

package lv.kristsk.k5z.plugin.intellij.psi.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionIdentifierLibraryAliasElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.ImportItemLibraryAliasElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LibraryAliasRef extends K5zWrappedPsiReference implements PsiPolyVariantReference {

    public LibraryAliasRef(ImportItemLibraryAliasElement element) {

        this.reference = new ImportItemAliasRef(element);
    }

    public LibraryAliasRef(FunctionIdentifierLibraryAliasElement element) {

        this.reference = new FunctionIdentifierAliasRef(element);
    }

    @Nullable
    @Override
    public PsiElement resolve() {

        PsiElement target = null;

        if (reference instanceof ImportItemAliasRef) {
            target = reference.resolve();
        }
        else if (reference instanceof FunctionIdentifierAliasRef) {
            target = reference.resolve();
        }

        return target;
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {

        if (getElement().equals(element)) {
            return true;
        }

        String thisName = null;
        if (reference instanceof ImportItemAliasRef) {
            ImportItemLibraryAliasElement aliasElement = (ImportItemLibraryAliasElement) reference.getElement();
            thisName = aliasElement.getName();
        }
        else if (reference instanceof FunctionIdentifierAliasRef) {
            FunctionIdentifierLibraryAliasElement aliasElement = (FunctionIdentifierLibraryAliasElement) reference
                .getElement();
            thisName = aliasElement.getName();
        }

        String targetName = null;
        if (element instanceof ImportItemLibraryAliasElement) {
            targetName = ((ImportItemLibraryAliasElement) element).getName();
        }
        else if (element instanceof FunctionIdentifierLibraryAliasElement) {
            targetName = ((FunctionIdentifierLibraryAliasElement) element).getName();
        }

        return thisName != null && thisName.equals(targetName);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {

        ResolveResult[] result = new ResolveResult[0];

        if (reference instanceof ImportItemAliasRef) {

            result = ((ImportItemAliasRef) reference).multiResolve(false);

        }
        else if (reference instanceof FunctionIdentifierAliasRef) {

            PsiElement importItemAliasElement = reference.resolve();

            if (importItemAliasElement != null) {
                result = new ResolveResult[]{new PsiElementResolveResult(importItemAliasElement)};
            }
        }

        return result;
    }
}
