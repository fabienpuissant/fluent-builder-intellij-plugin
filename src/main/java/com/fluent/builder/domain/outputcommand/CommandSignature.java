package com.fluent.builder.domain.outputcommand;

public record CommandSignature(String value) {
    public CommandSignature {
        if(value == null) throw new IllegalStateException();
    }
}
