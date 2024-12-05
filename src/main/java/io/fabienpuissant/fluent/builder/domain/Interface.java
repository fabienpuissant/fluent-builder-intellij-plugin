package io.fabienpuissant.fluent.builder.domain;

import java.util.List;

public class Interface {

  private final String name;
  private final List<String> signatures;

  public String name() {
    return name;
  }

  public List<String> signatures() {
    return signatures;
  }

  private Interface(InterfaceBuilder builder) {
    assert builder.name != null && builder.signatures != null;

    this.name = builder.name;
    this.signatures = builder.signatures;
  }

  public static InterfaceSignatureBuilder builder() {
    return new InterfaceBuilder();
  }

  public sealed interface InterfaceSignatureBuilder permits InterfaceBuilder {
    InterfaceContentBuilder name(final String name);
  }

  public sealed interface InterfaceContentBuilder permits InterfaceBuilder {
    Interface signatures(final List<String> signatures);
  }

  private static final class InterfaceBuilder implements InterfaceSignatureBuilder, InterfaceContentBuilder {

    private String name;
    private List<String> signatures;

    private InterfaceBuilder() {}

    @Override
    public InterfaceContentBuilder name(final String name) {
      this.name = name;

      return this;
    }

    @Override
    public Interface signatures(final List<String> signatures) {
      this.signatures = signatures;

      return new Interface(this);
    }
  }
}
