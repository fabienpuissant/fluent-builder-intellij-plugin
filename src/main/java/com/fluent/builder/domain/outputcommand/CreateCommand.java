package com.fluent.builder.domain.outputcommand;

import java.util.Objects;

public class CreateCommand extends BuilderCommand {
    private CommandContent content;

    private CreateCommand(CreateCommandBuilder builder) {
        super(builder.signature);

        if(builder.content == null) throw new IllegalStateException();
        this.content = builder.content;
    }

    public static CommandSignatureBuilder builder() {
        return new CreateCommandBuilder();
    }

    public sealed interface CommandSignatureBuilder permits CreateCommandBuilder {
        CommandContentBuilder signature(CommandSignature signature);
    }


    public sealed interface CommandContentBuilder permits CreateCommandBuilder {
        CreateCommand content(CommandContent content);
    }


    private static final class CreateCommandBuilder implements
            CommandSignatureBuilder, CommandContentBuilder {
        private CommandSignature signature;
        private CommandContent content;

        @Override
        public CommandContentBuilder signature(CommandSignature signature) {
            if(signature == null) throw new IllegalStateException();
            this.signature = signature;

            return this;
        }

        @Override
        public CreateCommand content(CommandContent content) {
            this.content = content;

            return new CreateCommand(this);
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof CreateCommand that)) return false;

        return Objects.equals(content, that.content) && Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(content);
        result = 31 * result + Objects.hashCode(signature);
        return result;
    }
}
