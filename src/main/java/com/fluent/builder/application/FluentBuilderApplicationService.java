package com.fluent.builder.application;

import com.fluent.builder.domain.Fields;
import com.fluent.builder.domain.FluentBuilderParameters;
import com.fluent.builder.domain.BuilderGeneratorDomainService;
import com.fluent.builder.domain.PluginContext;
import com.fluent.builder.infrastructure.secondary.FluentBuilderGenerator;
import com.intellij.psi.PsiField;

import java.util.List;

public class FluentBuilderApplicationService {

    private final BuilderGeneratorDomainService builder;

    public FluentBuilderApplicationService() {
        this.builder = new BuilderGeneratorDomainService(new FluentBuilderGenerator());
    }

    public void generateBuilder(PluginContext context, List<PsiField> mandatoryFields, List<PsiField> optionalFields) {
        builder.generateBuilder(FluentBuilderParameters.builder()
                .context(context)
                .mandatoryParameters(new Fields(mandatoryFields))
                .optionalParameters(new Fields(optionalFields))
        );
    }

}
