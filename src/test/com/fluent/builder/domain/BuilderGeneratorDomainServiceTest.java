package com.fluent.builder.domain;


import com.fluent.builder.domain.outputcommand.*;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static com.fluent.builder.domain.FluentBuilderFixtures.CLASS_NAME;
import static com.fluent.builder.domain.FluentBuilderFixtures.emptyBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BuilderGeneratorDomainServiceTest {

    @Mock
    private FluentBuilderGenerator builderPort;

    private BuilderGeneratorDomainService builder;

    @Captor
    private ArgumentCaptor<BuilderCommandOutput> commandOutput;

    @BeforeEach
    void setUp() {
        this.builder = new BuilderGeneratorDomainService(builderPort);
    }

    @Test
    void shouldAddBuilderClassIfNotExists() {
        builder.generateBuilder(emptyBuilder());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(CreateCommand.builder()
                .signature(new CommandSignature("public static final SutBuilder"))
                .content(new CommandContent(null))
        );
    }


}