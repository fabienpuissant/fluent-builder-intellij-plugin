package com.fluent.builder.application;

import com.fluent.builder.domain.*;
import com.fluent.builder.infrastructure.primary.FluentBuilderParametersMapper;
import com.fluent.builder.infrastructure.primary.PluginContext;
import com.fluent.builder.infrastructure.secondary.FluentBuilderGenerator;
import com.intellij.psi.PsiField;

import java.util.List;

public class FluentBuilderApplicationService {

    private final BuilderGeneratorDomainService builder;

    public FluentBuilderApplicationService() {
        this.builder = new BuilderGeneratorDomainService(new FluentBuilderGenerator());
    }

    public void generateBuilder(PluginContext context, List<PsiField> mandatoryFields, List<PsiField> optionalFields) {
        builder.generateBuilder(FluentBuilderParametersMapper.toDomain(context, mandatoryFields, optionalFields));
    }

}
