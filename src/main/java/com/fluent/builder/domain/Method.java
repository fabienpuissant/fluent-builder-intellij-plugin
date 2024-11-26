package com.fluent.builder.domain;

public class Method {

    private final String signature;
    private final String content;
    private final boolean shouldOverride;

    private Method(MethodBuilder builder) {
        assert builder.signature != null && builder.content != null;

        this.signature = builder.signature;
        this.content = builder.content;
        this.shouldOverride = Boolean.TRUE.equals(builder.shouldOverride);
    }

    public static MethodSignatureBuilder builder() {
        return new MethodBuilder();
    }

    public sealed interface MethodSignatureBuilder permits MethodBuilder {
        MethodContentBuilder signature(final String signature);
    }

    public sealed interface MethodContentBuilder permits MethodBuilder {
        MethodBuilder content(final String content);
    }

    public sealed interface MethodShouldOverrideBuilder permits MethodBuilder {
        Method shouldOverride(final boolean shouldOverride);
    }

    public static final class MethodBuilder implements MethodSignatureBuilder, MethodContentBuilder, MethodShouldOverrideBuilder {

        private String signature;
        private String content;
        private boolean shouldOverride;

        private MethodBuilder() {
        }

        @Override
        public MethodContentBuilder signature(final String signature) {
            this.signature = signature;

            return this;
        }

        @Override
        public MethodBuilder content(final String content) {
            this.content = content;

            return this;
        }

        @Override
        public Method shouldOverride(boolean shouldOverride) {
            this.shouldOverride = shouldOverride;

            return new Method(this);
        }

        public Method build() {
            return new Method(this);
        }
    }
}