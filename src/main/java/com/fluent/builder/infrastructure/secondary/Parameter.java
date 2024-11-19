package com.fluent.builder.infrastructure.secondary;

record Parameter<T>(String name, T 
                    ) {
    public Parameter {
        if (name.isBlank() || type == null) throw new IllegalStateException();
    }
}
