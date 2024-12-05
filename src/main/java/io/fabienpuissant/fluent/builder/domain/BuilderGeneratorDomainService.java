package io.fabienpuissant.fluent.builder.domain;

import static java.util.function.Predicate.*;
import static org.apache.commons.lang3.StringUtils.capitalize;

import io.fabienpuissant.fluent.builder.domain.outputcommand.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BuilderGeneratorDomainService {

  private final BuilderPort builderPort;

  public BuilderGeneratorDomainService(BuilderPort builderPort) {
    this.builderPort = builderPort;
  }

  public void generateBuilder(FluentBuilderParameters fluentBuilderParameters) {
    List<BuilderCommand> commands = new ArrayList<>();
    commands.addAll(interfacesCommands(fluentBuilderParameters));
    commands.addAll(builderCommands(fluentBuilderParameters));
    commands.addAll(builderFields(fluentBuilderParameters));
    commands.addAll(builderInterfacesCommands(fluentBuilderParameters));
    commands.addAll(buildMethod(fluentBuilderParameters));
    commands.addAll(buildConstructor(fluentBuilderParameters));
    commands.addAll(builderMethod(fluentBuilderParameters));
    builderPort.generateBuilder(new BuilderCommandOutput(commands));
  }

  private List<BuilderCommand> interfacesCommands(FluentBuilderParameters fluentBuilderParameters) {
    List<BuilderCommand> commands = new ArrayList<>();
    commands.addAll(interfacesToRemove(fluentBuilderParameters.existingClass()));

    interfacesToCreate(fluentBuilderParameters, commands);

    return commands;
  }

  private void interfacesToCreate(FluentBuilderParameters fluentBuilderParameters, List<BuilderCommand> commands) {
    List<Field> mandatoryParams = fluentBuilderParameters.parameters().fields().stream().filter(not(Field::isOptional)).toList();
    List<Field> optionalParams = fluentBuilderParameters.parameters().fields().stream().filter(Field::isOptional).toList();
    String className = fluentBuilderParameters.existingClass().className();

    for (int i = 0; i < mandatoryParams.size(); i++) {
      Field field = mandatoryParams.get(i);
      String currentInterfaceName = interfaceNameForParameter(field, className);
      boolean isLastMandatoryField = field.equals(mandatoryParams.getLast());
      StringBuilder nextInterfaceName = new StringBuilder();
      if (!isLastMandatoryField) {
        nextInterfaceName.append(interfaceNameForParameter(mandatoryParams.get(i + 1), className));
      } else {
        nextInterfaceName.append(optionalParams.isEmpty() ? className : "%sOptionalBuilder".formatted(className));
      }

      String interfaceContent = nextInterfaceName.toString().equals(className)
        ? String.format("%s %s(%s %s);", className, field.name(), field.type(), field.name())
        : String.format("%s %s(%s %s);", nextInterfaceName, field.name(), field.type(), field.name());

      commands.add(
        CreateCommand.builder()
          .signature(new CommandSignature(String.format("public sealed interface %s permits %sBuilder", currentInterfaceName, className)))
          .content(new CommandContent(interfaceContent))
          .scope(CommandScope.CLASS)
          .type(TargetType.INTERFACE)
      );
    }

    if (!optionalParams.isEmpty()) {
      StringBuilder contentBuilder = new StringBuilder();
      for (Field field : optionalParams) {
        contentBuilder.append("%sOptionalBuilder %s(%s %s);\n".formatted(className, field.name(), field.type(), field.name()));
      }
      contentBuilder.append("%s build();".formatted(className));

      commands.add(
        CreateCommand.builder()
          .signature(new CommandSignature("public sealed interface %sOptionalBuilder permits %sBuilder".formatted(className, className)))
          .content(new CommandContent(contentBuilder.toString()))
          .scope(CommandScope.CLASS)
          .type(TargetType.INTERFACE)
      );
    }
  }

  private String interfaceNameForParameter(Field field, String className) {
    return "%s%sBuilder".formatted(className, capitalize(field.name()));
  }

  private List<BuilderCommand> builderCommands(FluentBuilderParameters fluentBuilderParameters) {
    List<BuilderCommand> commands = new ArrayList<>();
    String builderName = fluentBuilderParameters.existingClass().className() + "Builder";

    deleteExistingBuilder(fluentBuilderParameters, builderName).ifPresent(commands::add);
    commands.add(createBuilder(fluentBuilderParameters, builderName));

    return commands;
  }

  private List<BuilderCommand> builderFields(FluentBuilderParameters fluentBuilderParameters) {
    List<BuilderCommand> commands = new ArrayList<>();
    for (Field field : fluentBuilderParameters.parameters().fields()) {
      commands.add(
        CreateCommand.builder()
          .signature(new CommandSignature("private %s %s;".formatted(field.type(), field.name())))
          .content(new CommandContent(null))
          .scope(CommandScope.BUILDER)
          .type(TargetType.FIELD)
      );
    }
    return commands;
  }

  private static List<CreateCommand> builderInterfacesCommands(FluentBuilderParameters fluentBuilderParameters) {
    List<CreateCommand> commands = new ArrayList<>();
    Iterator<Field> fieldIterator = fluentBuilderParameters.parameters().fields().iterator();

    String existingClassName = fluentBuilderParameters.existingClass().className();
    Field currentField;
    Field nextField = fieldIterator.hasNext() ? fieldIterator.next() : null;

    while (nextField != null) {
      currentField = nextField;
      nextField = fieldIterator.hasNext() ? fieldIterator.next() : null;

      boolean isOptionalFieldExist = fluentBuilderParameters.parameters().fields().stream().anyMatch(Field::isOptional);
      String nextBuilder = isOptionalFieldExist ? existingClassName + "OptionalBuilder" : existingClassName;
      String nextReturn = isOptionalFieldExist ? "this" : "new %s(this)".formatted(existingClassName);

      if (nextField != null) {
        nextBuilder = nextField.isOptional() ? existingClassName + "Optional" : existingClassName + capitalize(nextField.name());
        nextBuilder += "Builder";
        nextReturn = "this";
      }

      commands.add(
        CreateCommand.builder()
          .signature(
            new CommandSignature(
              """
              @Override
              public %s %s(%s %s)
              """.formatted(nextBuilder, currentField.name(), currentField.type(), currentField.name())
            )
          )
          .content(
            new CommandContent(
              """
              this.%s = %s;

              return %s;""".formatted(currentField.name(), currentField.name(), nextReturn)
            )
          )
          .scope(CommandScope.BUILDER)
          .type(TargetType.METHOD)
      );
    }

    return commands;
  }

  private List<CreateCommand> buildMethod(FluentBuilderParameters fluentBuilderParameters) {
    if (fluentBuilderParameters.parameters().fields().stream().noneMatch(Field::isOptional)) {
      return List.of();
    }
    return List.of(
      CreateCommand.builder()
        .signature(new CommandSignature("@Override\npublic %s build()".formatted(fluentBuilderParameters.existingClass().className())))
        .content(new CommandContent("return new %s(this);".formatted(fluentBuilderParameters.existingClass().className())))
        .scope(CommandScope.BUILDER)
        .type(TargetType.METHOD)
    );
  }

  private List<BuilderCommand> buildConstructor(FluentBuilderParameters fluentBuilderParameters) {
    List<BuilderCommand> commands = new ArrayList<>();

    if (isConstructorExists(fluentBuilderParameters)) {
      commands.add(
        DeleteCommand.builder()
          .name(new TargetName(fluentBuilderParameters.existingClass().className()))
          .scope(CommandScope.CLASS)
          .type(TargetType.CONSTRUCTOR)
      );
    }

    String content = fluentBuilderParameters
      .parameters()
      .fields()
      .stream()
      .map(field -> "this.%s = builder.%s;".formatted(field.name(), field.name()))
      .collect(Collectors.joining("\n"));
    commands.add(
      CreateCommand.builder()
        .signature(
          new CommandSignature(
            "private %s(%sBuilder builder)".formatted(
                fluentBuilderParameters.existingClass().className(),
                fluentBuilderParameters.existingClass().className()
              )
          )
        )
        .content(new CommandContent(content))
        .scope(CommandScope.CLASS)
        .type(TargetType.CONSTRUCTOR)
    );

    return commands;
  }

  private List<BuilderCommand> builderMethod(FluentBuilderParameters fluentBuilderParameters) {
    List<BuilderCommand> commands = new ArrayList<>();

    if (isIsBuilderMethodExist(fluentBuilderParameters, "builder()")) {
      commands.add(DeleteCommand.builder().name(new TargetName("builder")).scope(CommandScope.CLASS).type(TargetType.METHOD));
    }

    String builderMethodSignature = "%s builder()".formatted(extractFirstInterfaceName(fluentBuilderParameters));
    commands.add(
      CreateCommand.builder()
        .signature(new CommandSignature("public static %s".formatted(builderMethodSignature)))
        .content(new CommandContent("return new %sBuilder();".formatted(fluentBuilderParameters.existingClass().className())))
        .scope(CommandScope.CLASS)
        .type(TargetType.METHOD)
    );
    return commands;
  }

  private static String extractFirstInterfaceName(FluentBuilderParameters fluentBuilderParameters) {
    Field firstField = fluentBuilderParameters.parameters().fields().stream().findFirst().orElseThrow();
    if (fluentBuilderParameters.parameters().fields().stream().allMatch(Field::isOptional)) {
      return "%sOptionalBuilder".formatted(fluentBuilderParameters.existingClass().className());
    }
    return "%s%sBuilder".formatted(fluentBuilderParameters.existingClass().className(), capitalize(firstField.name()));
  }

  private static Optional<DeleteCommand> deleteExistingBuilder(FluentBuilderParameters fluentBuilderParameters, String builderName) {
    if (fluentBuilderParameters.existingClass().isBuilderExist()) {
      return Optional.of(DeleteCommand.builder().name(new TargetName(builderName)).scope(CommandScope.CLASS).type(TargetType.CLASS));
    }
    return Optional.empty();
  }

  private static CreateCommand createBuilder(FluentBuilderParameters fluentBuilderParameters, String builderName) {
    StringBuilder builderSignature = new StringBuilder("private static final class %s".formatted(builderName));
    addImplementedInterfaces(fluentBuilderParameters, builderSignature);

    return CreateCommand.builder()
      .signature(new CommandSignature(builderSignature.toString()))
      .content(new CommandContent(null))
      .scope(CommandScope.CLASS)
      .type(TargetType.CLASS);
  }

  private static void addImplementedInterfaces(FluentBuilderParameters fluentBuilderParameters, StringBuilder builderSignature) {
    if (fluentBuilderParameters.parameters().fields().isEmpty()) return;

    builderSignature.append(" implements");
    fluentBuilderParameters
      .parameters()
      .fields()
      .stream()
      .filter(not(Field::isOptional))
      .forEach(field ->
        builderSignature.append(" %s%sBuilder,".formatted(fluentBuilderParameters.existingClass().className(), capitalize(field.name())))
      );
    if (fluentBuilderParameters.parameters().fields().stream().anyMatch(Field::isOptional)) {
      builderSignature.append(" %sOptionalBuilder,".formatted(fluentBuilderParameters.existingClass().className()));
    }
    builderSignature.deleteCharAt(builderSignature.length() - 1);
  }

  private static List<DeleteCommand> interfacesToRemove(ExistingClass existingClass) {
    return existingClass
      .builderInterfaces()
      .stream()
      .map(inter -> DeleteCommand.builder().name(new TargetName(inter.name())).scope(CommandScope.CLASS).type(TargetType.INTERFACE))
      .toList();
  }

  private static boolean isConstructorExists(FluentBuilderParameters fluentBuilderParameters) {
    String className = fluentBuilderParameters.existingClass().className(); // e.g., "Test"
    String regex = "\\b" + className + "\\([^)]*" + className + "Builder\\s+builder\\s*\\)";
    Pattern pattern = Pattern.compile(regex);
    return fluentBuilderParameters
      .existingClass()
      .classMethods()
      .stream()
      .anyMatch(method -> {
        Matcher matcher = pattern.matcher(method.signature());
        return matcher.find();
      });
  }

  private static boolean isIsBuilderMethodExist(FluentBuilderParameters fluentBuilderParameters, String builderMethodSignature) {
    return fluentBuilderParameters
      .existingClass()
      .classMethods()
      .stream()
      .anyMatch(method -> method.signature().contains(builderMethodSignature));
  }
}
