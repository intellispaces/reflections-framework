package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.general.text.StringFunctions;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.annotationprocessor.AbstractObjectHandleGenerator;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.handle.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.handle.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.CustomTypeReferences;
import tech.intellispaces.java.reflection.reference.NotPrimitiveReference;
import tech.intellispaces.java.reflection.reference.TypeReference;
import tech.intellispaces.java.reflection.reference.TypeReferenceFunctions;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

abstract class ConversionObjectHandleGenerator extends AbstractObjectHandleGenerator {
  protected String domainClassSimpleName;
  protected String classTypeParams;
  protected String classTypeParamsBrief;
  protected String childObjectHandleType;
  protected String parentDomainClassSimpleName;
  protected String domainTypeParamsBrief;
  protected String domainTypeArguments;
  protected String primaryDomainSimpleName;
  protected final CustomTypeReference parentDomainType;
  protected String childFieldName;
  protected String domainType;
  protected boolean isAlias;

  public ConversionObjectHandleGenerator(
      CustomType customType, CustomTypeReference parentDomainType
  ) {
    super(customType);
    this.parentDomainType = parentDomainType;
  }

  protected void analyzeDomain() {
    domainClassSimpleName = addImportAndGetSimpleName(parentDomainType.targetType().canonicalName());
    domainTypeParamsBrief = parentDomainType.targetType().typeParametersBriefDeclaration();
    classTypeParams = sourceArtifact().typeParametersFullDeclaration();
    classTypeParamsBrief = sourceArtifact().typeParametersBriefDeclaration();
    domainTypeArguments = parentDomainType.typeArgumentsDeclaration(this::addImportAndGetSimpleName);
    childFieldName = StringFunctions.lowercaseFirstLetter(
        StringFunctions.removeTailOrElseThrow(sourceArtifact().simpleName(), "Domain"));
    parentDomainClassSimpleName = addImportAndGetSimpleName(parentDomainType.targetType().canonicalName());
  }

  protected void analyzeObjectHandleMethods(ArtifactGeneratorContext context) {
    CustomType actualParentDomainType = buildActualType(parentDomainType.targetType(), context);
    CustomTypeReference actualParentDomainTypeReference = CustomTypeReferences.get(
        actualParentDomainType, parentDomainType.typeArguments()
    );
    CustomType effectiveActualParentDomainType = actualParentDomainTypeReference.effectiveTargetType();

    analyzeObjectHandleMethods(effectiveActualParentDomainType, context);
  }

  @SuppressWarnings("unchecked,rawtypes")
  protected void analyzeAlias() {
    Optional<CustomTypeReference> mainEquivalentDomain = DomainFunctions.getAliasBaseDomain(sourceArtifact());
    isAlias = mainEquivalentDomain.isPresent();
    if (isAlias) {
      primaryDomainSimpleName = addImportAndGetSimpleName(mainEquivalentDomain.get().targetType().canonicalName());
      domainType = buildDomainType(mainEquivalentDomain.get().targetType(), mainEquivalentDomain.get().typeArguments());
    } else {
      domainType = buildDomainType(parentDomainType.targetType(), (List) sourceArtifact().typeParameters());
    }
  }

  protected MethodStatement convertMethodBeforeGenerate(MethodStatement method) {
    Map<String, NotPrimitiveReference> typeMapping = parentDomainType.typeArgumentMapping();
    return method.effective(typeMapping);
  }

  @Override
  protected Map<String, String> generateMethod(
      MethodStatement method, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    if (method.hasAnnotation(Channel.class)) {
      return generateNormalMethod(method, targetForm, methodOrdinal);
    } else {
      return generatePrototypeMethod(convertMethodBeforeGenerate(method));
    }
  }

  private Map<String, String> generateNormalMethod(
      MethodStatement method, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    appendMethodReturnHandleType(sb, method, targetForm);
    sb.append(" ");
    sb.append(getObjectHandleMethodName(method, targetForm));
    sb.append("(");
    appendMethodParams(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    sb.append(" {\n");
    sb.append("  ");
    buildReturnStatement(sb, method, targetForm);
    sb.append("\n}\n");
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", sb.toString()
    );
  }

  private Map<String, String> generatePrototypeMethod(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    appendMethodReturnType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParams(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    sb.append(" {\n");
    sb.append("  return this.")
        .append(childFieldName)
        .append(".")
        .append(method.name())
        .append("();\n");
    sb.append("}");
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", sb.toString()
    );
  }

  private void buildReturnStatement(StringBuilder sb, MethodStatement method, ObjectReferenceForm targetForm) {
    if (NameConventionFunctions.isConversionMethod(method)) {
      buildConversionChainReturnStatement(sb, method, targetForm);
      return;
    }

    MethodStatement actualParentMethod = parentDomainType.asCustomTypeReferenceOrElseThrow().targetType().actualMethod(
        method.name(), method.parameterTypes()
    ).orElseThrow();
    TypeReference parentReturnTypeRef = actualParentMethod.returnType().orElseThrow();

    MethodStatement actualChildMethod = sourceArtifact().actualMethod(method.name(), method.parameterTypes())
        .orElseThrow();
    TypeReference childReturnTypeRef = actualChildMethod.returnType().orElseThrow();

    if (TypeReferenceFunctions.isEqualTypes(parentReturnTypeRef, childReturnTypeRef)) {
      buildDirectReturnStatement(sb, method, targetForm);
    } else {
      if (parentReturnTypeRef.isCustomTypeReference() && childReturnTypeRef.isCustomTypeReference()) {
        CustomType expectedReturnType = parentReturnTypeRef.asCustomTypeReferenceOrElseThrow().targetType();
        CustomType actualReturnType = childReturnTypeRef.asCustomTypeReferenceOrElseThrow().targetType();
        if (
            !ObjectHandleFunctions.isDefaultObjectHandleType(actualReturnType)
                && !ObjectHandleFunctions.isDefaultObjectHandleType(expectedReturnType)
                && actualReturnType.hasParent(expectedReturnType)
        ) {
          buildDownwardReturnStatement(sb, method, actualReturnType, expectedReturnType);
        } else {
          buildCastReturnStatement(sb, method, targetForm);
        }
      } else {
        buildCastReturnStatement(sb, method, targetForm);
      }
    }
  }

  private void buildConversionChainReturnStatement(
      StringBuilder sb, MethodStatement method, ObjectReferenceForm targetForm
  ) {
    CustomType targetDomain = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();

    sb.append("return ");
    sb.append("this.").append(childFieldName);
    sb.append(DomainFunctions.buildConversionMethodsChain(sourceArtifact(), targetDomain));
    sb.append(";");
  }

  private void buildDirectReturnStatement(StringBuilder sb, MethodStatement method, ObjectReferenceForm targetForm) {
    sb.append("return ");
    sb.append("this.").append(childFieldName).append(".");
    sb.append(getObjectHandleMethodName(method, targetForm));
    sb.append("(");
    sb.append(method.params().stream()
        .map(MethodParam::name)
        .collect(Collectors.joining(", ")));
    sb.append(");");
  }

  private void buildCastReturnStatement(StringBuilder sb, MethodStatement method, ObjectReferenceForm targetForm) {
    sb.append("return ");
    sb.append("(");
    appendObjectFormMethodReturnType(sb, method);
    sb.append(") ");
    sb.append("this.").append(childFieldName).append(".");
    sb.append(getObjectHandleMethodName(method, targetForm));
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
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : method.params()) {
      commaAppender.run();
      sb.append(param.name());
    }
    sb.append(");\n");
    sb.append("  return value");

    String conversionChain = DomainFunctions.buildConversionMethodsChain(actualReturnType, expectedReturnType);
    if (conversionChain == null) {
      throw UnexpectedExceptions.withMessage("Could not build conversion methods chain from {0} to {1}",
          actualReturnType.canonicalName(), expectedReturnType.canonicalName()
      );
    }
    sb.append(conversionChain);
    sb.append(";");
  }

  @Override
  protected void appendObjectFormMethodReturnType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (NameConventionFunctions.isConversionMethod(method)) {
      sb.append(buildObjectHandleDeclaration(domainReturnType, getObjectHandleType()));
    } else {
      if (
          ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving)
              || method.hasAnnotation(Movable.class)
      ) {
        sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Movable));
      } else if (method.hasAnnotation(Unmovable.class)) {
        sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Unmovable));
      } else {
        sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.General));
      }
    }
  }
}
