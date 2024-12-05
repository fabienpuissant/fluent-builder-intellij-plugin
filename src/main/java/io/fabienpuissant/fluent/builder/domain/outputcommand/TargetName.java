package io.fabienpuissant.fluent.builder.domain.outputcommand;

public record TargetName(String name) {

    public TargetName {
        assert name != null;
    }
}
