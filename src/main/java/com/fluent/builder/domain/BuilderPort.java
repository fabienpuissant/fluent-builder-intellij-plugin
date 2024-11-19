package com.fluent.builder.domain;


import com.fluent.builder.domain.outputcommand.BuilderCommandOutput;

public interface BuilderPort {
    void generateBuilder(BuilderCommandOutput commandOutput);
}
