package com.fluent.builder.domain.outputcommand;

import java.util.Objects;

public class DeleteCommand implements BuilderCommand {

    private final TargetName name;
    private final CommandScope scope;
    private final TargetType type;

    public TargetName name() {
        return name;
    }

    @Override
    public CommandScope scope() {
        return scope;
    }

    @Override
    public TargetType type() {
        return type;
    }

    private DeleteCommand(DeleteCommandBuilder builder) {
        assert builder.name != null && builder.scope != null && builder.type != null;

        this.name = builder.name;
        this.scope = builder.scope;
        this.type = builder.type;
    }

    public static DeleteCommandTargetNameBuilder builder() {
        return new DeleteCommandBuilder();
    }

    public sealed interface DeleteCommandTargetNameBuilder permits DeleteCommandBuilder {
        DeleteCommandScopeBuilder name(TargetName name);
    }

    public sealed interface DeleteCommandScopeBuilder permits DeleteCommandBuilder {
        DeleteCommandTypeBuilder scope(CommandScope scope);
    }

    public sealed interface DeleteCommandTypeBuilder permits DeleteCommandBuilder {
        DeleteCommand type(TargetType type);
    }

    private static final class DeleteCommandBuilder implements DeleteCommandTargetNameBuilder, DeleteCommandScopeBuilder, DeleteCommandTypeBuilder {

        private TargetName name;
        private CommandScope scope;
        private TargetType type;

        @Override
        public DeleteCommandScopeBuilder name(TargetName name) {
            this.name = name;

            return this;
        }

        @Override
        public DeleteCommandTypeBuilder scope(CommandScope scope) {
            this.scope = scope;

            return this;
        }

        @Override
        public DeleteCommand type(TargetType type) {
            this.type = type;

            return new DeleteCommand(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        DeleteCommand that = (DeleteCommand) o;
        return Objects.equals(name, that.name) && scope == that.scope && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(scope);
        result = 31 * result + Objects.hashCode(type);
        return result;
    }

    @Override
    public String toString() {
        return "DeleteCommand{" +
                "name=" + name +
                ", scope=" + scope +
                ", type=" + type +
                '}';
    }
}
