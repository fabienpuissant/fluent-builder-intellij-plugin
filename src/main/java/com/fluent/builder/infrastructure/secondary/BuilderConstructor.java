package com.fluent.builder.infrastructure.secondary;

import com.intellij.psi.PsiField;

import java.util.List;

public record BuilderConstructor(List<PsiField> parameters) {
}
