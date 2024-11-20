package com.fluent.builder.domain;

public class FluentBuilderParameters {
    private final PluginContext context;
    private final Fields mandatoryParameters;
    private final Fields optionalParameters;

    public static FluentBuilderParametersContextBuilder builder() {
        return new FluentBuilderParametersBuilder();
    }

    public PluginContext context() {
        return context;
    }

    public Fields mandatoryParameters() {
        return mandatoryParameters;
    }

    public Fields optionalParameters() {
        return optionalParameters;
    }

    private FluentBuilderParameters(FluentBuilderParametersBuilder builder) {
        if(builder.context == null || builder.mandatoryParameters == null || builder.optionalParameters == null) throw new IllegalStateException();

        context = builder.context;
        mandatoryParameters = builder.mandatoryParameters;
        optionalParameters = builder.optionalParameters;
    }

    private static final class FluentBuilderParametersBuilder implements
        FluentBuilderParametersContextBuilder,
        FluentBuilderParametersMandatoryParametersBuilder,
        FluentBuilderParametersOptionalParametersBuilder {
        private PluginContext context;
        private Fields mandatoryParameters;
        private Fields optionalParameters;


        @Override
        public FluentBuilderParametersMandatoryParametersBuilder context(PluginContext context) {
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

    public interface FluentBuilderParametersContextBuilder {
        FluentBuilderParametersMandatoryParametersBuilder context(PluginContext context);
    }

    public interface FluentBuilderParametersMandatoryParametersBuilder {
        FluentBuilderParametersOptionalParametersBuilder mandatoryParameters(Fields mandatoryParameters);
    }

    public interface FluentBuilderParametersOptionalParametersBuilder {
        FluentBuilderParameters optionalParameters(Fields optionalParameters);
    }

}
