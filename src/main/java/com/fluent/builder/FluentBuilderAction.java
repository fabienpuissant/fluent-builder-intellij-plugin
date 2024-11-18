package com.fluent.builder;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;

public class FluentBuilderAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);

        if (!(psiFile instanceof com.intellij.psi.impl.source.PsiJavaFileImpl)) {
            Messages.showErrorDialog("No valid Java file found.", "Error");
            return;
        }

        PsiClass psiClass = ((com.intellij.psi.impl.source.PsiJavaFileImpl) psiFile).getClasses()[0];
        if (psiClass == null) {
            Messages.showErrorDialog("No class found in the current file.", "Error");
            return;
        }

        FluentBuilderDialog dialog = new FluentBuilderDialog(psiClass);
        if (dialog.showAndGet()) {
            dialog.generateBuilder();
        }
    }
}
