package lv.kristsk.k5z.plugin.intellij.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;

public interface PsiElementFactory {

    PsiElement createElement(ASTNode node);
}
