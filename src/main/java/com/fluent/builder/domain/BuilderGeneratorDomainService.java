package com.fluent.builder.domain;

import com.fluent.builder.domain.outputcommand.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
        commands.addAll(builderInterfacesImplementations(fluentBuilderParameters));
        builderPort.generateBuilder(new BuilderCommandOutput(commands));
    }

    private List<BuilderCommand> builderInterfacesImplementations(FluentBuilderParameters fluentBuilderParameters) {
        List<BuilderCommand> commands = new ArrayList<>();
        String existingClassName = fluentBuilderParameters.existingClass().className();

        Iterator<Field> fieldIterator = fluentBuilderParameters.parameters().fields().iterator();

        Field currentField = null;
        Field nextField = fieldIterator.hasNext() ? fieldIterator.next() : null;

        while (nextField != null) {
            currentField = nextField;
            nextField = fieldIterator.hasNext() ? fieldIterator.next() : null;

            String nextBuilder = existingClassName;
            String nextReturn = "new %s(this);".formatted(existingClassName);

            if(nextField != null) {
                nextBuilder = currentField.isOptional() ? existingClassName :  existingClassName + capitalize(nextField.name());
                nextBuilder += "Builder";
                nextReturn = "this";
            }

            commands.add(CreateCommand.builder()
                    .signature(new CommandSignature("""
                            @Override
                            public %s %s(%s %s)
                            """.formatted(nextBuilder, currentField.name(), currentField.type(), currentField.name())))
                    .content(new CommandContent("""
                            this.%s = %s;
                            
                            return %s;
                            """.formatted(currentField.name(), currentField.name(), nextReturn)))
                    .scope(CommandScope.BUILDER));
        }

        return commands;
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
        List<CreateCommand> commands = new ArrayList<>();
        Iterator<Field> fieldIterator = fields.fields().iterator();

        Field currentField = null;
        Field nextField = fieldIterator.hasNext() ? fieldIterator.next() : null;

        while (nextField != null) {
            currentField = nextField;
            nextField = fieldIterator.hasNext() ? fieldIterator.next() : null;

            String nextBuilder = existingClassName;

            if(nextField != null) {
                nextBuilder = currentField.isOptional() ? existingClassName :  existingClassName + capitalize(nextField.name());
                nextBuilder += "Builder";
            }

            commands.add(CreateCommand.builder()
                    .signature(new CommandSignature(
                            "public sealed interface %s%sBuilder permits %sBuilder"
                                    .formatted(existingClassName, capitalize(currentField.name()), existingClassName)))
                    .content(new CommandContent(
                            "%s %s(%s %s);"
                                    .formatted(nextBuilder, currentField.name(), currentField.type(), currentField.name())))
                    .scope(CommandScope.CLASS));
        }

        return commands;
    }
}
