package com.fluent.builder.domain;

record Parameter<T>(String name, T type) {
    public Parameter {
        if (name.isBlank() || type == null) throw new IllegalStateException();
    }
}
