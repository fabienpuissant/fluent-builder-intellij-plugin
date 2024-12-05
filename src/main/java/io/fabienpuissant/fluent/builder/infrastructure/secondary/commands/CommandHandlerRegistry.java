package io.fabienpuissant.fluent.builder.infrastructure.secondary.commands;

import io.fabienpuissant.fluent.builder.domain.outputcommand.TargetType;

import java.util.HashMap;
import java.util.Map;

public class CommandHandlerRegistry {
    private final Map<TargetType, CommandHandler> handlers = new HashMap<>();

    public CommandHandlerRegistry() {
        handlers.put(TargetType.FIELD, new FieldCommandHandler());
        handlers.put(TargetType.METHOD, new MethodCommandHandler());
        handlers.put(TargetType.CONSTRUCTOR, new ConstructorCommandHandler());
        handlers.put(TargetType.INTERFACE, new InterfaceCommandHandler());
        handlers.put(TargetType.CLASS, new ClassCommandHandler());
    }

    public CommandHandler getHandler(TargetType commandType) {
        return handlers.get(commandType);
    }
}
