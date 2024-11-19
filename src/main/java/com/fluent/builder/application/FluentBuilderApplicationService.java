package com.fluent.builder.application;

import com.fluent.builder.domain.BuilderClass;
import com.fluent.builder.domain.BuilderGeneratorDomainService;
import com.fluent.builder.infrastructure.primary.FieldData;
import com.fluent.builder.infrastructure.secondary.FluentBuilderGenerator;
import com.intellij.psi.PsiClass;

import java.util.List;

public class FluentBuilderApplicationService {

    private final BuilderGeneratorDomainService builder;

    public FluentBuilderApplicationService() {
        this.builder = new BuilderGeneratorDomainService(new FluentBuilderGenerator());
    }

    public void generateBuilder(PsiClass targetClass, List<FieldData> fields) {
        //TODO convert target class and fields into
        BuilderClass builderClass = new BuilderClass();
        builder.generateBuilder(builderClass);
    }
}
