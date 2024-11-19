package com.fluent.builder.domain;


import com.fluent.builder.infrastructure.secondary.FluentBuilderGenerator;
import com.fluent.builder.infrastructure.secondary.ItemsToBuild;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BuilderGeneratorDomainServiceTest {

    @Mock
    private FluentBuilderGenerator builderPort;

    private BuilderGeneratorDomainService builder;

    @BeforeEach
    void setUp() {
        this.builder = new BuilderGeneratorDomainService(builderPort);
    }

    @Test
    void test() {
        builder.generateBuilder(new BuilderClass());
        verify(builderPort).generateBuilder(new BuilderClass());
    }

}