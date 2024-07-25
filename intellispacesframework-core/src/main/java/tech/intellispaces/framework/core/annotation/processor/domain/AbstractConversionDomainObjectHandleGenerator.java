package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.core.common.NameConventionFunctions;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.CustomTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReferenceFunctions;

import java.util.stream.Collectors;

abstract class AbstractConversionDomainObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  protected final CustomTypeReference parentDomainType;
  protected String childFieldName;

  public AbstractConversionDomainObjectHandleGenerator(CustomType customType, CustomTypeReference parentDomainType) {
    super(customType);
    this.parentDomainType = parentDomainType;
  }

  protected void buildReturnStatement(StringBuilder sb, MethodStatement method) {
    MethodStatement actualParentMethod = parentDomainType.asCustomTypeReferenceSurely().targetType().actualMethod(
        method.name(), method.parameterTypes()
    ).orElseThrow();
    TypeReference parentReturnTypeRef = actualParentMethod.returnType().orElseThrow();

    MethodStatement actualChildMethod = annotatedType.actualMethod(method.name(), method.parameterTypes())
        .orElseThrow();
    TypeReference childReturnTypeRef = actualChildMethod.returnType().orElseThrow();

    sb.append("return ");
    if (TypeReferenceFunctions.isEqualTypes(parentReturnTypeRef, childReturnTypeRef)) {
      buildDirectReturnStatement(sb, method);
    } else {
      if (parentReturnTypeRef.isCustomTypeReference() && childReturnTypeRef.isCustomTypeReference()) {
        CustomType expectedReturnType = parentReturnTypeRef.asCustomTypeReferenceSurely().targetType();
        CustomType actualReturnType = childReturnTypeRef.asCustomTypeReferenceSurely().targetType();
        if (actualReturnType.hasParent(expectedReturnType)) {
          buildDowngradeReturnStatement(sb, method, actualReturnType, expectedReturnType);
        } else {
          buildCastReturnStatement(sb, method);
        }
      } else {
        buildCastReturnStatement(sb, method);
      }
    }
  }

  private void buildDirectReturnStatement(StringBuilder sb, MethodStatement method) {
    sb.append("this.").append(childFieldName).append(".");
    sb.append(method.name());
    sb.append("(");
    sb.append(method.params().stream()
        .map(MethodParam::name)
        .collect(Collectors.joining(", ")));
    sb.append(");");
  }

  private void buildCastReturnStatement(StringBuilder sb, MethodStatement method) {
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

  private void buildDowngradeReturnStatement(
      StringBuilder sb, MethodStatement method, CustomType actualReturnType, CustomType expectedReturnType
  ) {
    sb.append("new ");
    sb.append(context.addToImportAndGetSimpleName(NameConventionFunctions.getBaseDowngradeObjectHandleTypename(
        actualReturnType, expectedReturnType))
    );
    sb.append("(this.");
    sb.append(childFieldName);
    sb.append(".");
    sb.append(method.name());
    sb.append("());");
  }
}
