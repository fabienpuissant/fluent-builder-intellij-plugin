package com.fluent.builder.domain.outputcommand;


import java.util.Objects;
import java.util.Optional;

public class CommandContent {

    private final String value;

    public CommandContent(String value) {
        this.value = value;
    }

    public Optional<String> value() {
        if (value == null) return Optional.empty();

        return Optional.of(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        CommandContent that = (CommandContent) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
