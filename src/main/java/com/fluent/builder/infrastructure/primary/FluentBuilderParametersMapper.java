package com.fluent.builder.infrastructure.primary;

import com.fluent.builder.domain.ExistingClass;
import com.fluent.builder.domain.Fields;
import com.fluent.builder.domain.FluentBuilderParameters;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.List;

public class FluentBuilderParametersMapper {

    private FluentBuilderParametersMapper() {}

    public static FluentBuilderParameters toDomain(PluginContext context, List<PsiField> mandatoryFields, List<PsiField> optionalFields) {
        return FluentBuilderParameters.builder()
                .context(mapContext(context))
                .mandatoryParameters(mapFields(mandatoryFields))
                .optionalParameters(mapFields(optionalFields));
    }

    private static Fields mapFields(List<PsiField> mandatoryFields) {
        return null;
    }

    private static ExistingClass mapContext(PluginContext context) {
        return ExistingClass.builder()
                .isBuilderExist(isBuilderExist(context.ownerClass()))
                .existingBuilderFields(null)
                .builderInterfaces(null)
                .classMethods(null)
                .builderMethods(null);
    }

    private static boolean isBuilderExist(PsiClass psiClass) {
        return false;
    }

}
