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

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import lv.kristsk.k5z.plugin.intellij.psi.element.StatementBlockElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class K5zFoldingBuilder extends K5zTokenTypes implements FoldingBuilder, DumbAware {

    @NotNull
    public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {

        List<FoldingDescriptor> descriptors = new ArrayList<>();

        Collection<StatementBlockElement> statementBlocks = PsiTreeUtil.findChildrenOfType(node.getPsi(), StatementBlockElement.class);

        for (StatementBlockElement element : statementBlocks) {
            if (isMultiline(element)) {
                addDescriptorStartFromChildNode(descriptors, element.getNode());
            }
        }

        return descriptors.toArray(new FoldingDescriptor[0]);
    }

    private void addDescriptorStartFromChildNode(List<FoldingDescriptor> descriptors, ASTNode node) {

        ASTNode startNode = findChildOfText(node, "{");
        if (startNode != null) {
            int end = node.getStartOffset() + node.getTextLength();
            descriptors.add(new FoldingDescriptor(node, new TextRange(startNode.getStartOffset(), end)));
        }
    }

    private static ASTNode findChildOfText(ASTNode parent, String text) {

        ASTNode child = parent.getFirstChildNode();
        while (child != null) {
            if (child.getText().equals(text)) {
                break;
            }
            ASTNode sub = findChildOfText(child, text);
            if (sub != null) {
                return sub;
            }
            child = child.getTreeNext();
        }
        return child;
    }

    public String getPlaceholderText(@NotNull ASTNode node) {

        return "{ ... }";
    }

    public boolean isCollapsedByDefault(@NotNull ASTNode node) {

        return false;
    }

    private static boolean isMultiline(PsiElement element) {

        String text = element.getText();
        return text.contains("\n") || text.contains("\r") || text.contains("\r\n");
    }
}
