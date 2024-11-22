package com.fluent.builder.domain;

public record Method(String signature, String content) {
    public Method(final String signature, final String content) {
        this.signature = signature;
        this.content = content;
    }

    public static ContentBuilder signature(final String signature) {
        return new Builder().signature(signature);
    }

    public sealed interface ContentBuilder permits Builder {

        MethodCreator content(final String content);

    }

    public sealed interface MethodCreator permits Builder {

        Method build();

    }

    private static final class Builder implements ContentBuilder, MethodCreator {

        private String signature;

        private String content;

        private Builder() {
        }

        private ContentBuilder signature(final String signature) {
            this.signature = signature;
            return this;
        }

        @Override
        public MethodCreator content(final String content) {
            this.content = content;
            return this;
        }

        @Override
        public Method build() {
            return new Method(signature, content);
        }

    }

}
