package io.fabienpuissant.fluent.builder.domain.outputcommand;

import java.util.Objects;

public class CreateCommand implements BuilderCommand {
    private final CommandSignature signature;
    private final CommandContent content;
    private final CommandScope scope;
    private final TargetType type;

    @Override
    public CommandScope scope() {
        return scope;
    }

    public CommandContent content() {
        return content;
    }

    public CommandSignature signature() {
        return signature;
    }

    @Override
    public TargetType type() {
        return type;
    }

    private CreateCommand(CreateCommandBuilder builder) {
        assert builder.signature != null && builder.scope != null && builder.content != null && builder.type != null;

        this.signature = builder.signature;
        this.content = builder.content;
        this.scope = builder.scope;
        this.type = builder.type;
    }

    public static CommandSignatureBuilder builder() {
        return new CreateCommandBuilder();
    }

    public sealed interface CommandSignatureBuilder permits CreateCommandBuilder {
        CommandContentBuilder signature(CommandSignature signature);
    }

    public sealed interface CommandContentBuilder permits CreateCommandBuilder {
        CommandScopeBuilder content(CommandContent content);
    }

    public sealed interface CommandScopeBuilder permits CreateCommandBuilder {
        CreateCommandTypeBuilder scope(CommandScope scope);
    }

    public sealed interface CreateCommandTypeBuilder permits CreateCommandBuilder {
        CreateCommand type(TargetType type);
    }


    private static final class CreateCommandBuilder implements
            CommandContentBuilder, CommandSignatureBuilder, CommandScopeBuilder, CreateCommandTypeBuilder {

        private CommandSignature signature;
        private CommandContent content;
        private CommandScope scope;
        private TargetType type;


        @Override
        public CommandContentBuilder signature(CommandSignature signature) {
            assert signature != null;

            this.signature = signature;

            return this;
        }

        @Override
        public CommandScopeBuilder content(CommandContent content) {
            this.content = content;

            return this;
        }

        @Override
        public CreateCommandTypeBuilder scope(CommandScope scope) {
            this.scope = scope;

            return this;
        }

        @Override
        public CreateCommand type(TargetType type) {
            this.type = type;

            return new CreateCommand(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        CreateCommand that = (CreateCommand) o;
        return signature.equals(that.signature) && Objects.equals(content, that.content) && scope == that.scope;
    }

    @Override
    public int hashCode() {
        int result = signature.hashCode();
        result = 31 * result + Objects.hashCode(content);
        result = 31 * result + scope.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CreateCommand{" +
                "signature=" + signature +
                ", content=" + content +
                ", scope=" + scope +
                '}';
    }
}
