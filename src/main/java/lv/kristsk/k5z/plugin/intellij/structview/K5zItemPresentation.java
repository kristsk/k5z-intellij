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

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import lv.kristsk.k5z.plugin.intellij.K5zIcons;
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionDeclarationElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.HeaderElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.K5zFileElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.PhtmlIncludeElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class K5zItemPresentation implements ItemPresentation {

    protected final PsiElement element;

    protected K5zItemPresentation(PsiElement element) {

        this.element = element;
    }

    @Nullable
    public String getLocationString() {

        return null;
    }

    @Override
    public String getPresentableText() {

        String presentableText;

        if (element instanceof K5zFileElement) {

            HeaderElement headerNode = PsiTreeUtil.findChildOfType(element, HeaderElement.class);
            if (headerNode != null) {
                presentableText = headerNode.getName();
            }
            else {
                presentableText = HeaderElement.DEFAULT_NAME;
            }
        }
        else if (element instanceof FunctionDeclarationElement) {
            presentableText = ((FunctionDeclarationElement) element).getName();
        }
        else if (element instanceof PhtmlIncludeElement) {
            presentableText = ((PhtmlIncludeElement) element).getName();
        }
        else {
            presentableText = element.getNode().getText();
        }

        return presentableText;
    }

    @Nullable
    public Icon getIcon(boolean open) {

        if (element instanceof PhtmlIncludeElement) {
            return K5zIcons.ICON2;
        }
        if (element instanceof FunctionDeclarationElement) {
            return K5zIcons.ICON1;
        }

        return K5zIcons.FILE;
    }
}
