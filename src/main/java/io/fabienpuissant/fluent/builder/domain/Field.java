package io.fabienpuissant.fluent.builder.domain;

public class Field {

  private final String name;
  private final String type;
  private final boolean isOptional;

  public String name() {
    return name;
  }

  public String type() {
    return type;
  }

  public boolean isOptional() {
    return isOptional;
  }

  private Field(FieldBuilder builder) {
    assert builder.name != null && builder.type != null;

    this.name = builder.name;
    this.type = builder.type;
    this.isOptional = builder.isOptional;
  }

  public static FieldNameBuilder builder() {
    return new FieldBuilder();
  }

  public sealed interface FieldNameBuilder permits FieldBuilder {
    FieldTypeBuilder name(String name);
  }

  public sealed interface FieldTypeBuilder permits FieldBuilder {
    FieldIsOptionalBuilder type(String type);
  }

  public sealed interface FieldIsOptionalBuilder permits FieldBuilder {
    Field isOptional(boolean isOptional);
  }

  private static final class FieldBuilder implements FieldNameBuilder, FieldTypeBuilder, FieldIsOptionalBuilder {

    private String name;
    private String type;
    private boolean isOptional;

    @Override
    public FieldTypeBuilder name(String name) {
      this.name = name;

      return this;
    }

    @Override
    public FieldIsOptionalBuilder type(String type) {
      this.type = type;

      return this;
    }

    @Override
    public Field isOptional(boolean isOptional) {
      this.isOptional = isOptional;

      return new Field(this);
    }
  }
}
