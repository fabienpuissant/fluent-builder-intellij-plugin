package com.fluent.builder.domain.outputcommand;

public record CommandSignature(String value) {
    public CommandSignature {
        assert value != null;
    }
}
