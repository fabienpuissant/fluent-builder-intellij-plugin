package com.fluent.builder.domain;



public class BuilderGeneratorDomainService {

    private final BuilderPort builderPort;

    public BuilderGeneratorDomainService(BuilderPort builderPort) {
        this.builderPort = builderPort;
    }

    public void generateBuilder(FluentBuilderParameters fluentBuilderParameters) {
        builderPort.generateBuilder(null);
    }
}
