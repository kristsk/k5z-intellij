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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import lv.kristsk.k5z.plugin.intellij.K5zFileRoot;
import lv.kristsk.k5z.plugin.intellij.K5zSdkType;
import lv.kristsk.k5z.plugin.intellij.psi.PsiElementFactory;
import org.apache.commons.lang.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ImportItemElement extends K5zPsiElement {

    public static class Factory implements PsiElementFactory {

        public static Factory INSTANCE = new Factory();

        @Override
        public PsiElement createElement(ASTNode node) {

            return new ImportItemElement(node);
        }
    }

    public ImportItemElement(@NotNull ASTNode node) {

        super(node);
    }

    public ImportLineElement getImportLineElement() {

        return PsiTreeUtil.getParentOfType(this, ImportLineElement.class);
    }

    public ImportItemLibraryAliasElement getAliasElement() {

        return PsiTreeUtil.findChildOfType(this, ImportItemLibraryAliasElement.class);
    }

    public ImportItemLibraryNameElement getNameElement() {

        return PsiTreeUtil.findChildOfType(this, ImportItemLibraryNameElement.class);
    }

    public String getLibraryName() {

        ImportItemLibraryNameElement nameElement = getNameElement();

        if (nameElement != null) {
            return nameElement.getName();
        }
        else {
            return getAliasElement().getName();
        }
    }

    public K5zFileRoot resolveToK5zFileRoot() {

        Collection<VirtualFile> libraryDirectories = K5zSdkType.getLibraryDirectories();

        libraryDirectories.add(getContainingFile().getContainingDirectory().getVirtualFile());

        return K5zSdkType.resolveToK5zFileRoot(
            libraryDirectories,
            getProject(),
            getRelativePathForImportedLibrary()
        );
    }

    private String getRelativePathForImportedLibrary() {

        ImportLinePathElement pathElement = getImportLineElement().getPathElement();

        String pathText = pathElement != null ? pathElement.getText() : "";
        pathText = StringEscapeUtils.unescapeJava(pathText);
        pathText = pathText.substring(1, pathText.length() - 1);

        return pathText + "/" + getLibraryName() + ".k5z";
    }
}
