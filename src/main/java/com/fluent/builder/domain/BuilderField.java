package com.fluent.builder.domain;

public class BuilderField {
    private String name;
    private String type;

    public String name() {
        return name;
    }

    public String type() {
        return type;
    }

    private BuilderField(BuilderFieldBuilder builder) {
        assert builder.name != null && builder.type != null;

        this.name = builder.name;
        this.type = builder.type;
    }

    public static BuilderFieldNameBuilder builder() {
        return new BuilderFieldBuilder();
    }

    public sealed interface BuilderFieldNameBuilder permits BuilderFieldBuilder {
        BuilderFieldTypeBuilder name(String name);
    }

    public sealed interface BuilderFieldTypeBuilder permits BuilderFieldBuilder {
        BuilderField type(String type);
    }

    private static final class BuilderFieldBuilder implements BuilderFieldNameBuilder, BuilderFieldTypeBuilder {
        private String name;
        private String type;

        @Override
        public BuilderFieldTypeBuilder name(String name) {
            this.name = name;

            return this;
        }

        @Override
        public BuilderField type(String type) {
            this.type = type;

            return new BuilderField(this);
        }
    }



}

