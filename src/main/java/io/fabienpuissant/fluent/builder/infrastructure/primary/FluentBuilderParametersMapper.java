package io.fabienpuissant.fluent.builder.infrastructure.primary;

import com.intellij.psi.*;
import io.fabienpuissant.fluent.builder.domain.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class FluentBuilderParametersMapper {

  private FluentBuilderParametersMapper() {}

  public static FluentBuilderParameters toDomain(PluginContext context, List<PsiField> mandatoryFields, List<PsiField> optionalFields) {
    return FluentBuilderParameters.builder()
      .context(mapContext(context))
      .parameters(mapFields(mandatoryFields, false).concat(mapFields(optionalFields, true)));
  }

  private static Fields mapFields(List<PsiField> fields, boolean isOptional) {
    return new Fields(
      fields
        .stream()
        .map(field -> Field.builder().name(field.getName()).type(field.getType().getPresentableText()).isOptional(isOptional))
        .toList()
    );
  }

  private static ExistingClass mapContext(PluginContext context) {
    PsiClass psiClass = context.ownerClass();
    return ExistingClass.builder()
      .className(context.ownerClass().getName())
      .isBuilderExist(isBuilderExist(psiClass))
      .interfaces(getInterfaces(psiClass))
      .classMethods(extractMethods(psiClass));
  }

  private static List<Interface> getInterfaces(PsiClass psiClass) {
    return extractInterfaces(psiClass).stream().map(FluentBuilderParametersMapper::mapInterface).toList();
  }

  private static Interface mapInterface(PsiClass psiClass) {
    return Interface.builder().name(psiClass.getName()).signatures(extractInterfaceSignature(psiClass));
  }

  private static boolean isBuilderExist(PsiClass psiClass) {
    return getBuilder(psiClass) != null;
  }

  private static String builderName(String className) {
    return className + "Builder";
  }

  private static @Nullable PsiClass getBuilder(PsiClass psiClass) {
    return psiClass.findInnerClassByName(builderName(psiClass.getName()), false);
  }

  private static List<PsiClass> extractInterfaces(PsiClass psiClass) {
    return Arrays.stream(psiClass.getInnerClasses()).filter(PsiClass::isInterface).toList();
  }

  private static List<String> extractInterfaceSignature(PsiClass psiClass) {
    List<String> methodSignatures = new ArrayList<>();

    for (PsiMethod method : psiClass.getMethods()) {
      StringBuilder signatureBuilder = new StringBuilder();

      // Add return type
      PsiType returnType = method.getReturnType();
      if (returnType != null) {
        signatureBuilder.append(returnType.getCanonicalText()).append(" ");
      }

      // Add method name
      signatureBuilder.append(method.getName()).append("(");

      // Add parameters
      PsiParameter[] parameters = method.getParameterList().getParameters();
      for (int i = 0; i < parameters.length; i++) {
        PsiParameter parameter = parameters[i];
        signatureBuilder.append(parameter.getType().getCanonicalText()).append(" ").append(parameter.getName());
        if (i < parameters.length - 1) {
          signatureBuilder.append(", ");
        }
      }

      signatureBuilder.append(");");

      methodSignatures.add(signatureBuilder.toString());
    }

    return methodSignatures;
  }

  public static List<Method> extractMethods(PsiClass psiClass) {
    List<Method> methods = new ArrayList<>();

    if (psiClass != null) {
      for (PsiMethod psiMethod : psiClass.getMethods()) {
        String signature = buildMethodSignature(psiMethod);
        String content = extractMethodContent(psiMethod);

        // Build and add the Method instance
        methods.add(Method.builder().signature(signature).content(content));
      }
    }

    return methods;
  }

  private static String buildMethodSignature(PsiMethod psiMethod) {
    StringBuilder signatureBuilder = new StringBuilder();

    // Add return type
    PsiType returnType = psiMethod.getReturnType();
    if (returnType != null) {
      signatureBuilder.append(returnType.getCanonicalText()).append(" ");
    }

    // Add method name
    signatureBuilder.append(psiMethod.getName()).append("(");

    // Add parameters
    PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
    for (int i = 0; i < parameters.length; i++) {
      PsiParameter parameter = parameters[i];
      signatureBuilder.append(parameter.getType().getCanonicalText()).append(" ").append(parameter.getName());
      if (i < parameters.length - 1) {
        signatureBuilder.append(", ");
      }
    }

    signatureBuilder.append(");");
    return signatureBuilder.toString();
  }

  private static String extractMethodContent(PsiMethod psiMethod) {
    PsiCodeBlock body = psiMethod.getBody();
    if (body != null) {
      String bodyText = body.getText();
      return bodyText.substring(1, bodyText.length() - 1).trim();
    }
    return "";
  }
}
