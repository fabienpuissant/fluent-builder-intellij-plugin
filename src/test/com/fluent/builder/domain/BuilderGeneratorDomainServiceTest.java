package com.fluent.builder.domain;


import com.fluent.builder.domain.outputcommand.*;
import com.fluent.builder.infrastructure.secondary.FluentBuilderGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.fluent.builder.domain.FluentBuilderFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BuilderGeneratorDomainServiceTest {

    @Mock
    private FluentBuilderGenerator builderPort;

    private BuilderGeneratorDomainService builder;

    @Captor
    private ArgumentCaptor<BuilderCommandOutput> commandOutput;

    @BeforeEach
    void setUp() {
        this.builder = new BuilderGeneratorDomainService(builderPort);
    }

    @Test
        //TODO Maybe not delete all interfaces (first version drop all and create)
    void shouldDeleteOneInterfaces() {
        builder.generateBuilder(classWithOneInterface());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                DeleteCommand.builder()
                        .name(new TargetName("SutNameBuilder"))
                        .scope(CommandScope.CLASS)
                        .type(TargetType.INTERFACE)

        );
    }

    @Test
    void shouldRemoveAllCurrentInterfaces() {
        builder.generateBuilder(classWithInterfaces());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                DeleteCommand.builder()
                        .name(new TargetName("SutNameBuilder"))
                        .scope(CommandScope.CLASS)
                        .type(TargetType.INTERFACE)

        ).contains(
                DeleteCommand.builder()
                        .name(new TargetName("SutTypeBuilder"))
                        .scope(CommandScope.CLASS)
                        .type(TargetType.INTERFACE)
        );
    }

    @Test
    void shouldCreateLastInterfaceReturnBuiltObject() {
        builder.generateBuilder(classWithOneMandatoryParameter());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                CreateCommand.builder()
                        .signature(new CommandSignature("public sealed interface SutFirstNameBuilder permits SutBuilder"))
                        .content(new CommandContent("Sut firstName(String firstName);"))
                        .scope(CommandScope.CLASS)
        );
    }

    @Test
    void shouldInterfaceImplementNextMandatoryParamInterface() {
        builder.generateBuilder(classWithTwoMandatoryParameter());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                CreateCommand.builder()
                        .signature(new CommandSignature("public sealed interface SutFirstNameBuilder permits SutBuilder"))
                        .content(new CommandContent("SutLastNameBuilder firstName(String firstName);"))
                        .scope(CommandScope.CLASS)
        ).contains(CreateCommand.builder()
                .signature(new CommandSignature("public sealed interface SutLastNameBuilder permits SutBuilder"))
                .content(new CommandContent("Sut lastName(String lastName);"))
                .scope(CommandScope.CLASS));
    }

    @Test
    void shouldAddOptionalParamInterfaceAtTheEnd() {
        builder.generateBuilder(classWithTwoMandatoryParameterAndOneOptional());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                CreateCommand.builder()
                        .signature(new CommandSignature("public sealed interface SutAgeBuilder permits SutBuilder"))
                        .content(new CommandContent("Sut age(int age);"))
                        .scope(CommandScope.CLASS)
        );
    }

    @Test
    void shouldOptionalParamInterfacesAlwaysReturnMainClassBuilder() {
        builder.generateBuilder(classWithTwoMandatoryParameterAndTwoOptional());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                        CreateCommand.builder()
                                .signature(new CommandSignature("public sealed interface SutAgeBuilder permits SutBuilder"))
                                .content(new CommandContent("SutBuilder age(int age);"))
                                .scope(CommandScope.CLASS))
                .contains(
                        CreateCommand.builder()
                                .signature(new CommandSignature("public sealed interface SutGenderBuilder permits SutBuilder"))
                                .content(new CommandContent("Sut gender(String gender);"))
                                .scope(CommandScope.CLASS)
                );
    }

    @Test
    void shouldCreateBuilderIfNotExists() {
        builder.generateBuilder(emptyClass());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                CreateCommand.builder()
                        .signature(new CommandSignature("private static final class SutBuilder"))
                        .content(new CommandContent(null))
                        .scope(CommandScope.CLASS));
    }

    @Test
    void shouldNotCreateBuilderIfAlreadyExists() {
        builder.generateBuilder(emptyClassWithBuilder());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).doesNotContain(
                CreateCommand.builder()
                        .signature(new CommandSignature("private static final class SutBuilder"))
                        .content(new CommandContent(null))
                        .scope(CommandScope.CLASS));
    }

    @Test
    void shouldCreateBuilderImplementsAllInterfaces() {
        builder.generateBuilder(classWithTwoMandatoryParameter());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                CreateCommand.builder()
                        .signature(new CommandSignature("private static final class SutBuilder implements FirstNameBuilder, LastNameBuilder"))
                        .content(new CommandContent(null))
                        .scope(CommandScope.CLASS));
    }

    @Test
    void shouldCreateBuilderFieldsWhenNotExists() {
        builder.generateBuilder(classWithOneMandatoryParameter());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                CreateCommand.builder()
                        .signature(new CommandSignature("private String firstName;"))
                        .content(new CommandContent(null))
                        .scope(CommandScope.BUILDER));
    }

    @Test
    void shouldDeleteBuilderFieldsWhenExistInCurrentBuilderButNotExistsInParameters() {
        builder.generateBuilder(classWithBuilderFieldNotExistingInParams());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                        DeleteCommand.builder()
                                .name(new TargetName("firstName"))
                                .scope(CommandScope.BUILDER)
                                .type(TargetType.FIELD))
                .contains(CreateCommand.builder()
                        .signature(new CommandSignature("private String lastName;"))
                        .content(new CommandContent(null))
                        .scope(CommandScope.BUILDER));
    }

    @Test
    void shouldNotAlterBuilderFieldWhenExistInCurrentBuilder() {
        builder.generateBuilder(classWithBuilderFieldExistingInParams());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).noneSatisfy(command -> {
            assertThat(command).isInstanceOfSatisfying(CreateCommand.class, create -> {
                assertThat(create.scope()).isEqualTo(CommandScope.BUILDER);
                assertThat(create.signature().value()).contains("firstName").contains("lastName");
            }).isInstanceOfSatisfying(DeleteCommand.class, delete -> {
                assertThat(delete.name()).isEqualTo("firstName").isEqualTo("lastName");
            });
        });
    }

    @Test
        //TODO Maybe not delete all interfaces (first version drop all and create)
    void shouldDeleteAllBuilderInterfaces() {
        builder.generateBuilder(classWithBuilderMethods());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                        DeleteCommand.builder()
                                .name(new TargetName("firstName"))
                                .scope(CommandScope.BUILDER)
                                .type(TargetType.METHOD));
    }


    @Test
    void shouldImplementInterfaceInBuilderWhenNotExists() {
        builder.generateBuilder(classWithOneMandatoryParameter());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                CreateCommand.builder()
                        .signature(new CommandSignature("""
                                @Override
                                public Sut firstName(String firstName)
                                """))
                        .content(new CommandContent("""
                                this.firstName = firstName;
                                
                                return new Sut(this);
                                """))
                        .scope(CommandScope.BUILDER));
    }

    @Test
    void shouldImplementMultipleInterfacesInBuilderWhenNotExists() {
        builder.generateBuilder(classWithTwoMandatoryParameter());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                CreateCommand.builder()
                        .signature(new CommandSignature("""
                                @Override
                                public SutLastNameBuilder firstName(String firstName)
                                """))
                        .content(new CommandContent("""
                                this.firstName = firstName;
                                
                                return this;
                                """))
                        .scope(CommandScope.BUILDER))
                .contains(CreateCommand.builder()
                        .signature(new CommandSignature("""
                                @Override
                                public Sut lastName(String lastName)
                                """))
                        .content(new CommandContent("""
                                this.lastName = lastName;
                                
                                return new Sut(this);
                                """))
                        .scope(CommandScope.BUILDER));
    }

    @Test
    void shouldBuilderImplementBuildMethodIfAtLeastOneFieldOptional() {
        builder.generateBuilder(classWithTwoMandatoryParameterAndOneOptional());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                CreateCommand.builder()
                        .signature(new CommandSignature("public Sut build()"))
                        .content(new CommandContent("return new Sut(this);"))
                        .scope(CommandScope.BUILDER));
    }

    @Test
    void shouldCreateClassConstructor() {
        builder.generateBuilder(classWithTwoMandatoryParameterAndOneOptional());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                CreateCommand.builder()
                        .signature(new CommandSignature("private Sut(SutBuilder builder)"))
                        .content(new CommandContent("""
                                this.firstName = builder.firstName;
                                this.lastName = builder.lastName;
                                this.age = builder.age;
                                """))
                        .scope(CommandScope.CLASS));
    }

    @Test
    void shouldCreateBuilderMethod() {
        builder.generateBuilder(classWithTwoMandatoryParameterAndOneOptional());

        verify(builderPort).generateBuilder(commandOutput.capture());
        assertThat(commandOutput.getValue().commands()).contains(
                CreateCommand.builder()
                        .signature(new CommandSignature("public static SutFirstNameBuilder builder()"))
                        .content(new CommandContent("return new SutBuilder();"))
                        .scope(CommandScope.CLASS));
    }



}