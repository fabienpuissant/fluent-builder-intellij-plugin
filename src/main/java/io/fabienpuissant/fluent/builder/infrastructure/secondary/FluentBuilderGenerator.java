package io.fabienpuissant.fluent.builder.infrastructure.secondary;

import io.fabienpuissant.fluent.builder.domain.BuilderPort;
import io.fabienpuissant.fluent.builder.domain.outputcommand.*;
import io.fabienpuissant.fluent.builder.infrastructure.secondary.commands.CommandHandler;
import io.fabienpuissant.fluent.builder.infrastructure.secondary.commands.CommandHandlerRegistry;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.codeStyle.CodeStyleManager;

public class FluentBuilderGenerator implements BuilderPort {

    private final PsiClass psiClass;

    public FluentBuilderGenerator() {
        PsiClassService psiClassService = ApplicationManager.getApplication().getService(PsiClassService.class);
        psiClass = psiClassService.getPsiClass().orElseThrow();
    }


    @Override
    public void generateBuilder(BuilderCommandOutput commandOutput) {
        for (BuilderCommand command : commandOutput.commands()) {
            if (command instanceof CreateCommand createCommand) {
                WriteCommandAction.runWriteCommandAction(psiClass.getProject(), () -> {
                    executeCreateCommand(createCommand);
                });
            } else if (command instanceof DeleteCommand deleteCommand) {
                WriteCommandAction.runWriteCommandAction(psiClass.getProject(), () -> {
                    executeDeleteCommand(deleteCommand);
                });
            } else {
                throw new UnsupportedOperationException("Unsupported command type: " + command.getClass().getName());
            }
        }
        WriteCommandAction.runWriteCommandAction(psiClass.getProject(), () -> {
            CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(psiClass.getProject());
            codeStyleManager.reformat(psiClass);
        });
    }

    private void executeCreateCommand(CreateCommand createCommand) {
        PsiClass targetClass = getTargetClass(createCommand);

        TargetType type = createCommand.type();
        CommandHandler handler = new CommandHandlerRegistry().getHandler(type);

        if (handler != null) {
            handler.handle(targetClass, createCommand);
        } else {
            throw new UnsupportedOperationException("No handler found for type: " + type);
        }
    }

    private void executeDeleteCommand(DeleteCommand deleteCommand) {
        PsiClass targetClass = getTargetClass(deleteCommand);

        TargetType type = deleteCommand.type();
        CommandHandler handler = new CommandHandlerRegistry().getHandler(type);

        if (handler != null) {
            handler.handle(targetClass, deleteCommand);
        } else {
            throw new UnsupportedOperationException("No handler found for type: " + type);
        }
    }

    private PsiClass getTargetClass(BuilderCommand command) {
        PsiClass targetClass = (command.scope() == CommandScope.BUILDER)
                ? getBuilderClass()
                : psiClass;

        if (targetClass == null) {
            throw new IllegalArgumentException("Target class is null. Cannot add elements.");
        }
        return targetClass;
    }

    private PsiClass getBuilderClass() {
        PsiClass builderClass = psiClass.findInnerClassByName("%sBuilder".formatted(psiClass.getName()), false);
        assert builderClass != null;
        return builderClass;
    }
}
