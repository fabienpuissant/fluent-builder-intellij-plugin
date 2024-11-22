package com.fluent.builder.domain;

public record Interface(String signature) {
    public Interface {
        if(signature == null) throw new IllegalStateException();

        Field.builder().name("").type("").scope("");

    }
}
