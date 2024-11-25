package com.fluent.builder.domain.outputcommand;

public abstract class BuilderCommand {

    protected CommandSignature signature;

    public BuilderCommand(CommandSignature signature) {
        this.signature = signature;
    }
}
