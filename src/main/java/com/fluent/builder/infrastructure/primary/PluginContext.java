package com.fluent.builder.infrastructure.primary;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

public record PluginContext(Project project, Editor editor, PsiClass ownerClass) {}
