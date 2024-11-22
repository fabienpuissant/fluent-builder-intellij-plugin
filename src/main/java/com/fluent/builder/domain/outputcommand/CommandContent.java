package com.fluent.builder.domain.outputcommand;

public record CommandContent(String value) {
    public CommandContent {
        if(value == null) throw new IllegalStateException();
    }
}
