package com.fluent.builder;

public class FieldData {

    private String name;
    private String type;
    private boolean isOptional;

    public FieldData(String name, String type, boolean isOptional) {
        this.name = name;
        this.type = type;
        this.isOptional = isOptional;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }
}
