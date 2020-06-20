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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import lv.kristsk.k5z.plugin.intellij.K5zFileRoot;
import lv.kristsk.k5z.plugin.intellij.K5zSdkType;
import lv.kristsk.k5z.plugin.intellij.K5zTokenTypes;
import lv.kristsk.k5z.plugin.intellij.parser.K5zLexer;
import lv.kristsk.k5z.plugin.intellij.parser.K5zParser;
import lv.kristsk.k5z.plugin.intellij.psi.element.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class FunctionIdentifierNameRef extends PsiReferenceBase<FunctionIdentifierNameElement>
    implements PsiPolyVariantReference {

    public FunctionIdentifierNameRef(FunctionIdentifierNameElement element) {

        super(element);
    }

    @Nullable
    @Override
    public PsiElement resolve() {

        return null;
    }

    private PsiElement resolveInThisFile() {

        final String targetName = getElement().getName();

        if (targetName == null) {
            return null;
        }

        PsiElementFilter filter = element -> element instanceof FunctionHeaderNameElement && targetName.equals(((FunctionHeaderNameElement) element).getName());

        PsiElement[] nameElements = PsiTreeUtil.collectElements(getElement().getContainingFile(), filter);

        if (nameElements.length != 1) {
            return null;
        }

        return nameElements[0];
    }

    private PsiElement resolveInOtherFile(FunctionIdentifierLibraryAliasElement identifierAlias) {

        PsiReference aliasReference = identifierAlias.getReference();

        if (aliasReference == null) {
            return null;
        }

        ImportItemLibraryAliasElement importAlias = (ImportItemLibraryAliasElement) aliasReference.resolve();

        if (importAlias == null) {
            return null;
        }

        K5zFileRoot k5zFileRoot = importAlias.getItemElement().resolveToK5zFileRoot();

        if (k5zFileRoot != null) {
            return resolveInFile(k5zFileRoot);
        }
        else {
            return null;
        }
    }

    private PsiElement resolveInFile(K5zFileRoot k5zFileRoot) {

        final String targetName = getElement().getName();

        if (targetName == null) {
            return null;
        }

        PsiElementFilter filter = element -> element instanceof FunctionHeaderNameElement && targetName.equals(((FunctionHeaderNameElement) element).getName());

        PsiElement[] functionHeaderNameElements = PsiTreeUtil.collectElements(k5zFileRoot, filter);

        if (functionHeaderNameElements.length != 1) {
            return null;
        }

        return functionHeaderNameElements[0];
    }

    @NotNull
    @Override
    public Object[] getVariants() {

        return new Object[0];
    }

    @NotNull
    @Override
    public TextRange getRangeInElement() {

        return new TextRange(0, getElement().getText().length());
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {

        Project project = getElement().getProject();
        PsiElement leafFromText = K5zPsiUtils.createLeafFromText(project,
            getElement().getContext(),
            newElementName,
            K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zParser.CAMEL_ID)
        );

        if (!leafFromText.getNode().getElementType().equals(K5zTokenTypes.TOKEN_ELEMENT_TYPES.get(K5zLexer.CAMEL_ID)
        )) {
            throw new IncorrectOperationException("Bad function name!");
        }

        getElement().getFirstChild().replace(leafFromText);

        return getElement();
    }

    @NotNull
    @Override
    public String getCanonicalText() {

        return getElement().getFirstChild().getText();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean b) {

        Collection<PsiElement> elements = new ArrayList<>();

        PsiElement inThisFile = resolveInThisFile();

        if (inThisFile != null) {
            elements.add(inThisFile);
        }
        else {
            FunctionIdentifierLibraryAliasElement aliasElement = getElement().getIdentifierElement().getAliasElement();

            if (aliasElement != null) {
                PsiElement inOtherFile = resolveInOtherFile(aliasElement);
                if (inOtherFile != null) {
                    elements.add(inOtherFile);
                }
            }
            else {
                K5zFileRoot coreFile = K5zSdkType.resolveToK5zFileRoot(
                    K5zSdkType.getLibraryDirectories(),
                    getElement().getProject(),
                    "SystemLibraries/Core.k5z"
                );

                if (coreFile != null) {
                    PsiElement inCoreFile = resolveInFile(coreFile);

                    if (inCoreFile != null) {
                        elements.add(inCoreFile);
                    }
                }
            }
        }

        return PsiElementResolveResult.createResults(elements);
    }
}
