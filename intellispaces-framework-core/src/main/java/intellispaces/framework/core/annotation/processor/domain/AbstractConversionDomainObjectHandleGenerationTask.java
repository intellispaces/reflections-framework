package intellispaces.framework.core.annotation.processor.domain;

import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.common.javastatement.reference.TypeReferenceFunctions;

import javax.annotation.processing.RoundEnvironment;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class AbstractConversionDomainObjectHandleGenerationTask extends AbstractDomainObjectHandleGenerationTask {
  protected final CustomTypeReference parentDomainType;
  protected String childFieldName;

  public AbstractConversionDomainObjectHandleGenerationTask(
      CustomType initiatorType, CustomType customType, CustomTypeReference parentDomainType
  ) {
    super(initiatorType, customType);
    this.parentDomainType = parentDomainType;
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, RoundEnvironment roundEnv
  ) {
    return buildActualType(customType, roundEnv)
        .actualMethods().stream()
        .filter(this::isNotGetDomainMethod);
  }

  protected void buildReturnStatement(StringBuilder sb, MethodStatement method) {
    MethodStatement actualParentMethod = parentDomainType.asCustomTypeReferenceOrElseThrow().targetType().actualMethod(
        method.name(), method.parameterTypes()
    ).orElseThrow();
    TypeReference parentReturnTypeRef = actualParentMethod.returnType().orElseThrow();

    MethodStatement actualChildMethod = annotatedType.actualMethod(method.name(), method.parameterTypes())
        .orElseThrow();
    TypeReference childReturnTypeRef = actualChildMethod.returnType().orElseThrow();

    if (TypeReferenceFunctions.isEqualTypes(parentReturnTypeRef, childReturnTypeRef)) {
      buildDirectReturnStatement(sb, method);
    } else {
      if (parentReturnTypeRef.isCustomTypeReference() && childReturnTypeRef.isCustomTypeReference()) {
        CustomType expectedReturnType = parentReturnTypeRef.asCustomTypeReferenceOrElseThrow().targetType();
        CustomType actualReturnType = childReturnTypeRef.asCustomTypeReferenceOrElseThrow().targetType();
        if (actualReturnType.hasParent(expectedReturnType)) {
          buildDownwardReturnStatement(sb, method, actualReturnType, expectedReturnType);
        } else {
          buildCastReturnStatement(sb, method);
        }
      } else {
        buildCastReturnStatement(sb, method);
      }
    }
  }

  private void buildDirectReturnStatement(StringBuilder sb, MethodStatement method) {
    sb.append("return ");
    sb.append("this.").append(childFieldName).append(".");
    sb.append(method.name());
    sb.append("(");
    sb.append(method.params().stream()
        .map(MethodParam::name)
        .collect(Collectors.joining(", ")));
    sb.append(");");
  }

  private void buildCastReturnStatement(StringBuilder sb, MethodStatement method) {
    sb.append("return ");
    sb.append("(");
    appendMethodReturnHandleType(sb, method);
    sb.append(") ");
    sb.append("this.").append(childFieldName).append(".");
    sb.append(method.name());
    sb.append("(");
    sb.append(method.params().stream()
        .map(MethodParam::name)
        .collect(Collectors.joining(", ")));
    sb.append(");");
  }

  private void buildDownwardReturnStatement(
      StringBuilder sb, MethodStatement method, CustomType actualReturnType, CustomType expectedReturnType
  ) {
    sb.append("var value = this.");
    sb.append(childFieldName);
    sb.append(".");
    sb.append(method.name());
    sb.append("();\n");
    sb.append("   if (ObjectFunctions.isMovableObjectHandle(value)) {\n");
    sb.append("      return new ");
    sb.append(context.addToImportAndGetSimpleName(NameConventionFunctions.getMovableDownwardObjectHandleTypename(
        actualReturnType, expectedReturnType))
    );
    sb.append("((");
    sb.append(context.addToImportAndGetSimpleName(NameConventionFunctions.getMovableObjectHandleTypename(
        actualReturnType.className()))
    );
    sb.append(") value);\n");
    sb.append("} else {\n");
    sb.append("  throw new RuntimeException(\"Not implemented\");\n");
    sb.append("}");
  }
}
