package com.fluent.builder.domain;

public enum Scope {
    PRIVATE("private"),
    PUBLIC("public");

    private final String scope;

    Scope(String scope) {
        this.scope = scope;
    }

    public String scope() {
        return scope;
    }
}
