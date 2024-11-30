package com.fluent.builder.infrastructure.secondary.commands;

import com.fluent.builder.domain.outputcommand.CreateCommand;
import com.fluent.builder.domain.outputcommand.DeleteCommand;
import com.intellij.psi.*;

import java.util.Arrays;
import java.util.Objects;

class ConstructorCommandHandler extends CommandHandler {

    @Override
    public void handle(PsiClass targetClass, CreateCommand command) {
        String signature = command.signature().value();
        String content = command.content().value().orElse("");
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        String constructorText = signature + " {\n" + content + "\n}";
        PsiMethod constructor = factory.createMethodFromText(constructorText, targetClass);
        if (constructor.isConstructor()) {
            targetClass.add(constructor);
        } else {
            throw new IllegalArgumentException("Invalid constructor signature: " + signature);
        }
    }

    @Override
    public void handle(PsiClass targetClass, DeleteCommand command) {
        PsiMethod[] constructors = targetClass.getConstructors();
        Arrays.stream(constructors).filter(Objects::nonNull)
                .filter(constructor -> Objects.requireNonNull(constructor.getSignature(PsiSubstitutor.EMPTY).toString()).contains("Builder")) //TODO A verifier
                .forEach(PsiElement::delete);
    }
}
