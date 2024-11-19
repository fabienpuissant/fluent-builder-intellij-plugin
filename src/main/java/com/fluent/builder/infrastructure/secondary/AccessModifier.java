package com.fluent.builder.infrastructure.secondary;

public enum AccessModifier {
    PUBLIC("public"),
    PRIVATE("private");

    public final String label;

    AccessModifier(String label) {
        this.label = label;
    }
}
