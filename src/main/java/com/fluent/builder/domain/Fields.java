package com.fluent.builder.domain;

import java.util.List;
import java.util.stream.Stream;

public record Fields(List<Field> fields) {
    public Fields {
        assert fields != null;
    }

    public Fields concat(Fields newFields) {
        return new Fields(Stream.concat(fields.stream(), newFields.fields().stream()).toList());
    }
}
