package com.fluent.builder.infrastructure.secondary;

import com.intellij.psi.PsiClass;

import java.util.Optional;

public interface PsiClassService {

    Optional<PsiClass> getPsiClass();

    void setPsiClass(PsiClass psiClass);
}
