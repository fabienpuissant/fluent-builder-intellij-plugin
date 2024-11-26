package com.fluent.builder.domain;

record ClassName(String value) {
    public ClassName {
        assert !value.isBlank();
    }
}
