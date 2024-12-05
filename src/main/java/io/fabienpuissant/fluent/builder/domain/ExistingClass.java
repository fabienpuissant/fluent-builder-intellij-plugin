package io.fabienpuissant.fluent.builder.domain;

import java.util.List;

public class ExistingClass {

    private final String className;
    private final boolean isBuilderExist;
    private final List<Interface> builderInterfaces;
    private final List<Method> classMethods;

    public String className() {
        return className;
    }

    public boolean isBuilderExist() {
        return isBuilderExist;
    }

    public List<Interface> builderInterfaces() {
        return builderInterfaces;
    }

    public List<Method> classMethods() {
        return classMethods;
    }

    private ExistingClass(ExistingClassBuilder builder) {
        assert builder.className != null && builder.builderInterfaces != null && builder.classMethods != null;

        this.className = builder.className;
        this.isBuilderExist = builder.isBuilderExist;
        this.builderInterfaces = builder.builderInterfaces;
        this.classMethods = builder.classMethods;
    }

    public static ExistingClassNameBuilder builder() {
        return new ExistingClassBuilder();
    }

    public sealed interface ExistingClassNameBuilder permits ExistingClassBuilder {
        IsBuilderExistBuilder className(String className);
    }

    public sealed interface IsBuilderExistBuilder permits ExistingClassBuilder {
        ExistingClassInterfacesBuilder isBuilderExist(boolean isBuilderExist);
    }

    public sealed interface ExistingClassInterfacesBuilder permits ExistingClassBuilder {
        ExistingClassMethodsBuilder interfaces(List<Interface> builderInterfaces);
    }

    public sealed interface ExistingClassMethodsBuilder permits ExistingClassBuilder {
        ExistingClass classMethods(List<Method> classMethods);
    }

    private static final class ExistingClassBuilder implements ExistingClassNameBuilder, IsBuilderExistBuilder, ExistingClassInterfacesBuilder, ExistingClassMethodsBuilder {

        private String className;
        private boolean isBuilderExist;
        private List<Interface> builderInterfaces;
        private List<Method> classMethods;

        private ExistingClassBuilder() {
        }

        @Override
        public IsBuilderExistBuilder className(String className) {
            this.className = className;

            return this;
        }

        @Override
        public ExistingClassInterfacesBuilder isBuilderExist(boolean isBuilderExist) {
            this.isBuilderExist = isBuilderExist;

            return this;
        }


        @Override
        public ExistingClassMethodsBuilder interfaces(List<Interface> builderInterfaces) {
            this.builderInterfaces = builderInterfaces;

            return this;
        }

        @Override
        public ExistingClass classMethods(List<Method> classMethods) {
            this.classMethods = classMethods;

            return new ExistingClass(this);
        }
    }
}