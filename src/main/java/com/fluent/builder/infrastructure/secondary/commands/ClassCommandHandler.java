package com.fluent.builder.infrastructure.secondary.commands;

import com.fluent.builder.domain.outputcommand.CreateCommand;
import com.fluent.builder.domain.outputcommand.DeleteCommand;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;

public class ClassCommandHandler extends CommandHandler {

    @Override
    public void handle(PsiClass targetClass, CreateCommand command) {
        String signature = command.signature().value();
        String content = command.content().value().orElse("");
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        String methodText = "\t" + signature + " {\n" + content + "\n}";
        PsiClass newClass = factory.createImplicitClassFromText(methodText, targetClass);
        targetClass.add(newClass);
    }

    @Override
    public void handle(PsiClass targetClass, DeleteCommand command) {
        PsiClass innerClassByName = targetClass.findInnerClassByName(command.name().name(), false);
        assert innerClassByName != null;
        innerClassByName.delete();
    }
}
