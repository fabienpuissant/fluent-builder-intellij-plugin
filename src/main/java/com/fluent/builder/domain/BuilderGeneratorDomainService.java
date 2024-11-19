package com.fluent.builder.domain;



public class BuilderGeneratorDomainService {

    private final BuilderPort builderPort;

    public BuilderGeneratorDomainService(BuilderPort builderPort) {
        this.builderPort = builderPort;
    }

    public void generateBuilder(BuilderClass builderClass) {
        builderPort.generateBuilder();
    }
}
