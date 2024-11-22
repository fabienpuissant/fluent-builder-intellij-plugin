package com.fluent.builder.infrastructure.primary;



import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FluentBuilderParametersMapperTest  {

    private Project project = mock(Project.class);

    private Editor editor = mock(Editor.class);

    private PsiClass psiClass = mock(PsiClass.class);

    @Test
    void shouldMapIsBuilderExistToFalseWhenNoBuilder() {
        PluginContext pluginContext = new PluginContext(project, editor, psiClass);
    }

}