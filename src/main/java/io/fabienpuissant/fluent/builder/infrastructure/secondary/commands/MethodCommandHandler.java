package io.fabienpuissant.fluent.builder.infrastructure.secondary.commands;

import io.fabienpuissant.fluent.builder.domain.outputcommand.CreateCommand;
import io.fabienpuissant.fluent.builder.domain.outputcommand.DeleteCommand;
import com.intellij.psi.*;

import java.util.Arrays;
import java.util.Objects;

public class MethodCommandHandler extends CommandHandler {

    @Override
    public void handle(PsiClass targetClass, CreateCommand command) {
        String signature = command.signature().value();
        String content = command.content().value().orElse("");
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        String methodText = signature + " {\n" + content + "\n}";
        PsiMethod method = factory.createMethodFromText(methodText, targetClass);
        targetClass.add(method);
    }

    @Override
    public void handle(PsiClass targetClass, DeleteCommand command) {
        PsiMethod[] psiMethods = psiClass.findMethodsByName(command.name().name(), false);
        Arrays.stream(psiMethods).filter(Objects::nonNull).forEach(PsiElement::delete);
    }
}
