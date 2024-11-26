package com.fluent.builder.domain;

import java.util.List;

public class ExistingClass {

    private String className;
    private boolean isBuilderExist;
    private List<BuilderField> existingBuilderFields;
    private List<Interface> builderInterfaces;
    private List<Method> classMethods;
    private List<Method> builderMethods;

    public String className() {
        return className;
    }

    public boolean isBuilderExist() {
        return isBuilderExist;
    }

    public List<BuilderField> existingBuilderFields() {
        return existingBuilderFields;
    }

    public List<Interface> builderInterfaces() {
        return builderInterfaces;
    }

    public List<Method> classMethods() {
        return classMethods;
    }

    public List<Method> builderMethods() {
        return builderMethods;
    }

    private ExistingClass(ExistingClassBuilder builder) {
        assert builder.className != null && builder.existingBuilderFields != null && builder.builderInterfaces != null && builder.classMethods != null && builder.builderMethods != null;

        this.className = builder.className;
        this.isBuilderExist = builder.isBuilderExist;
        this.existingBuilderFields = builder.existingBuilderFields;
        this.builderInterfaces = builder.builderInterfaces;
        this.classMethods = builder.classMethods;
        this.builderMethods = builder.builderMethods;
    }

    public static ExistingClassNameBuilder builder() {
        return new ExistingClassBuilder();
    }

    public sealed interface ExistingClassNameBuilder permits ExistingClassBuilder {
        IsBuilderExistBuilder className(String className);
    }

    public sealed interface IsBuilderExistBuilder permits ExistingClassBuilder {
        ExistingBuilderFieldsBuilder isBuilderExist(boolean isBuilderExist);
    }

    public sealed interface ExistingBuilderFieldsBuilder permits ExistingClassBuilder {
        ExistingClassInterfacesBuilder existingBuilderFields(List<BuilderField> existingBuilderFields);
    }

    public sealed interface ExistingClassInterfacesBuilder permits ExistingClassBuilder {
        ExistingClassMethodsBuilder interfaces(List<Interface> builderInterfaces);
    }

    public sealed interface ExistingClassMethodsBuilder permits ExistingClassBuilder {
        ExistingClassBuilderMethodsBuilder classMethods(List<Method> classMethods);
    }

    public sealed interface ExistingClassBuilderMethodsBuilder permits ExistingClassBuilder {
        ExistingClass builderMethods(List<Method> builderMethods);
    }

    private static final class ExistingClassBuilder implements ExistingClassNameBuilder, IsBuilderExistBuilder, ExistingBuilderFieldsBuilder, ExistingClassInterfacesBuilder, ExistingClassMethodsBuilder, ExistingClassBuilderMethodsBuilder {

        private String className;
        private boolean isBuilderExist;
        private List<BuilderField> existingBuilderFields;
        private List<Interface> builderInterfaces;
        private List<Method> classMethods;
        private List<Method> builderMethods;

        private ExistingClassBuilder() {
        }

        @Override
        public IsBuilderExistBuilder className(String className) {
            this.className = className;

            return this;
        }

        @Override
        public ExistingBuilderFieldsBuilder isBuilderExist(boolean isBuilderExist) {
            this.isBuilderExist = isBuilderExist;

            return this;
        }

        @Override
        public ExistingClassInterfacesBuilder existingBuilderFields(List<BuilderField> existingBuilderFields) {
            this.existingBuilderFields = existingBuilderFields;

            return this;
        }

        @Override
        public ExistingClassMethodsBuilder interfaces(List<Interface> builderInterfaces) {
            this.builderInterfaces = builderInterfaces;

            return this;
        }

        @Override
        public ExistingClassBuilderMethodsBuilder classMethods(List<Method> classMethods) {
            this.classMethods = classMethods;

            return this;
        }

        @Override
        public ExistingClass builderMethods(List<Method> builderMethods) {
            this.builderMethods = builderMethods;
            return new ExistingClass(this);
        }

    }


}