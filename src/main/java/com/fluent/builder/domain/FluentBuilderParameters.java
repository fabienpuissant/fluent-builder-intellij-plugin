package com.fluent.builder.domain;

public class FluentBuilderParameters {
    private final ExistingClass existingClass;
    private final Fields mandatoryParameters;
    private final Fields optionalParameters;

    public static FluentBuilderParametersContextBuilder builder() {
        return new FluentBuilderParametersBuilder();
    }

    public ExistingClass context() {
        return existingClass;
    }

    public Fields mandatoryParameters() {
        return mandatoryParameters;
    }

    public Fields optionalParameters() {
        return optionalParameters;
    }

    private FluentBuilderParameters(FluentBuilderParametersBuilder builder) {
        if(builder.context == null || builder.mandatoryParameters == null || builder.optionalParameters == null) throw new IllegalStateException();

        existingClass = builder.context;
        mandatoryParameters = builder.mandatoryParameters;
        optionalParameters = builder.optionalParameters;
    }

    private static final class FluentBuilderParametersBuilder implements
        FluentBuilderParametersContextBuilder,
        FluentBuilderParametersMandatoryParametersBuilder,
        FluentBuilderParametersOptionalParametersBuilder {
        private ExistingClass context;
        private Fields mandatoryParameters;
        private Fields optionalParameters;


        @Override
        public FluentBuilderParametersMandatoryParametersBuilder context(ExistingClass context) {
            this.context = context;

            return this;
        }

        @Override
        public FluentBuilderParametersOptionalParametersBuilder mandatoryParameters(Fields mandatoryParameters) {
            this.mandatoryParameters = mandatoryParameters;

            return this;
        }

        @Override
        public FluentBuilderParameters optionalParameters(Fields optionalParameters) {
            this.optionalParameters = optionalParameters;

            return new FluentBuilderParameters(this);
        }
    }

    public sealed interface FluentBuilderParametersContextBuilder permits FluentBuilderParametersBuilder {
        FluentBuilderParametersMandatoryParametersBuilder context(ExistingClass context);
    }

    public sealed interface FluentBuilderParametersMandatoryParametersBuilder permits FluentBuilderParametersBuilder {
        FluentBuilderParametersOptionalParametersBuilder mandatoryParameters(Fields mandatoryParameters);
    }

    public sealed interface FluentBuilderParametersOptionalParametersBuilder permits FluentBuilderParametersBuilder {
        FluentBuilderParameters optionalParameters(Fields optionalParameters);
    }

}
