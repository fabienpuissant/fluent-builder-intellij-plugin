package com.fluent.builder.infrastructure.secondary.commands;

import com.fluent.builder.domain.outputcommand.CreateCommand;
import com.fluent.builder.domain.outputcommand.DeleteCommand;
import com.fluent.builder.infrastructure.secondary.PsiClassService;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiClass;

public abstract class CommandHandler {

    protected final PsiClass psiClass;

    public CommandHandler() {
        PsiClassService psiClassService = ApplicationManager.getApplication().getService(PsiClassService.class);
        psiClass = psiClassService.getPsiClass().orElseThrow();
    }

    public abstract void handle(PsiClass targetClass, CreateCommand command);

    public abstract void handle(PsiClass targetClass, DeleteCommand command);
}
