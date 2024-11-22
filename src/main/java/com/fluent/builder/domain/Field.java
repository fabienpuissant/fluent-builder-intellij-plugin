package com.fluent.builder.domain;

public class Field {
    private String name;
    private String type;
    private String scope;

    private Field(FieldBuilder builder) {
        if(builder.name == null || builder.type == null || builder.scope == null) throw new IllegalStateException();

        this.name = builder.name;
        this.type = builder.type;
        this.scope = builder.scope;
    }

    public static FieldNameBuilder builder() {
        return new FieldBuilder();
    }

    public sealed interface FieldNameBuilder permits FieldBuilder {
        FieldTypeBuilder name(String name);
    }

    public sealed interface FieldTypeBuilder permits FieldBuilder {
        FieldScopeBuilder type(String type);
    }

    public sealed interface FieldScopeBuilder permits FieldBuilder {
        FieldBuilder scope(String scope);
    }

    private static final class FieldBuilder implements FieldNameBuilder, FieldTypeBuilder, FieldScopeBuilder {
        private String name;
        private String type;
        private String scope;

        @Override
        public FieldTypeBuilder name(String name) {
            this.name = name;

            return this;
        }

        @Override
        public FieldBuilder scope(String scope) {
            this.scope = scope;

            return this;
        }

        @Override
        public FieldScopeBuilder type(String type) {
            this.type = type;

            return this;
        }
    }



}
