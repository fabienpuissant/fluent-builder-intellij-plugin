package io.fabienpuissant.fluent.builder.infrastructure.secondary;

import com.intellij.openapi.components.Service;
import com.intellij.psi.PsiClass;


import java.util.Optional;

@Service
public final class PsiClassServiceImpl implements PsiClassService {
    private PsiClass psiClass;

    @Override
    public Optional<PsiClass> getPsiClass() {
        if (psiClass == null) return Optional.empty();
        return Optional.of(psiClass);
    }

    @Override
    public void setPsiClass(PsiClass psiClass) {
        this.psiClass = psiClass;
    }
}

