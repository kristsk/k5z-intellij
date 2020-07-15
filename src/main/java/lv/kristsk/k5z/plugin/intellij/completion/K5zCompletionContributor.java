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

package lv.kristsk.k5z.plugin.intellij.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionDeclarationElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionDeclarationsElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionHeaderNameElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.VariableElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class K5zCompletionContributor extends CompletionContributor {

    public static final String DUMMY_IDENTIFIER = CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED;

    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {

        PsiElement originalPosition = parameters.getOriginalPosition();

        PsiElement currentDeclaration = PsiTreeUtil.findFirstParent(originalPosition, new Condition<PsiElement>() {
            @Override
            public boolean value(PsiElement element) {

                return element instanceof FunctionDeclarationElement;
            }
        });

        if (currentDeclaration != null) {
            Collection<VariableElement> variables = PsiTreeUtil.findChildrenOfType(currentDeclaration, VariableElement.class);
            for (VariableElement variable : variables) {
                System.out.println("Adding variable: " + variable.getName());
//                result.addElement(LookupElementBuilder.create(Objects.requireNonNull(variable.getName())));
                result.addElement(LookupElementBuilder.createWithIcon(variable));
            }
        }

        PsiElement declarationsRoot = PsiTreeUtil.findFirstParent(originalPosition, new Condition<PsiElement>() {
            @Override
            public boolean value(PsiElement element) {

                return element instanceof FunctionDeclarationsElement;
            }
        });

        

        if (declarationsRoot != null) {
            Collection<FunctionHeaderNameElement> declarations = PsiTreeUtil.findChildrenOfType(declarationsRoot, FunctionHeaderNameElement.class);
            for (FunctionHeaderNameElement declaration : declarations) {
                System.out.println("Adding declaration: " + declaration.getName());
//                result.addElement(LookupElementBuilder.create(Objects.requireNonNull(declaration.getName())));
                result.addElement(LookupElementBuilder.createWithIcon(declaration));
            }
        }
    }

//    public K5zCompletionContributor() {
//        CompletionProvider<CompletionParameters> variableCompletions = new CompletionProvider<CompletionParameters>() {
//            @Override
//            protected void addCompletions(
//                @NotNull CompletionParameters parameters,
//                @NotNull ProcessingContext context,
//                @NotNull CompletionResultSet result) {
//
//                PsiElement originalPosition = parameters.getOriginalPosition();
//
//                PsiElement declaration = PsiTreeUtil.findFirstParent(originalPosition, new Condition<PsiElement>() {
//                    @Override
//                    public boolean value(PsiElement element) {
//
//                        return element instanceof FunctionDeclarationElement;
//                    }
//                });
//
//                if (declaration != null) {
//
//                    Collection<VariableElement> variables = PsiTreeUtil.findChildrenOfType(declaration, VariableElement.class);
//
//                    for (VariableElement variable : variables) {
//                        result.addElement(LookupElementBuilder.createWithSmartPointer(variable.getNameNode().getText(), originalPosition));
//                    }
//                }
//            }
//        };
//
//        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), variableCompletions);
//    }

    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {


        context.setDummyIdentifier("a");
    }
}
