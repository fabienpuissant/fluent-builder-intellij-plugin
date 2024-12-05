package io.fabienpuissant.fluent.builder.domain;


import io.fabienpuissant.fluent.builder.domain.outputcommand.BuilderCommandOutput;

public interface BuilderPort {
    void generateBuilder(BuilderCommandOutput commandOutput);
}
