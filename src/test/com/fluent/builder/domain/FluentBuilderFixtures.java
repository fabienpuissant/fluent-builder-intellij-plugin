package com.fluent.builder.domain;

import java.util.List;

public class FluentBuilderFixtures {

    private FluentBuilderFixtures() {
    }

    public static final String CLASS_NAME = "Sut";

    static FluentBuilderParameters classWithOneInterface() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(false)
                        .existingBuilderFields(List.of())
                        .interfaces(List.of(
                                Interface.builder()
                                        .name("SutNameBuilder")
                                        .signatures(List.of(""))
                        ))
                        .classMethods(List.of())
                        .builderMethods(List.of()))
                .parameters(new Fields(List.of(Field.builder()
                        .name("name")
                        .type("String")
                        .isOptional(false))));
    }

    static FluentBuilderParameters classWithInterfaces() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(false)
                        .existingBuilderFields(List.of())
                        .interfaces(List.of(
                                Interface.builder()
                                        .name("SutNameBuilder")
                                        .signatures(List.of("")),
                                Interface.builder()
                                        .name("SutTypeBuilder")
                                        .signatures(List.of(""))
                        ))
                        .classMethods(List.of())
                        .builderMethods(List.of()))
                .parameters(new Fields(List.of(Field.builder()
                                .name("name")
                                .type("String")
                                .isOptional(false),
                        Field.builder()
                                .name("type")
                                .type("String")
                                .isOptional(false)
                )));
    }

    static FluentBuilderParameters classWithOneMandatoryParameter() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(false)
                        .existingBuilderFields(List.of())
                        .interfaces(List.of())
                        .classMethods(List.of())
                        .builderMethods(List.of()))
                .parameters(new Fields(List.of(Field.builder()
                        .name("firstName")
                        .type("String")
                        .isOptional(false))));
    }

    static FluentBuilderParameters classWithTwoMandatoryParameter() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(false)
                        .existingBuilderFields(List.of())
                        .interfaces(List.of())
                        .classMethods(List.of())
                        .builderMethods(List.of()))
                .parameters(new Fields(List.of(Field.builder()
                                .name("firstName")
                                .type("String")
                                .isOptional(false),
                        Field.builder()
                                .name("lastName")
                                .type("String")
                                .isOptional(false))));
    }

    static FluentBuilderParameters classWithTwoMandatoryParameterAndOneOptional() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(false)
                        .existingBuilderFields(List.of())
                        .interfaces(List.of())
                        .classMethods(List.of())
                        .builderMethods(List.of()))
                .parameters(new Fields(List.of(Field.builder()
                                .name("firstName")
                                .type("String")
                                .isOptional(false),
                        Field.builder()
                                .name("lastName")
                                .type("String")
                                .isOptional(false),
                        Field.builder()
                                .name("age")
                                .type("int")
                                .isOptional(true))));
    }

    static FluentBuilderParameters classWithTwoMandatoryParameterAndTwoOptional() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(false)
                        .existingBuilderFields(List.of())
                        .interfaces(List.of())
                        .classMethods(List.of())
                        .builderMethods(List.of()))
                .parameters(new Fields(List.of(Field.builder()
                                .name("firstName")
                                .type("String")
                                .isOptional(false),
                        Field.builder()
                                .name("lastName")
                                .type("String")
                                .isOptional(false),
                        Field.builder()
                                .name("age")
                                .type("int")
                                .isOptional(true),
                        Field.builder()
                                .name("gender")
                                .type("String")
                                .isOptional(true))));
    }

    static FluentBuilderParameters emptyClass() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(false)
                        .existingBuilderFields(List.of())
                        .interfaces(List.of())
                        .classMethods(List.of())
                        .builderMethods(List.of()))
                .parameters(new Fields(List.of(Field.builder()
                        .name("firstName")
                        .type("String")
                        .isOptional(false))));
    }

    static FluentBuilderParameters emptyClassWithBuilder() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(true)
                        .existingBuilderFields(List.of())
                        .interfaces(List.of())
                        .classMethods(List.of())
                        .builderMethods(List.of()))
                .parameters(new Fields(List.of(Field.builder()
                        .name("firstName")
                        .type("String")
                        .isOptional(false))));
    }

    static FluentBuilderParameters classWithBuilderFieldNotExistingInParams() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(true)
                        .existingBuilderFields(List.of(
                                BuilderField.builder()
                                        .name("firstName")
                                        .type("String")
                        ))
                        .interfaces(List.of())
                        .classMethods(List.of())
                        .builderMethods(List.of()))
                .parameters(new Fields(List.of(Field.builder()
                        .name("lastName")
                        .type("String")
                        .isOptional(false))));
    }

    static FluentBuilderParameters classWithBuilderFieldExistingInParams() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(true)
                        .existingBuilderFields(List.of(
                                BuilderField.builder()
                                        .name("firstName")
                                        .type("String"),
                                BuilderField.builder()
                                        .name("lastName")
                                        .type("String")
                        ))
                        .interfaces(List.of())
                        .classMethods(List.of())
                        .builderMethods(List.of()))
                .parameters(new Fields(List.of(Field.builder()
                                .name("firstName")
                                .type("String")
                                .isOptional(false),
                        Field.builder()
                                .name("lastName")
                                .type("String")
                                .isOptional(false))));
    }

    //TODO shouldOverride seems useless maybe tbd

    static FluentBuilderParameters classWithBuilderMethod() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(true)
                        .existingBuilderFields(List.of(
                                BuilderField.builder()
                                        .name("firstName")
                                        .type("String")))
                        .interfaces(List.of())
                        .classMethods(List.of(Method.builder()
                                .signature("public static SutFirstNameBuilder builder()")
                                .content("return new SutBuilder();")
                                .shouldOverride(false)))
                        .builderMethods(List.of()))
                .parameters(new Fields(List.of(Field.builder()
                        .name("firstName")
                        .type("Strind")
                        .isOptional(false))));
    }

    static FluentBuilderParameters classWithConstructor() {
        return FluentBuilderParameters.builder()
                .context(ExistingClass.builder()
                        .className(CLASS_NAME)
                        .isBuilderExist(true)
                        .existingBuilderFields(List.of(
                                BuilderField.builder()
                                        .name("firstName")
                                        .type("String")))
                        .interfaces(List.of())
                        .classMethods(List.of(Method.builder()
                                        .signature("public static SutFirstNameBuilder builder()")
                                        .content("return new SutBuilder();")
                                        .shouldOverride(false),
                                Method.builder()
                                        .signature("private Sut(Sut.SutBuilder builder)")
                                        .content("this.firstName = builder.firstName;")
                                        .shouldOverride(false)))
                        .builderMethods(List.of()))
                .parameters(new Fields(List.of(Field.builder()
                        .name("firstName")
                        .type("Strind")
                        .isOptional(false))));
    }

}
