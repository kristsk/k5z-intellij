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

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import lv.kristsk.k5z.plugin.intellij.K5zLanguage;
import org.jetbrains.annotations.NotNull;

public class K5zPsiUtils {

    public static PsiElement findFirstVariableElementInFunctionDeclarationNode(final String variableName, VariableElement element) {

        FunctionDeclarationElement functionDeclarationNode = PsiTreeUtil.getContextOfType(element, FunctionDeclarationElement.class);
        return findFirstVariableElementByName(variableName, functionDeclarationNode);
    }

    public static PsiElement findFirstVariableElementByName(final String variableName, FunctionDeclarationElement functionDeclarationNode) {

        PsiElementFilter defaultNode = new PsiElementFilter() {
            @Override
            public boolean isAccepted(PsiElement element) {

                PsiElement testNode = element.getFirstChild();

                if (testNode != null) {
                    if (element instanceof VariableElement) {
                        String testNodeText = testNode.getText();
                        return variableName.equals(testNodeText);
                    }
                    else {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        };

        PsiElement[] variableNodes = PsiTreeUtil.collectElements(functionDeclarationNode, defaultNode);

        if (variableNodes.length > 0) {
            return variableNodes[0];
        }
        else {
            return null;
        }
    }

    public static PsiElement findParameterNameElementInFunctionDeclarationNode(final String parameterName, FunctionDeclarationElement functionDeclarationNode) {

        PsiElementFilter nodeFilter = new PsiElementFilter() {
            @Override
            public boolean isAccepted(@NotNull PsiElement element) {

                if (element instanceof ParameterNameElement) {
                    String testNodeText = element.getFirstChild().getText();
                    return parameterName.equals(testNodeText);
                }
                else {
                    return false;
                }
            }
        };

        PsiElement[] parameterNodes = PsiTreeUtil.collectElements(functionDeclarationNode, nodeFilter);

        if (parameterNodes.length > 0) {
            return parameterNodes[0];
        }
        else {
            return null;
        }
    }

    public static PsiElement createLeafFromText(Project project, PsiElement context, String text, IElementType type) {

        PsiFileFactoryImpl factory = (PsiFileFactoryImpl) PsiFileFactory.getInstance(project);
        PsiElement el = factory.createElementFromText(text,
            K5zLanguage.INSTANCE,
            type,
            context
        );
        return PsiTreeUtil.getDeepestFirst(el);
    }

    public static PsiElement findFirstFunctionHeaderNameElementByName(final String functionName, K5zFileElement fileElement) {

        PsiElementFilter filter = new PsiElementFilter() {
            @Override
            public boolean isAccepted(PsiElement element) {

                PsiElement testNode = element.getFirstChild();

                if (testNode != null) {
                    if (element instanceof FunctionHeaderNameElement) {
                        String testText = testNode.getText();
                        return functionName.equals(testText);
                    }
                    else {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        };

        PsiElement[] elements = PsiTreeUtil.collectElements(fileElement, filter);

        if (elements.length > 0) {
            return elements[0];
        }
        else {
            return null;
        }
    }
}
