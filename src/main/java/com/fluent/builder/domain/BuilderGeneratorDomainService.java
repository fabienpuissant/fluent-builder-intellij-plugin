package com.fluent.builder.domain;

import com.fluent.builder.domain.outputcommand.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        commands.add(createConstructor(fluentBuilderParameters));
        builderMethod(fluentBuilderParameters).ifPresent(commands::add);
        builderPort.generateBuilder(new BuilderCommandOutput(commands));
    }

    private Optional<CreateCommand> builderMethod(FluentBuilderParameters fluentBuilderParameters) {
        if (fluentBuilderParameters.parameters().fields().isEmpty()) return Optional.empty();
        return Optional.of(CreateCommand.builder()
                .signature(new CommandSignature("public static %s builder()".formatted(extractFirstInterfaceName(fluentBuilderParameters))))
                .content(new CommandContent("return new %sBuilder();".formatted(fluentBuilderParameters.existingClass().className())))
                .scope(CommandScope.CLASS));
    }

    private String extractFirstInterfaceName(FluentBuilderParameters fluentBuilderParameters) {
        Field firstField = fluentBuilderParameters.parameters().fields().stream().findFirst().orElseThrow();
        return "%s%sBuilder".formatted(fluentBuilderParameters.existingClass().className(), capitalize(firstField.name()));
    }

    private CreateCommand createConstructor(FluentBuilderParameters fluentBuilderParameters) {
        StringBuilder content = new StringBuilder();
        fluentBuilderParameters.parameters().fields().forEach(field -> content.append("this.%s = builder.%s;\n".formatted(field.name(), field.name())));
        return CreateCommand.builder()
                .signature(new CommandSignature("private %s(%sBuilder builder)".formatted(fluentBuilderParameters.existingClass().className(), fluentBuilderParameters.existingClass().className())))
                .content(new CommandContent(content.toString()))
                .scope(CommandScope.CLASS);
    }

    private List<CreateCommand> buildMethod(FluentBuilderParameters fluentBuilderParameters) {
        if(fluentBuilderParameters.parameters().fields().stream().noneMatch(Field::isOptional)) {
            return List.of();
        }
        return List.of(CreateCommand.builder()
                .signature(new CommandSignature("public %s build()".formatted(fluentBuilderParameters.existingClass().className())))
                .content(new CommandContent("return new %s(this);".formatted(fluentBuilderParameters.existingClass().className())))
                .scope(CommandScope.BUILDER));
    }


    private List<BuilderCommand> builderFields(FluentBuilderParameters fluentBuilderParameters) {
        List<BuilderCommand> commands = new ArrayList<>();
        for (Field field: fluentBuilderParameters.parameters().fields()) {
            boolean fieldNotExistInBuilder = fluentBuilderParameters.existingClass().existingBuilderFields().stream().noneMatch(builderField -> builderField.name().equals(field.name()));
            if(fieldNotExistInBuilder) {
                commands.add(CreateCommand.builder()
                        .signature(new CommandSignature("private %s %s;".formatted(field.type(), field.name())))
                        .content(new CommandContent(null))
                        .scope(CommandScope.BUILDER));
            }
        }

        for (BuilderField builderField: fluentBuilderParameters.existingClass().existingBuilderFields()) {
            boolean fieldNotExistInParams = fluentBuilderParameters.parameters().fields().stream().noneMatch(field -> builderField.name().equals(field.name()));
            if(fieldNotExistInParams) {
                commands.add(DeleteCommand.builder()
                                .name(new TargetName(builderField.name()))
                                .scope(CommandScope.BUILDER)
                                .type(TargetType.FIELD));
            }
        }

        return commands;
    }

    private List<BuilderCommand> builderCommands(FluentBuilderParameters fluentBuilderParameters) {
        if (fluentBuilderParameters.existingClass().isBuilderExist()) return List.of();

        StringBuilder builderSignature = new StringBuilder("private static final class %sBuilder".formatted(fluentBuilderParameters.existingClass().className()));
        addImplementedInterfaces(fluentBuilderParameters, builderSignature);

        return List.of(CreateCommand.builder()
                .signature(new CommandSignature(builderSignature.toString()))
                .content(new CommandContent(null))
                .scope(CommandScope.CLASS));
    }

    private static void addImplementedInterfaces(FluentBuilderParameters fluentBuilderParameters, StringBuilder builderSignature) {
        if(fluentBuilderParameters.parameters().fields().isEmpty()) return;

        builderSignature.append(" implements");
        fluentBuilderParameters.parameters().fields().forEach(field -> builderSignature.append(" %sBuilder,".formatted(capitalize(field.name()))));
        builderSignature.deleteCharAt(builderSignature.length() - 1);
    }

    private static List<BuilderCommand> interfacesCommands(FluentBuilderParameters fluentBuilderParameters) {
        List<BuilderCommand> commands = new ArrayList<>();
        commands.addAll(interfacesToRemove(fluentBuilderParameters.existingClass()));
        commands.addAll(interfacesToCreate(fluentBuilderParameters.parameters(),
                fluentBuilderParameters.existingClass().className()));
        return commands;
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
        );
    }

    private List<BuilderCommand> builderInterfacesCommands(FluentBuilderParameters fluentBuilderParameters) {
        String existingClassName = fluentBuilderParameters.existingClass().className();
        List<BuilderCommand> commands = new ArrayList<>();
        commands.addAll(deleteAllBuilderInterfaces(fluentBuilderParameters));
        commands.addAll(createBuilderInterfaces(fluentBuilderParameters, existingClassName));
        return commands;
    }

    private List<DeleteCommand> deleteAllBuilderInterfaces(FluentBuilderParameters fluentBuilderParameters) {
        return fluentBuilderParameters.existingClass().builderMethods().stream().map(interfaceToDelete -> DeleteCommand.builder()
                .name(new TargetName(extractMethodName(interfaceToDelete.signature())))
                .scope(CommandScope.BUILDER)
                .type(TargetType.METHOD)).toList();
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
                                
                                return %s;
                                """.formatted(currentField.name(), currentField.name(), nextReturn)))
                        .scope(CommandScope.BUILDER)
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
        Field currentField = null;
        Field nextField = fieldIterator.hasNext() ? fieldIterator.next() : null;

        while (nextField != null) {
            currentField = nextField;
            nextField = fieldIterator.hasNext() ? fieldIterator.next() : null;

            String nextBuilder = existingClassName;
            String nextReturn = "new %s(this)".formatted(existingClassName);

            if (nextField != null) {
                nextBuilder = currentField.isOptional() ? existingClassName : existingClassName + capitalize(nextField.name());
                nextBuilder += "Builder";
                nextReturn = "this";
            }

            commands.add(commandGenerator.generate(currentField, nextBuilder, nextReturn));
        }

        return commands;
    }

    public static String extractMethodName(String signature) {
        Pattern pattern = Pattern.compile("\\b(\\w+)\\s*\\(");
        Matcher matcher = pattern.matcher(signature);

        if (matcher.find()) {
            return matcher.group(1);
        }

        throw new IllegalArgumentException("Invalid method signature: " + signature);
    }
}
