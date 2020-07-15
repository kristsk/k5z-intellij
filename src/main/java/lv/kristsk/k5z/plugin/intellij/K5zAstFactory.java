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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.DefaultASTFactoryImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import lv.kristsk.k5z.plugin.intellij.parser.K5zParser;
import lv.kristsk.k5z.plugin.intellij.psi.PsiElementFactory;
import lv.kristsk.k5z.plugin.intellij.psi.element.*;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class K5zAstFactory extends DefaultASTFactoryImpl {

    private static final Map<IElementType, PsiElementFactory> factories = new HashMap<>();

    static {
        List<RuleIElementType> ruleTypes = K5zTokenTypes.RULE_ELEMENT_TYPES;

        factories.put(ruleTypes.get(K5zParser.RULE_k5zFile), K5zFileElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_header), HeaderElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_functionDeclarations), FunctionDeclarationsElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_functionDeclaration), FunctionDeclarationElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_functionIdentifier), FunctionIdentifierElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_phtmlIncludeLine), PhtmlIncludeElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_importLine), ImportLineElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_importLinePath), ImportLinePathElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_importItem), ImportItemElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_importItemAlias), ImportItemLibraryAliasElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_importItemLibrary), ImportItemLibraryNameElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_array), ArrayElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_variable), VariableElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_parameterName), ParameterNameElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_functionHeader), FunctionHeaderElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_functionHeaderName), FunctionHeaderNameElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_functionIdentifierName), FunctionIdentifierNameElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_functionIdentifierAlias), FunctionIdentifierLibraryAliasElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_statementBlock), StatementBlockElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_phtmlIncludeLine), PhtmlIncludeLineElement.Factory.INSTANCE);
        factories.put(ruleTypes.get(K5zParser.RULE_phtmlIncludeLine), PhtmlIncludeFilenameElement.Factory.INSTANCE);

        //System.out.println("factories: " + factories);
    }

    @NotNull
    public CompositeElement createComposite(@NotNull IElementType type) {

        return super.createComposite(type);
    }

    @NotNull
    @Override
    public LeafElement createLeaf(@NotNull IElementType type, @NotNull CharSequence text) {

        return new LeafPsiElement(type, text);
    }

    public static PsiElement createInternalParseTreeNode(ASTNode node) {

        PsiElement psiElement;
        IElementType tokenType = node.getElementType();

//        System.out.println("createInternalParseTreeNode: " + tokenType);

        PsiElementFactory factory = factories.get(tokenType);
        if (factory != null) {
            psiElement = factory.createElement(node);
        }
        else {
            psiElement = new ASTWrapperPsiElement(node);
        }

        return psiElement;
    }
}
