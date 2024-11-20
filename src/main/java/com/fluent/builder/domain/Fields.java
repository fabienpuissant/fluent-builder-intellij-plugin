package com.fluent.builder.domain;

import com.intellij.psi.PsiField;

import java.util.List;

public record Fields(List<PsiField> fields) {
    public Fields {
        if(fields == null) throw new IllegalStateException();
    }
}
