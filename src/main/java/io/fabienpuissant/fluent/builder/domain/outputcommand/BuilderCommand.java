package io.fabienpuissant.fluent.builder.domain.outputcommand;

public interface BuilderCommand {
  CommandScope scope();

  TargetType type();
}
