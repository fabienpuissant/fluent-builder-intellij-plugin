package io.fabienpuissant.fluent.builder.infrastructure.secondary.commands;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import io.fabienpuissant.fluent.builder.domain.outputcommand.CreateCommand;
import io.fabienpuissant.fluent.builder.domain.outputcommand.DeleteCommand;

class FieldCommandHandler extends CommandHandler {

  @Override
  public void handle(PsiClass targetClass, CreateCommand command) {
    String signature = command.signature().value();
    PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiClass.getProject());
    PsiField field = factory.createFieldFromText(signature, targetClass);
    targetClass.add(field);
  }

  @Override
  public void handle(PsiClass targetClass, DeleteCommand command) {
    PsiField psiField = targetClass.findFieldByName(command.name().name(), false);
    assert psiField != null;
    psiField.delete();
  }
}
