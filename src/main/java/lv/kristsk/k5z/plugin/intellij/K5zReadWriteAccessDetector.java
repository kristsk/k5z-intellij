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

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import lv.kristsk.k5z.plugin.intellij.parser.K5zParser;
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionHeaderNameElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionIdentifierNameElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.ParameterNameElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.VariableElement;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class K5zReadWriteAccessDetector extends ReadWriteAccessDetector {

    protected static List<RuleIElementType> ruleElementTypes = K5zTokenTypes.RULE_ELEMENT_TYPES;

    protected Boolean elementHasRuleType(PsiElement element, int ruleId) {

        IElementType elementType = element.getNode().getElementType();
        return elementType.equals(ruleElementTypes.get(ruleId));
    }

    @Override
    public boolean isReadWriteAccessible(@NotNull PsiElement element) {

        return element instanceof VariableElement ||
               element instanceof ParameterNameElement ||
               element instanceof FunctionHeaderNameElement ||
               element instanceof FunctionIdentifierNameElement;
    }

    @Override
    public boolean isDeclarationWriteAccess(@NotNull PsiElement element) {

        if (element instanceof FunctionHeaderNameElement) {
            return true;
        }

        PsiElement elementParent = element.getParent();

        if (elementParent == null) {
            return false;
        }

        if (elementHasRuleType(elementParent, K5zParser.RULE_array)) {
            elementParent = elementParent.getParent();
        }

        if (elementParent == null) {
            return false;
        }

        return elementHasRuleType(elementParent, K5zParser.RULE_assignExpressionLeftSide);

    }

    @NotNull
    @Override
    public Access getReferenceAccess(@NotNull PsiElement referencedElement, PsiReference reference) {

        PsiElement element = reference.getElement();
        return getExpressionAccess(element);
    }

    @NotNull
    @Override
    public Access getExpressionAccess(@NotNull PsiElement expression) {

        if (isDeclarationWriteAccess(expression)) {
            return Access.Write;
        }

        PsiElement expressionParent = expression.getParent();

        if (elementHasRuleType(expressionParent, K5zParser.RULE_assignExpression)) {
            if (expression.equals(expressionParent.getFirstChild())) {
                return Access.Write;
            }
        }

        return Access.Read;
    }
}
