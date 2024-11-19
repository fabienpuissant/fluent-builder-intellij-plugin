package com.fluent.builder.domain;

public class Method {

    private final String signature;
    private final String content;

    public String signature() {
        return signature;
    }

    public String content() {
        return content;
    }

    private Method(MethodBuilder builder) {
        assert builder.signature != null && builder.content != null;

        this.signature = builder.signature;
        this.content = builder.content;
    }

    public static MethodSignatureBuilder builder() {
        return new MethodBuilder();
    }

    public sealed interface MethodSignatureBuilder permits MethodBuilder {
        MethodContentBuilder signature(final String signature);
    }

    public sealed interface MethodContentBuilder permits MethodBuilder {
        Method content(final String content);
    }

    public static final class MethodBuilder implements MethodSignatureBuilder, MethodContentBuilder {

        private String signature;
        private String content;

        private MethodBuilder() {
        }

        @Override
        public MethodContentBuilder signature(final String signature) {
            this.signature = signature;

            return this;
        }

        @Override
        public Method content(final String content) {
            this.content = content;

            return new Method(this);
        }
    }
}