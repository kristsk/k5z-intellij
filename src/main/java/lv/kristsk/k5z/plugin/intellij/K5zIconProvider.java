package lv.kristsk.k5z.plugin.intellij;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IconProvider;
import com.intellij.psi.PsiElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.FunctionHeaderNameElement;
import lv.kristsk.k5z.plugin.intellij.psi.element.VariableElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class K5zIconProvider extends IconProvider {

    public Icon getIcon(@NotNull PsiElement psiElement, int flags) {

        System.out.println("Looking for icon for: " + psiElement);

        if (psiElement instanceof VariableElement) {
            return AllIcons.Nodes.Variable;
        }
        else if (psiElement instanceof FunctionHeaderNameElement) {
            return AllIcons.Nodes.Method;
        }

        return null;
    }

}
