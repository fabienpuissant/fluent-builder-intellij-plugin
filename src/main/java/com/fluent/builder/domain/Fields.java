package com.fluent.builder.domain;

import java.util.List;

public record Fields(List<Field> fields) {
    public Fields {
        if(fields == null) throw new IllegalStateException();
    }
}
