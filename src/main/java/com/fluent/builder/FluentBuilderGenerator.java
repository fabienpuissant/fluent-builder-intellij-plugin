package com.fluent.builder;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiJavaFile;

import java.util.List;

public class FluentBuilderGenerator {
    private final PsiClass targetClass;
    private final List<FieldData> fields;

    public FluentBuilderGenerator(PsiClass targetClass, List<FieldData> fields) {
        this.targetClass = targetClass;
        this.fields = fields;
    }

    public void generate() {
        PsiElementFactory factory = PsiElementFactory.getInstance(targetClass.getProject());

        // Generate builder class code
        StringBuilder builderClass = new StringBuilder("public static class Builder {\n");
        for (FieldData field : fields) {
            builderClass.append("    private ").append(field.getType()).append(" ").append(field.getName()).append(";\n");

            builderClass.append("    public Builder ").append(field.getName()).append("(")
                    .append(field.getType()).append(" ").append(field.getName()).append(") {\n")
                    .append("        this.").append(field.getName()).append(" = ").append(field.getName()).append(";\n")
                    .append("        return this;\n")
                    .append("    }\n");
        }

        builderClass.append("    public ").append(targetClass.getName()).append(" build() {\n")
                .append("        return new ").append(targetClass.getName()).append("(this);\n")
                .append("    }\n")
                .append("}\n");

        // Add builder class to the current file
        PsiJavaFile file = (PsiJavaFile) targetClass.getContainingFile();
        file.getClasses()[0].add(factory.createClassFromText(builderClass.toString(), null));
    }
}
