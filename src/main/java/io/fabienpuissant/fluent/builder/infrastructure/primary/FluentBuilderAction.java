package io.fabienpuissant.fluent.builder.infrastructure.primary;

import io.fabienpuissant.fluent.builder.infrastructure.secondary.PsiClassService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

public class FluentBuilderAction extends AnAction {

    private final PsiClassService psiClassService;

    public FluentBuilderAction() {
        this.psiClassService = ApplicationManager.getApplication().getService(PsiClassService.class);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(PlatformDataKeys.EDITOR);

        if (project == null || editor == null) {
            return;
        }

        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        PsiClass psiClass = PsiTreeUtil.findChildOfType(psiFile, PsiClass.class);

        if (psiClass == null) {
            return;
        }
        psiClassService.setPsiClass(psiClass);

        DialogFieldSelection dialog = new DialogFieldSelection(new PluginContext(project, editor, psiClass));
        dialog.show();
    }
}
