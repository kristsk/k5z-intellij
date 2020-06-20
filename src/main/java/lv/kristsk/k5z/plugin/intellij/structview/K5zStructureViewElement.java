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

package lv.kristsk.k5z.plugin.intellij.structview;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import lv.kristsk.k5z.plugin.intellij.K5zFileRoot;
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionDeclarationElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.PhtmlIncludeElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class K5zStructureViewElement implements StructureViewTreeElement, SortableTreeElement {

    private final PsiElement element;

    public K5zStructureViewElement(PsiElement element) {

        this.element = element;
    }

    @Override
    public Object getValue() {

        return element;
    }

    @Override
    public void navigate(boolean requestFocus) {

        if (element instanceof NavigationItem) {
            ((NavigationItem) element).navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {

        return element instanceof NavigationItem && ((NavigationItem) element).canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {

        return element instanceof NavigationItem && ((NavigationItem) element).canNavigateToSource();
    }

    @Override
    @NotNull
    public String getAlphaSortKey() {

        String sortKey = "";

        if (element instanceof PsiNamedElement) {
            String name = ((PsiNamedElement) element).getName();

            if (name != null) {
                sortKey = name;
            }
        }

        return sortKey;
    }

    @Override
    @NotNull
    public ItemPresentation getPresentation() {

        return new K5zItemPresentation(element);
    }

    @Override
    @NotNull
    public TreeElement[] getChildren() {

        if (element instanceof K5zFileRoot) {
            List<TreeElement> treeElements = new ArrayList<TreeElement>();

            Collection<PhtmlIncludeElement> phtmlIncludeElements = PsiTreeUtil.collectElementsOfType(element,
                PhtmlIncludeElement.class);
            for (ASTWrapperPsiElement element : phtmlIncludeElements) {
                treeElements.add(new K5zStructureViewElement(element));
            }

            Collection<FunctionDeclarationElement> functionDeclarationElements = PsiTreeUtil.collectElementsOfType
                (element, FunctionDeclarationElement.class);
            for (ASTWrapperPsiElement element : functionDeclarationElements) {
                treeElements.add(new K5zStructureViewElement(element));
            }

            return treeElements.toArray(new TreeElement[treeElements.size()]);
        }
        return EMPTY_ARRAY;
    }

    @Override
    public boolean equals(Object o) {

        boolean result;

        if (this == o) {
            result = true;
        }
        else if (o == null || getClass() != o.getClass()) {
            result = false;
        }
        else {
            K5zStructureViewElement that = (K5zStructureViewElement) o;

            result = !(element != null ? !element.equals(that.element) : that.element != null);
        }

        return result;
    }

    @Override
    public int hashCode() {

        return element != null ? element.hashCode() : 0;
    }
}
