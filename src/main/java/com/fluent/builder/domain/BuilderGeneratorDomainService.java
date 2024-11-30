package com.fluent.builder.domain;

import com.fluent.builder.domain.outputcommand.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.capitalize;

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

    private static List<BuilderCommand> interfacesCommands(FluentBuilderParameters fluentBuilderParameters) {
        List<BuilderCommand> commands = new ArrayList<>();
        commands.addAll(interfacesToRemove(fluentBuilderParameters.existingClass()));
        commands.addAll(interfacesToCreate(fluentBuilderParameters.parameters(),
                fluentBuilderParameters.existingClass().className()));
        return commands;
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
            commands.add(CreateCommand.builder()
                    .signature(new CommandSignature("private %s %s;".formatted(field.type(), field.name())))
                    .content(new CommandContent(null))
                    .scope(CommandScope.BUILDER)
                    .type(TargetType.FIELD));
        }
        return commands;
    }

    private List<CreateCommand> builderInterfacesCommands(FluentBuilderParameters fluentBuilderParameters) {
        String existingClassName = fluentBuilderParameters.existingClass().className();
        return createBuilderInterfaces(fluentBuilderParameters, existingClassName);
    }

    private List<CreateCommand> buildMethod(FluentBuilderParameters fluentBuilderParameters) {
        if (fluentBuilderParameters.parameters().fields().stream().noneMatch(Field::isOptional)) {
            return List.of();
        }
        return List.of(CreateCommand.builder()
                .signature(new CommandSignature("public %s build()".formatted(fluentBuilderParameters.existingClass().className())))
                .content(new CommandContent("return new %s(this);".formatted(fluentBuilderParameters.existingClass().className())))
                .scope(CommandScope.BUILDER)
                .type(TargetType.METHOD));
    }

    private List<BuilderCommand> buildConstructor(FluentBuilderParameters fluentBuilderParameters) {
        List<BuilderCommand> commands = new ArrayList<>();

        if (isConstructorExists(fluentBuilderParameters)) {
            commands.add(DeleteCommand.builder()
                    .name(new TargetName(fluentBuilderParameters.existingClass().className()))
                    .scope(CommandScope.CLASS)
                    .type(TargetType.CONSTRUCTOR));
        }

        String content = fluentBuilderParameters.parameters().fields().stream()
                .map(field -> "this.%s = builder.%s;".formatted(field.name(), field.name()))
                .collect(Collectors.joining("\n"));
        commands.add(CreateCommand.builder()
                .signature(new CommandSignature("private %s(%sBuilder builder)".formatted(fluentBuilderParameters.existingClass().className(), fluentBuilderParameters.existingClass().className())))
                .content(new CommandContent(content))
                .scope(CommandScope.CLASS)
                .type(TargetType.CONSTRUCTOR));

        return commands;
    }

    private List<BuilderCommand> builderMethod(FluentBuilderParameters fluentBuilderParameters) {
        List<BuilderCommand> commands = new ArrayList<>();
        String builderMethodSignature = "%s builder()".formatted(extractFirstInterfaceName(fluentBuilderParameters));

        if (isIsBuilderMethodExist(fluentBuilderParameters, builderMethodSignature)) {
            commands.add(DeleteCommand.builder()
                    .name(new TargetName("builder"))
                    .scope(CommandScope.CLASS)
                    .type(TargetType.METHOD));
        }

        commands.add(CreateCommand.builder()
                .signature(new CommandSignature("public static %s".formatted(builderMethodSignature)))
                .content(new CommandContent("return new %sBuilder();".formatted(fluentBuilderParameters.existingClass().className())))
                .scope(CommandScope.CLASS)
                .type(TargetType.METHOD));
        return commands;
    }


    private static String extractFirstInterfaceName(FluentBuilderParameters fluentBuilderParameters) {
        Field firstField = fluentBuilderParameters.parameters().fields().stream().findFirst().orElseThrow();
        return "%s%sBuilder".formatted(fluentBuilderParameters.existingClass().className(), capitalize(firstField.name()));
    }


    private static Optional<DeleteCommand> deleteExistingBuilder(FluentBuilderParameters fluentBuilderParameters, String builderName) {
        if (fluentBuilderParameters.existingClass().isBuilderExist()) {
            return Optional.of(DeleteCommand.builder()
                    .name(new TargetName(builderName))
                    .scope(CommandScope.CLASS)
                    .type(TargetType.CLASS));
        }
        return Optional.empty();
    }

    private static CreateCommand createBuilder(FluentBuilderParameters fluentBuilderParameters, String builderName) {
        StringBuilder builderSignature = new StringBuilder("public static final class %s".formatted(builderName));
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
        fluentBuilderParameters.parameters().fields().forEach(field -> builderSignature.append(" %s%sBuilder,".formatted(fluentBuilderParameters.existingClass().className(), capitalize(field.name()))));
        builderSignature.deleteCharAt(builderSignature.length() - 1);
    }

    private static List<DeleteCommand> interfacesToRemove(ExistingClass existingClass) {
        return existingClass.builderInterfaces().stream()
                .map(inter -> DeleteCommand.builder()
                        .name(new TargetName(inter.name()))
                        .scope(CommandScope.CLASS)
                        .type(TargetType.INTERFACE)
                ).toList();
    }

    private static List<CreateCommand> interfacesToCreate(Fields fields, String existingClassName) {
        return generateInterfaces(fields.fields().iterator(), existingClassName,
                (currentField, nextBuilder, nextReturn) -> CreateCommand.builder()
                        .signature(new CommandSignature(
                                "public sealed interface %s%sBuilder permits %sBuilder"
                                        .formatted(existingClassName, capitalize(currentField.name()), existingClassName)))
                        .content(new CommandContent(
                                "%s %s(%s %s);"
                                        .formatted(nextBuilder, currentField.name(), currentField.type(), currentField.name())))
                        .scope(CommandScope.CLASS)
                        .type(TargetType.INTERFACE)
        );
    }

    private static List<CreateCommand> createBuilderInterfaces(FluentBuilderParameters fluentBuilderParameters, String existingClassName) {
        return generateInterfaces(fluentBuilderParameters.parameters().fields().iterator(), existingClassName,
                (currentField, nextBuilder, nextReturn) -> CreateCommand.builder()
                        .signature(new CommandSignature("""
                                @Override
                                public %s %s(%s %s)
                                """.formatted(nextBuilder, currentField.name(), currentField.type(), currentField.name())))
                        .content(new CommandContent("""
                                this.%s = %s;
                                
                                return %s;""".formatted(currentField.name(), currentField.name(), nextReturn)))
                        .scope(CommandScope.BUILDER)
                        .type(TargetType.METHOD)
        );
    }

    @FunctionalInterface
    interface CommandGenerator<T> {
        T generate(Field currentField, String nextBuilder, String nextReturn);
    }

    private static <T> List<T> generateInterfaces(
            Iterator<Field> fieldIterator,
            String existingClassName,
            CommandGenerator<T> commandGenerator
    ) {
        List<T> commands = new ArrayList<>();
        Field currentField;
        Field nextField = fieldIterator.hasNext() ? fieldIterator.next() : null;

        while (nextField != null) {
            currentField = nextField;
            nextField = fieldIterator.hasNext() ? fieldIterator.next() : null;

            String nextBuilder = existingClassName;
            String nextReturn = "new %s(this)".formatted(existingClassName);

            if (nextField != null) {
                nextBuilder = nextField.isOptional() ? existingClassName : existingClassName + capitalize(nextField.name());
                nextBuilder += "Builder";
                nextReturn = "this";
            }

            commands.add(commandGenerator.generate(currentField, nextBuilder, nextReturn));
        }

        return commands;
    }

    private static boolean isConstructorExists(FluentBuilderParameters fluentBuilderParameters) {
        String className = fluentBuilderParameters.existingClass().className(); // e.g., "Test"
        String regex = "\\b" +className + "\\([^)]*" + className +  "Builder\\s+builder\\s*\\)";
        Pattern pattern = Pattern.compile(regex);
        return fluentBuilderParameters.existingClass().classMethods().stream()
                .anyMatch(method -> {
                    Matcher matcher = pattern.matcher(method.signature());
                    return matcher.find();
                });
    }


    private static boolean isIsBuilderMethodExist(FluentBuilderParameters fluentBuilderParameters, String builderMethodSignature) {
        return fluentBuilderParameters.existingClass().classMethods().stream().anyMatch(method -> method.signature().contains(builderMethodSignature));
    }
}
