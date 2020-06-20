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

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionHeaderElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class K5zChooseByNameContributor implements ChooseByNameContributor {

    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {

        List<String> names = new ArrayList<>();

        for (FunctionHeaderElement element : getFunctionHeaderElements(project)) {

            ItemPresentation presentation = element.getPresentation();

            if (presentation != null) {
                names.add(presentation.getPresentableText());
            }
        }

        return names.toArray(new String[0]);
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {

        List<FunctionHeaderElement> functionHeaderElements = new ArrayList<>();

        for (FunctionHeaderElement element : getFunctionHeaderElements(project)) {

            ItemPresentation presentation = element.getPresentation();

            if (presentation != null && name.equals(presentation.getPresentableText())) {

                functionHeaderElements.add(element);
            }
        }

        return functionHeaderElements.toArray(new NavigationItem[0]);
    }

    private List<FunctionHeaderElement> getFunctionHeaderElements(Project project) {

        List<FunctionHeaderElement> result = new ArrayList<>();

        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(
            FileTypeIndex.NAME,
            K5zFileType.INSTANCE,
            GlobalSearchScope.allScope(project)
        );

        for (VirtualFile virtualFile : virtualFiles) {

            K5zFileRoot root = (K5zFileRoot) PsiManager.getInstance(project).findFile(virtualFile);
            if (root != null) {

                Collection<FunctionHeaderElement> elements = PsiTreeUtil.findChildrenOfType(root, FunctionHeaderElement.class);
                result.addAll(elements);
            }
        }

        return result;
    }
}
