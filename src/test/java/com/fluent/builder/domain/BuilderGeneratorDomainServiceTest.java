package com.fluent.builder.domain;


import com.fluent.builder.infrastructure.secondary.FluentBuilderGenerator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.impl.ProjectImpl;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.psi.util.PsiUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BuilderGeneratorDomainServiceTest {

    @Mock
    private FluentBuilderGenerator builderPort;

    private BuilderGeneratorDomainService builder;

    private PsiFileFactory psiFileFactory;

    @BeforeEach
    void setUp() {
        this.builder = new BuilderGeneratorDomainService(builderPort);
    }

    @Test
    void shouldAddBuilderClassIfNotExists() throws IOException {
        builder.generateBuilder(null);

        emptyClass();

        verify(builderPort).generateBuilder(null);
    }

    private PsiClass emptyClass() throws IOException {
        String data = new String(getClass().getResourceAsStream("/EmptyClass.java").readAllBytes());
        PsiFile psiFile = psiFileFactory.createFileFromText("EmptyClass.java", data);
        return PsiUtil.getTopLevelClass(psiFile);
    }

}