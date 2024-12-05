package io.fabienpuissant.fluent.builder.domain;

public class FluentBuilderParameters {
    private final ExistingClass existingClass;
    private final Fields parameters;

    public ExistingClass existingClass() {
        return existingClass;
    }

    public static FluentBuilderParametersContextBuilder builder() {
        return new FluentBuilderParametersBuilder();
    }

    public Fields parameters() {
        return parameters;
    }


    private FluentBuilderParameters(FluentBuilderParametersBuilder builder) {
        assert builder.context != null && builder.mandatoryParameters != null;

        existingClass = builder.context;
        parameters = builder.mandatoryParameters;
    }

    private static final class FluentBuilderParametersBuilder implements
        FluentBuilderParametersContextBuilder,
        FluentBuilderParametersMandatoryParametersBuilder {
        private ExistingClass context;
        private Fields mandatoryParameters;


        @Override
        public FluentBuilderParametersMandatoryParametersBuilder context(ExistingClass context) {
            this.context = context;

            return this;
        }

        @Override
        public FluentBuilderParameters parameters(Fields mandatoryParameters) {
            this.mandatoryParameters = mandatoryParameters;

            return new FluentBuilderParameters(this);

        }
    }

    public sealed interface FluentBuilderParametersContextBuilder permits FluentBuilderParametersBuilder {
        FluentBuilderParametersMandatoryParametersBuilder context(ExistingClass context);
    }

    public sealed interface FluentBuilderParametersMandatoryParametersBuilder permits FluentBuilderParametersBuilder {
        FluentBuilderParameters parameters(Fields mandatoryParameters);
    }

}
