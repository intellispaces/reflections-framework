package intellispaces.framework.core.annotation.processor.domain;

import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.common.javastatement.reference.NotPrimitiveReference;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.common.javastatement.reference.TypeReferenceFunctions;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.object.ObjectFunctions;

import javax.annotation.processing.RoundEnvironment;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class AbstractConversionDomainObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  protected final CustomTypeReference parentDomainType;
  protected String childFieldName;

  public AbstractConversionDomainObjectHandleGenerator(
      CustomType initiatorType, CustomType customType, CustomTypeReference parentDomainType
  ) {
    super(initiatorType, customType);
    this.parentDomainType = parentDomainType;
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, RoundEnvironment roundEnv
  ) {
    return buildActualType(parentDomainType.targetType(), roundEnv).actualMethods().stream()
        .filter(this::isNotDomainClassGetter);
  }

  protected MethodStatement convertMethodBeforeGenerate(MethodStatement method) {
    Map<String, NotPrimitiveReference> typeMapping = parentDomainType.typeArgumentMapping();
    return method.effective(typeMapping);
  }

  protected void buildReturnStatement(StringBuilder sb, MethodStatement method, GuideForm guideForm) {
    MethodStatement actualParentMethod = parentDomainType.asCustomTypeReferenceOrElseThrow().targetType().actualMethod(
        method.name(), method.parameterTypes()
    ).orElseThrow();
    TypeReference parentReturnTypeRef = actualParentMethod.returnType().orElseThrow();

    MethodStatement actualChildMethod = annotatedType.actualMethod(method.name(), method.parameterTypes())
        .orElseThrow();
    TypeReference childReturnTypeRef = actualChildMethod.returnType().orElseThrow();

    if (TypeReferenceFunctions.isEqualTypes(parentReturnTypeRef, childReturnTypeRef)) {
      buildDirectReturnStatement(sb, method, guideForm);
    } else {
      if (parentReturnTypeRef.isCustomTypeReference() && childReturnTypeRef.isCustomTypeReference()) {
        CustomType expectedReturnType = parentReturnTypeRef.asCustomTypeReferenceOrElseThrow().targetType();
        CustomType actualReturnType = childReturnTypeRef.asCustomTypeReferenceOrElseThrow().targetType();
        if (
            !ObjectFunctions.isDefaultObjectHandleType(actualReturnType)
                && !ObjectFunctions.isDefaultObjectHandleType(expectedReturnType)
                && actualReturnType.hasParent(expectedReturnType)
        ) {
          buildDownwardReturnStatement(sb, method, actualReturnType, expectedReturnType);
        } else {
          buildCastReturnStatement(sb, method, guideForm);
        }
      } else {
        buildCastReturnStatement(sb, method, guideForm);
      }
    }
  }

  private void buildDirectReturnStatement(StringBuilder sb, MethodStatement method, GuideForm guideForm) {
    sb.append("return ");
    sb.append("this.").append(childFieldName).append(".");
    sb.append(getMethodName(method, guideForm));
    sb.append("(");
    sb.append(method.params().stream()
        .map(MethodParam::name)
        .collect(Collectors.joining(", ")));
    sb.append(");");
  }

  private void buildCastReturnStatement(StringBuilder sb, MethodStatement method, GuideForm guideForm) {
    sb.append("return ");
    sb.append("(");
    appendMethodReturnHandleType(sb, method);
    sb.append(") ");
    sb.append("this.").append(childFieldName).append(".");
    sb.append(getMethodName(method, guideForm));
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
