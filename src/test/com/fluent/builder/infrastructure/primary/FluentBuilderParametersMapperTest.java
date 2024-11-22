package com.fluent.builder.infrastructure.primary;


import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.util.PsiUtil;
import com.intellij.testFramework.LightPlatformTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FluentBuilderParametersMapperTest extends LightPlatformTestCase {

    private Project project = mock(Project.class);

    private Editor editor = mock(Editor.class);

    private PsiClass psiClass = mock(PsiClass.class);

    @Test
    void shouldMapIsBuilderExistToFalseWhenNoBuilder() {
        PluginContext pluginContext = new PluginContext(project, editor, psiClass);
    }

}