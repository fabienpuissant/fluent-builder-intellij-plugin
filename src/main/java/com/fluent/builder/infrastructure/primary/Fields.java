package com.fluent.builder.infrastructure.primary;

import com.intellij.psi.PsiField;

import java.util.List;

public record Fields(List<PsiField> mandatoryFields, List<PsiField> optionalFields) {
    public Fields {
        if (mandatoryFields == null || optionalFields == null) throw new IllegalStateException();
    }
}
