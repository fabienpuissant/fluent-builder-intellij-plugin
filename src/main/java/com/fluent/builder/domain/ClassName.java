package com.fluent.builder.domain;

record ClassName(String value) {
    public ClassName {
        if (value.isBlank()) throw new IllegalStateException();
    }
}
