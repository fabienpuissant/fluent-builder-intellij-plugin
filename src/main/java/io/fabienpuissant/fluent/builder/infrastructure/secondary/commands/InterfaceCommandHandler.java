package io.fabienpuissant.fluent.builder.infrastructure.secondary.commands;

import io.fabienpuissant.fluent.builder.domain.outputcommand.CreateCommand;
import io.fabienpuissant.fluent.builder.domain.outputcommand.DeleteCommand;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;

public class InterfaceCommandHandler extends CommandHandler {

    @Override
    public void handle(PsiClass targetClass, CreateCommand command) {
        String signature = command.signature().value();
        String content = command.content().value().orElse("");
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiClass.getProject());

        String interfaceText = signature + " { " + content + " }";

        PsiClass dummyClass = factory.createClassFromText(interfaceText, null);

        PsiClass[] innerClasses = dummyClass.getInnerClasses();
        for (PsiClass inner : innerClasses) {
            if (inner.isInterface()) {
                targetClass.add(inner);
                return;
            }
        }

        throw new IllegalArgumentException("Failed to create interface from signature: " + signature);
    }

    @Override
    public void handle(PsiClass targetClass, DeleteCommand command) {
        PsiClass innerClassByName = psiClass.findInnerClassByName(command.name().name(), false);
        assert innerClassByName != null;
        innerClassByName.delete();
    }
}
