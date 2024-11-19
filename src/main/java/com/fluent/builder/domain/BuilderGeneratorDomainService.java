package com.fluent.builder.domain;


import com.fluent.builder.infrastructure.secondary.FluentBuilderGenerator;

public class BuilderGeneratorDomainService {

    private final BuilderPort builderPort;

    public BuilderGeneratorDomainService(BuilderPort builderPort) {
        this.builderPort = builderPort;
    }

    public void generateBuilder(BuilderClass builderClass) {
        builderPort.generateBuilder();
    }
}
