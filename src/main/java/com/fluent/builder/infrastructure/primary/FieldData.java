package com.fluent.builder.infrastructure.primary;

public class FieldData {

    private final String name;
    private final String type;
    private final boolean isOptional;

    public String name() {
        return name;
    }

    public String type() {
        return type;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public static FieldDataNameBuilder builder() {
        return new FieldDataBuilder();
    }

    private FieldData(final FieldDataBuilder builder) {
        if (builder.name == null || builder.type == null) throw new IllegalStateException();

        this.name = builder.name;
        this.type = builder.type;
        this.isOptional = builder.isOptional;
    }


    public static class FieldDataBuilder implements
            FieldDataNameBuilder,
            FieldDataTypeBuilder,
            FieldDataIsOptionalBuilder {

        private String name;
        private String type;
        private boolean isOptional;


        @Override
        public FieldDataTypeBuilder name(final String name) {
            this.name = name;

            return this;
        }

        @Override
        public FieldDataIsOptionalBuilder type(final String type) {
            this.type = type;

            return this;
        }

        @Override
        public FieldData isOptional(final boolean isOptional) {
            this.isOptional = isOptional;

            return new FieldData(this);
        }

        public FieldData build() {
            return new FieldData(this);
        }
    }

    public interface FieldDataNameBuilder {
        FieldDataTypeBuilder name(final String type);
    }

    public interface FieldDataTypeBuilder {
        FieldDataIsOptionalBuilder type(final String type);
    }

    public interface FieldDataIsOptionalBuilder {
        FieldData isOptional(final boolean isOptional);
    }

}
