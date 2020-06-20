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
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionHeaderNameElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionIdentifierNameElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class FunctionNameRef extends K5zWrappedPsiReference implements PsiPolyVariantReference {

    public FunctionNameRef(@NotNull FunctionHeaderNameElement element) {

        this.reference = new FunctionHeaderNameRef(element);
    }

    public FunctionNameRef(@NotNull FunctionIdentifierNameElement element) {

        this.reference = new FunctionIdentifierNameRef(element);
    }

    @Nullable
    @Override
    public PsiElement resolve() {

        PsiElement target;

        if (reference instanceof FunctionHeaderNameRef) {
            target = reference.resolve();
        }
        else if (reference instanceof FunctionIdentifierNameRef) {
            target = reference.resolve();
        }
        else {
            target = null;
        }

        return target;
    }

    @Override
    public boolean isReferenceTo(PsiElement element) {

        if (element.equals(getElement())) {
            return true;
        }

        String thisName = null;
        if (reference instanceof FunctionIdentifierNameRef) {
            FunctionIdentifierNameElement thisNameElement = ((FunctionIdentifierNameRef) reference).getElement();

            thisName = thisNameElement.getParent().getText();
        }
        else if (reference instanceof FunctionHeaderNameRef) {
            FunctionHeaderNameElement thisNameElement = ((FunctionHeaderNameRef) reference).getElement();

            thisName = "::" + thisNameElement.getName();
        }

        String targetName = null;
        if (element instanceof FunctionIdentifierNameElement) {
            targetName = element.getParent().getText();
        }
        else if (element instanceof FunctionHeaderNameElement) {
            targetName = "::" + ((FunctionHeaderNameElement) element).getName();
        }

        return thisName != null && thisName.equals(targetName);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean b) {

        ResolveResult[] result = new ResolveResult[0];

        if (reference instanceof FunctionHeaderNameRef) {

            Collection<PsiElementResolveResult> resolveResults = new ArrayList<>();

            PsiElement element = reference.resolve();
            if (element != null) {
                resolveResults.add(new PsiElementResolveResult(element));
            }

            result = resolveResults.toArray(new ResolveResult[0]);
        }
        else if (reference instanceof FunctionIdentifierNameRef) {
            result = ((FunctionIdentifierNameRef) reference).multiResolve(b);
        }

        return result;
    }
}
