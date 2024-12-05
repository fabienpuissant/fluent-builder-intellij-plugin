package io.fabienpuissant.fluent.builder.domain;

import static io.fabienpuissant.fluent.builder.domain.FluentBuilderFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import io.fabienpuissant.fluent.builder.domain.outputcommand.*;
import io.fabienpuissant.fluent.builder.infrastructure.secondary.FluentBuilderGenerator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
  void shouldDeleteOneInterfaces() {
    builder.generateBuilder(classWithOneInterface());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).contains(
      DeleteCommand.builder().name(new TargetName("SutNameBuilder")).scope(CommandScope.CLASS).type(TargetType.INTERFACE)
    );
  }

  @Test
  void shouldRemoveAllCurrentInterfaces() {
    builder.generateBuilder(classWithInterfaces());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands())
      .contains(DeleteCommand.builder().name(new TargetName("SutNameBuilder")).scope(CommandScope.CLASS).type(TargetType.INTERFACE))
      .contains(DeleteCommand.builder().name(new TargetName("SutTypeBuilder")).scope(CommandScope.CLASS).type(TargetType.INTERFACE));
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
        .type(TargetType.INTERFACE)
    );
  }

  @Test
  void shouldInterfaceImplementNextMandatoryParamInterface() {
    builder.generateBuilder(classWithTwoMandatoryParameter());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands())
      .contains(
        CreateCommand.builder()
          .signature(new CommandSignature("public sealed interface SutFirstNameBuilder permits SutBuilder"))
          .content(new CommandContent("SutLastNameBuilder firstName(String firstName);"))
          .scope(CommandScope.CLASS)
          .type(TargetType.INTERFACE)
      )
      .contains(
        CreateCommand.builder()
          .signature(new CommandSignature("public sealed interface SutLastNameBuilder permits SutBuilder"))
          .content(new CommandContent("Sut lastName(String lastName);"))
          .scope(CommandScope.CLASS)
          .type(TargetType.INTERFACE)
      );
  }

  @Test
  void shouldAddOptionalParamInterfaceAtTheEnd() {
    builder.generateBuilder(classWithTwoMandatoryParameterAndOneOptional());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).contains(
      CreateCommand.builder()
        .signature(new CommandSignature("public sealed interface SutOptionalBuilder permits SutBuilder"))
        .content(new CommandContent("SutOptionalBuilder age(int age);\nSut build();"))
        .scope(CommandScope.CLASS)
        .type(TargetType.INTERFACE)
    );
  }

  @Test
  void shouldOptionalParamBeInOptionalBuilder() {
    builder.generateBuilder(classWithTwoMandatoryParameterAndTwoOptional());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands())
      .contains(
        CreateCommand.builder()
          .signature(new CommandSignature("public sealed interface SutLastNameBuilder permits SutBuilder"))
          .content(new CommandContent("SutOptionalBuilder lastName(String lastName);"))
          .scope(CommandScope.CLASS)
          .type(TargetType.INTERFACE)
      )
      .contains(
        CreateCommand.builder()
          .signature(new CommandSignature("public sealed interface SutOptionalBuilder permits SutBuilder"))
          .content(new CommandContent("SutOptionalBuilder age(int age);\nSutOptionalBuilder gender(String gender);\nSut build();"))
          .scope(CommandScope.CLASS)
          .type(TargetType.INTERFACE)
      );
  }

  @Test
  void shouldCreateBuilder() {
    builder.generateBuilder(emptyClass());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).contains(
      CreateCommand.builder()
        .signature(new CommandSignature("private static final class SutBuilder implements SutFirstNameBuilder"))
        .content(new CommandContent(null))
        .scope(CommandScope.CLASS)
        .type(TargetType.CLASS)
    );
  }

  @Test
  void shouldDropAndCreateCreateBuilderIfAlreadyExists() {
    builder.generateBuilder(emptyClassWithBuilder());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).containsSequence(
      List.of(
        DeleteCommand.builder().name(new TargetName("SutBuilder")).scope(CommandScope.CLASS).type(TargetType.CLASS),
        CreateCommand.builder()
          .signature(new CommandSignature("private static final class SutBuilder implements SutFirstNameBuilder"))
          .content(new CommandContent(null))
          .scope(CommandScope.CLASS)
          .type(TargetType.CLASS)
      )
    );
  }

  @Test
  void shouldCreateBuilderImplementsAllInterfaces() {
    builder.generateBuilder(classWithTwoMandatoryParameter());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).contains(
      CreateCommand.builder()
        .signature(new CommandSignature("private static final class SutBuilder implements SutFirstNameBuilder, SutLastNameBuilder"))
        .content(new CommandContent(null))
        .scope(CommandScope.CLASS)
        .type(TargetType.CLASS)
    );
  }

  @Test
  void shouldCreateBuilderImplementsAllInterfacesWithOptional() {
    builder.generateBuilder(classWithTwoMandatoryParameterAndOneOptional());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).contains(
      CreateCommand.builder()
        .signature(
          new CommandSignature(
            "private static final class SutBuilder implements SutFirstNameBuilder, SutLastNameBuilder, SutOptionalBuilder"
          )
        )
        .content(new CommandContent(null))
        .scope(CommandScope.CLASS)
        .type(TargetType.CLASS)
    );
  }

  @Test
  void shouldCreateBuilderFieldsWhenNotExists() {
    builder.generateBuilder(classWithOneMandatoryParameter());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).contains(
      CreateCommand.builder()
        .signature(new CommandSignature("private String firstName;"))
        .content(new CommandContent(null))
        .scope(CommandScope.BUILDER)
        .type(TargetType.FIELD)
    );
  }

  @Test
  void shouldNotAlterBuilderFieldWhenExistInCurrentBuilder() {
    builder.generateBuilder(classWithBuilderFieldExistingInParams());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).noneSatisfy(command -> {
      assertThat(command)
        .isInstanceOfSatisfying(CreateCommand.class, create -> {
          assertThat(create.scope()).isEqualTo(CommandScope.BUILDER);
          assertThat(create.signature().value()).contains("firstName").contains("lastName");
        })
        .isInstanceOfSatisfying(DeleteCommand.class, delete -> {
          assertThat(delete.name()).isEqualTo("firstName").isEqualTo("lastName");
        });
    });
  }

  @Test
  void shouldImplementInterfaceInBuilderWhenNotExists() {
    builder.generateBuilder(classWithOneMandatoryParameter());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).contains(
      CreateCommand.builder()
        .signature(
          new CommandSignature(
            """
            @Override
            public Sut firstName(String firstName)
            """
          )
        )
        .content(
          new CommandContent(
            """
            this.firstName = firstName;

            return new Sut(this);"""
          )
        )
        .scope(CommandScope.BUILDER)
        .type(TargetType.METHOD)
    );
  }

  @Test
  void shouldImplementMultipleInterfacesInBuilderWhenNotExists() {
    builder.generateBuilder(classWithTwoMandatoryParameterAndOneOptional());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands())
      .contains(
        CreateCommand.builder()
          .signature(
            new CommandSignature(
              """
              @Override
              public SutLastNameBuilder firstName(String firstName)
              """
            )
          )
          .content(
            new CommandContent(
              """
              this.firstName = firstName;

              return this;"""
            )
          )
          .scope(CommandScope.BUILDER)
          .type(TargetType.METHOD)
      )
      .contains(
        CreateCommand.builder()
          .signature(
            new CommandSignature(
              """
              @Override
              public SutOptionalBuilder lastName(String lastName)
              """
            )
          )
          .content(
            new CommandContent(
              """
              this.lastName = lastName;

              return this;"""
            )
          )
          .scope(CommandScope.BUILDER)
          .type(TargetType.METHOD)
      )
      .contains(
        CreateCommand.builder()
          .signature(
            new CommandSignature(
              """
              @Override
              public SutOptionalBuilder age(int age)
              """
            )
          )
          .content(
            new CommandContent(
              """
              this.age = age;

              return this;"""
            )
          )
          .scope(CommandScope.BUILDER)
          .type(TargetType.METHOD)
      );
  }

  @Test
  void shouldBuilderImplementBuildMethodIfAtLeastOneFieldOptional() {
    builder.generateBuilder(classWithTwoMandatoryParameterAndOneOptional());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).contains(
      CreateCommand.builder()
        .signature(new CommandSignature("@Override\npublic Sut build()"))
        .content(new CommandContent("return new Sut(this);"))
        .scope(CommandScope.BUILDER)
        .type(TargetType.METHOD)
    );
  }

  @Test
  void shouldCreateClassConstructor() {
    builder.generateBuilder(classWithTwoMandatoryParameterAndOneOptional());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).contains(
      CreateCommand.builder()
        .signature(new CommandSignature("private Sut(SutBuilder builder)"))
        .content(
          new CommandContent(
            """
            this.firstName = builder.firstName;
            this.lastName = builder.lastName;
            this.age = builder.age;"""
          )
        )
        .scope(CommandScope.CLASS)
        .type(TargetType.CONSTRUCTOR)
    );
  }

  @Test
  void shouldDropClassConstructorWhenExists() {
    builder.generateBuilder(classWithConstructor());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).containsSequence(
      List.of(
        DeleteCommand.builder().name(new TargetName("Sut")).scope(CommandScope.CLASS).type(TargetType.CONSTRUCTOR),
        CreateCommand.builder()
          .signature(new CommandSignature("private Sut(SutBuilder builder)"))
          .content(
            new CommandContent(
              """
              this.firstName = builder.firstName;"""
            )
          )
          .scope(CommandScope.CLASS)
          .type(TargetType.CONSTRUCTOR)
      )
    );
  }

  @Test
  void shouldCreateBuilderMethod() {
    builder.generateBuilder(classWithTwoMandatoryParameterAndOneOptional());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).contains(
      CreateCommand.builder()
        .signature(new CommandSignature("public static SutFirstNameBuilder builder()"))
        .content(new CommandContent("return new SutBuilder();"))
        .scope(CommandScope.CLASS)
        .type(TargetType.METHOD)
    );
  }

  @Test
  void shouldCreateBuilderMethodWithAllOptionalParam() {
    builder.generateBuilder(classWithTwoOptionalParameter());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).contains(
      CreateCommand.builder()
        .signature(new CommandSignature("public static SutOptionalBuilder builder()"))
        .content(new CommandContent("return new SutBuilder();"))
        .scope(CommandScope.CLASS)
        .type(TargetType.METHOD)
    );
  }

  @Test
  void shouldDropBuilderMethodWhenExists() {
    builder.generateBuilder(classWithBuilderMethod());

    verify(builderPort).generateBuilder(commandOutput.capture());
    assertThat(commandOutput.getValue().commands()).containsSequence(
      List.of(
        DeleteCommand.builder().name(new TargetName("builder")).scope(CommandScope.CLASS).type(TargetType.METHOD),
        CreateCommand.builder()
          .signature(new CommandSignature("public static SutFirstNameBuilder builder()"))
          .content(new CommandContent("return new SutBuilder();"))
          .scope(CommandScope.CLASS)
          .type(TargetType.METHOD)
      )
    );
  }
}
