package intellispaces.jaquarius.annotation.processor.domain;

import intellispaces.common.action.runner.Runner;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.text.TextActions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.common.javastatement.reference.NotPrimitiveReference;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.common.javastatement.reference.TypeReferenceFunctions;
import intellispaces.jaquarius.annotation.Movable;
import intellispaces.jaquarius.annotation.Unmovable;
import intellispaces.jaquarius.common.NameConventionFunctions;
import intellispaces.jaquarius.guide.GuideForm;
import intellispaces.jaquarius.object.ObjectFunctions;
import intellispaces.jaquarius.object.ObjectHandleTypes;
import intellispaces.jaquarius.space.channel.ChannelFunctions;
import intellispaces.jaquarius.space.domain.DomainFunctions;
import intellispaces.jaquarius.traverse.TraverseType;

import java.util.Map;
import java.util.stream.Collectors;

abstract class AbstractConversionDomainObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  protected final CustomTypeReference parentDomainType;
  protected String childFieldName;

  public AbstractConversionDomainObjectHandleGenerator(
      CustomType initiatorType, CustomType customType, CustomTypeReference parentDomainType
  ) {
    super(initiatorType, customType);
    this.parentDomainType = parentDomainType;
  }

  protected MethodStatement convertMethodBeforeGenerate(MethodStatement method) {
    Map<String, NotPrimitiveReference> typeMapping = parentDomainType.typeArgumentMapping();
    return method.effective(typeMapping);
  }

  protected void buildReturnStatement(StringBuilder sb, MethodStatement method, GuideForm guideForm) {
    if (NameConventionFunctions.isConversionMethod(method)) {
      buildConversionChainReturnStatement(sb, method, guideForm);
      return;
    }

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

  private void buildConversionChainReturnStatement(StringBuilder sb, MethodStatement method, GuideForm guideForm) {
    CustomType targetDomain = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();

    sb.append("return ");
    sb.append("this.").append(childFieldName);
    sb.append(DomainFunctions.buildConversionMethodsChain(annotatedType, targetDomain));
    sb.append(";");
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
    sb.append("(");
    Runner commaAppender = TextActions.skippingFirstTimeCommaAppender(sb);
    for (MethodParam param : method.params()) {
      commaAppender.run();
      sb.append(param.name());
    }
    sb.append(");\n");
    sb.append("  return value");

    String conversionChain = DomainFunctions.buildConversionMethodsChain(actualReturnType, expectedReturnType);
    if (conversionChain == null) {
      throw UnexpectedViolationException.withMessage("Could not build conversion methods chain from {0} to {1}",
          actualReturnType.canonicalName(), expectedReturnType.canonicalName()
      );
    }
    sb.append(conversionChain);
    sb.append(";");
  }

  @Override
  protected void appendMethodReturnHandleType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (NameConventionFunctions.isConversionMethod(method)) {
      sb.append(getObjectHandleDeclaration(domainReturnType, getObjectHandleType()));
    } else {
      if (
          ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving)
              || method.hasAnnotation(Movable.class)
      ) {
        sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Movable));
      } else if (method.hasAnnotation(Unmovable.class)) {
        sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Unmovable));
      } else {
        sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Common));
      }
    }
  }
}
