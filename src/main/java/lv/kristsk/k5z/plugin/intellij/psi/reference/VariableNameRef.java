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
import com.intellij.psi.util.PsiTreeUtil;
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionDeclarationElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.K5zPsiUtils;
import lv.kristsk.k5z.plugin.intellij.psi.element.ParameterNameElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.VariableElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VariableNameRef extends K5zWrappedPsiReference {

    public VariableNameRef(@NotNull VariableElement element) {

        reference = new VariableRef(element);
    }

    public VariableNameRef(@NotNull ParameterNameElement element) {

        reference = new ParameterNameRef(element);
    }

    @Nullable
    @Override
    public PsiElement resolve() {

        PsiElement target;

        if (reference instanceof ParameterNameRef) {
            target = reference.resolve();
        }
        else if (reference instanceof VariableRef) {

            FunctionDeclarationElement functionDeclarationNode = PsiTreeUtil.getContextOfType(getElement(), FunctionDeclarationElement.class);

            target = K5zPsiUtils.findParameterNameElementInFunctionDeclarationNode(
                ((VariableRef) reference).getElement().getName(),
                functionDeclarationNode
            );

            if (target == null) {
                target = reference.resolve();
            }
        }
        else {
            target = null;
        }

        return target;
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {

        FunctionDeclarationElement d1 = PsiTreeUtil.getContextOfType(element, FunctionDeclarationElement.class);
        FunctionDeclarationElement d2 = PsiTreeUtil.getContextOfType(reference.getElement(), FunctionDeclarationElement.class);

        if (d1 != null && !d1.equals(d2)) {
            return false;
        }

        String elementName = getElementName(element);
        String referencedElementName = getReferencedElementName();

        return elementName != null && elementName.equals(referencedElementName);
    }

    private String getElementName(PsiElement element) {

        String elementName = "<n/a>";

        if (element instanceof VariableElement) {
            elementName = ((VariableElement) element).getName();
        }
        else if (element instanceof ParameterNameElement) {
            elementName = ((ParameterNameElement) element).getName();
        }

        return elementName;
    }

    private String getReferencedElementName() {

        String referencedElementName = "<n/a>";

        if (reference instanceof VariableRef) {
            referencedElementName = ((VariableRef) reference).getElement().getName();
        }
        else if (reference instanceof ParameterNameRef) {
            referencedElementName = ((ParameterNameRef) reference).getElement().getName();
        }

        return referencedElementName;
    }

    @NotNull
    @Override
    public Object[] getVariants() {

        return new Object[0];
    }
}
