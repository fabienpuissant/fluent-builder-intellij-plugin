package com.fluent.builder.application;

import com.fluent.builder.domain.BuilderClass;
import com.fluent.builder.domain.BuilderGeneratorDomainService;
import com.fluent.builder.infrastructure.primary.Fields;
import com.fluent.builder.infrastructure.primary.PluginContext;
import com.fluent.builder.infrastructure.secondary.FluentBuilderGenerator;

public class FluentBuilderApplicationService {

    private final BuilderGeneratorDomainService builder;

    public FluentBuilderApplicationService() {
        this.builder = new BuilderGeneratorDomainService(new FluentBuilderGenerator());
    }

    public void generateBuilder(PluginContext context, Fields fields) {
        //TODO convert target class and fields into
        BuilderClass builderClass = new BuilderClass();
        builder.generateBuilder(builderClass);
    }

}
