package com.fluent.builder.domain;

public class Field {
    private String name;
    private String type;

    private Field(FieldBuilder builder) {
        if(builder.name == null || builder.type == null) throw new IllegalStateException();

        this.name = builder.name;
        this.type = builder.type;
    }

    public static FieldNameBuilder builder() {
        return new FieldBuilder();
    }

    public sealed interface FieldNameBuilder permits FieldBuilder {
        FieldTypeBuilder name(String name);
    }

    public sealed interface FieldTypeBuilder permits FieldBuilder {
        Field type(String type);
    }

    private static final class FieldBuilder implements FieldNameBuilder, FieldTypeBuilder {
        private String name;
        private String type;

        @Override
        public FieldTypeBuilder name(String name) {
            this.name = name;

            return this;
        }

        @Override
        public Field type(String type) {
            this.type = type;

            return new Field(this);
        }
    }



}
