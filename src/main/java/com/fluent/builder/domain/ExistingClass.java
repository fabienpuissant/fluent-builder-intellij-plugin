package com.fluent.builder.domain;

import java.util.List;

public class ExistingClass {

    private boolean isBuilderExist;
    private List<Field> existingBuilderFields;
    private List<Interface> builderInterfaces;
    private List<Method> classMethods;
    private List<Method> builderMethods;

    private ExistingClass(ExistingClassBuilder builder) {
        if(builder.existingBuilderFields == null || builder.builderInterfaces == null || builder.classMethods == null || builder.builderMethods == null) throw  new IllegalStateException();

        this.isBuilderExist = isBuilderExist;
        this.existingBuilderFields = existingBuilderFields;
        this.builderInterfaces = builderInterfaces;
        this.classMethods = classMethods;
        this.builderMethods = builderMethods;
    }

    public static IsBuilderExistBuilder builder() {
        return new ExistingClassBuilder();
    }

    public sealed interface IsBuilderExistBuilder permits ExistingClassBuilder {
        ExistingBuilderFieldsBuilder isBuilderExist(boolean isBuilderExist);
    }

    public sealed interface ExistingBuilderFieldsBuilder permits ExistingClassBuilder {
        ExistingClassInterfacesBuilder existingBuilderFields(List<Field> existingBuilderFields);
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

    private static final class ExistingClassBuilder implements IsBuilderExistBuilder, ExistingBuilderFieldsBuilder, ExistingClassInterfacesBuilder, ExistingClassMethodsBuilder, ExistingClassBuilderMethodsBuilder {

        private boolean isBuilderExist;
        private List<Field> existingBuilderFields;
        private List<Interface> builderInterfaces;
        private List<Method> classMethods;
        private List<Method> builderMethods;

        private ExistingClassBuilder() {
        }

        @Override
        public ExistingBuilderFieldsBuilder isBuilderExist(boolean isBuilderExist) {
            this.isBuilderExist = isBuilderExist;

            return this;
        }

        @Override
        public ExistingClassInterfacesBuilder existingBuilderFields(List<Field> existingBuilderFields) {
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