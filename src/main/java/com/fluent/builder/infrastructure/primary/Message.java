package com.fluent.builder.infrastructure.primary;

public enum Message {
    SELECT_FIELDS("Select Fields"),
    FIELDS_SELECTION_TITLE("Select fields for the Sealed Fluent Builder (optional fields can be explicitly selected later)"),
    SELECT_OPTIONAL_FIELDS("Select Optional Fields (the Order Is Respected by the Builder)"),
    REQUIRED_FIELDS("Required fields:"),
    OPTIONAL_FIELDS("Optional fields:"),
    MOVE_DOWN("Move Down"),
    MOVE_UP("Move UP");


    private final String label;

    Message(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
