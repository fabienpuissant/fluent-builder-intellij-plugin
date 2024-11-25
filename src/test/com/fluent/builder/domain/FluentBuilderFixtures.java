package com.fluent.builder.domain;

import java.util.List;

public class FluentBuilderFixtures {

    private FluentBuilderFixtures() {
    }

    public static final String CLASS_NAME = "Sut";

    static FluentBuilderParameters emptyBuilder() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(false)
                        .existingBuilderFields(List.of())
                        .interfaces(List.of())
                        .classMethods(List.of())
                        .builderMethods(List.of()))
                .mandatoryParameters(new Fields(List.of()))
                .optionalParameters(new Fields(List.of()));
    }

}
