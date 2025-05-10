package tech.intellispaces.reflectionsframework.annotationprocessor.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.reflectionsframework.annotation.Channel;
import tech.intellispaces.reflectionsframework.annotation.Movable;
import tech.intellispaces.reflectionsframework.annotation.Unmovable;
import tech.intellispaces.reflectionsframework.naming.NameConventionFunctions;
import tech.intellispaces.reflectionsframework.object.reference.MovabilityTypes;
import tech.intellispaces.reflectionsframework.object.reference.ObjectReferenceForm;
import tech.intellispaces.reflectionsframework.object.reference.ObjectReferenceForms;
import tech.intellispaces.reflectionsframework.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.reflectionsframework.space.channel.ChannelFunctions;
import tech.intellispaces.reflectionsframework.space.domain.DomainFunctions;
import tech.intellispaces.reflectionsframework.traverse.TraverseType;
import tech.intellispaces.jstatements.common.LanguageFunctions;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.method.MethodParam;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.jstatements.reference.CustomTypeReference;
import tech.intellispaces.jstatements.reference.CustomTypeReferences;
import tech.intellispaces.jstatements.reference.NotPrimitiveReference;
import tech.intellispaces.jstatements.reference.TypeReference;
import tech.intellispaces.jstatements.reference.TypeReferenceFunctions;

abstract class ConversionObjectGenerator extends AbstractObjectGenerator {
  protected String domainClassSimpleName;
  protected String classTypeParams;
  protected String classTypeParamsBrief;
  protected String childObjectHandleType;
  protected String parentDomainClassSimpleName;
  protected String domainTypeParamsBrief;
  protected String domainTypeArguments;
  protected String primaryDomainSimpleName;
  protected final CustomTypeReference superDomainType;
  protected String childFieldName;
  protected String domainType;
  protected boolean isAlias;

  public ConversionObjectGenerator(CustomType customType, CustomTypeReference superDomainType) {
    super(customType);
    this.superDomainType = superDomainType;
  }

  protected void analyzeDomain() {
    domainClassSimpleName = addImportAndGetSimpleName(superDomainType.targetType().canonicalName());
    domainTypeParamsBrief = superDomainType.targetType().typeParametersBriefDeclaration();
    classTypeParams = ObjectReferenceFunctions.getObjectFormTypeParamDeclaration(
        sourceArtifact(), ObjectReferenceForms.ObjectHandle, MovabilityTypes.General, this::addImportAndGetSimpleName, false, true
    );
    classTypeParamsBrief = sourceArtifact().typeParametersBriefDeclaration();
    domainTypeArguments = superDomainType.typeArgumentsDeclaration(this::addImportAndGetSimpleName);
    childFieldName = StringFunctions.lowercaseFirstLetter(
        StringFunctions.removeTailOrElseThrow(sourceArtifact().simpleName(), "Domain"));
    if (LanguageFunctions.isKeyword(childFieldName)) {
      childFieldName = "$" + childFieldName;
    }
    parentDomainClassSimpleName = addImportAndGetSimpleName(superDomainType.targetType().canonicalName());
  }

  protected void analyzeObjectHandleMethods(ArtifactGeneratorContext context) {
    CustomType actualSuperDomainType = buildActualType(superDomainType.targetType(), context);
    CustomTypeReference actualSuperDomainTypeReference = CustomTypeReferences.get(
        actualSuperDomainType, superDomainType.typeArguments()
    );
    CustomType effectiveActualSuperDomainType = actualSuperDomainTypeReference.effectiveTargetType();
    analyzeObjectFormMethods(effectiveActualSuperDomainType, context);
  }

  @SuppressWarnings("unchecked,rawtypes")
  protected void analyzeAlias() {
    Optional<CustomTypeReference> mainEquivalentDomain = DomainFunctions.getAliasBaseDomain(sourceArtifact());
    isAlias = mainEquivalentDomain.isPresent();
    if (isAlias) {
      primaryDomainSimpleName = addImportAndGetSimpleName(mainEquivalentDomain.get().targetType().canonicalName());
      domainType = buildDomainType(mainEquivalentDomain.get().targetType(), mainEquivalentDomain.get().typeArguments());
    } else {
      domainType = buildDomainType(superDomainType.targetType(), (List) sourceArtifact().typeParameters());
    }
  }

  protected MethodStatement convertMethodBeforeGenerate(MethodStatement method) {
    Map<String, NotPrimitiveReference> typeMapping = superDomainType.typeArgumentMapping();
    return method.effective(typeMapping);
  }

  @Override
  protected Map<String, String> generateMethod(
      MethodStatement method, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    if (method.hasAnnotation(Channel.class)) {
      return generateNormalMethod(method, targetForm, methodOrdinal);
    } else {
      return generateCustomizerMethod(convertMethodBeforeGenerate(method));
    }
  }

  private Map<String, String> generateNormalMethod(
      MethodStatement method, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    appendMethodReturnTypeDeclaration(sb, method, targetForm);
    sb.append(" ");
    sb.append(getObjectFormMethodName(method, targetForm));
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

  @Override
  protected boolean includeMethodForm(
      MethodStatement method, ObjectReferenceForm targetForm
  ) {
    if (ObjectReferenceForms.Primitive.is(targetForm)) {
      Optional<MethodStatement> actualMethod = superDomainType.targetType().actualMethod(
          method.name(), method.parameterTypes()
      );
      if (actualMethod.isEmpty()) {
        return false;
      }
      if (actualMethod.get().returnType().orElseThrow().isNamedReference()) {
        return false;
      }
    }
    return super.includeMethodForm(method, targetForm);
  }

  private Map<String, String> generateCustomizerMethod(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, method);
    if (method.returnType().isPresent()) {
      appendMethodReturnType(sb, method);
    } else {
      sb.append("void");
    }
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParams(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    sb.append(" {\n");
    if (method.returnType().isPresent()) {
      sb.append("  return ");
    }
    sb.append("this.")
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

    MethodStatement actualParentMethod = superDomainType.asCustomTypeReferenceOrElseThrow().targetType().actualMethod(
        method.name(), method.parameterTypes()
    ).orElseThrow();
    TypeReference parentReturnTypeRef = actualParentMethod.returnType().orElseThrow();

    MethodStatement actualChildMethod = sourceArtifact().actualMethod(method.name(), method.parameterTypes())
        .orElseThrow();
    TypeReference childReturnTypeRef = actualChildMethod.returnType().orElseThrow();

    if (TypeReferenceFunctions.isEqualTypes(parentReturnTypeRef, childReturnTypeRef)) {
      if (isMovableMethod(method)) {
        buildMovableDirectReturnStatement(sb, method, targetForm);
      } else {
        buildDirectReturnStatement(sb, method, targetForm);
      }
    } else {
      if (parentReturnTypeRef.isCustomTypeReference() && childReturnTypeRef.isCustomTypeReference()) {
        CustomType expectedReturnType = parentReturnTypeRef.asCustomTypeReferenceOrElseThrow().targetType();
        CustomType actualReturnType = childReturnTypeRef.asCustomTypeReferenceOrElseThrow().targetType();
        if (
            !ObjectReferenceFunctions.isDefaultObjectHandleType(actualReturnType)
                && !ObjectReferenceFunctions.isDefaultObjectHandleType(expectedReturnType)
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
    sb.append(getObjectFormMethodName(method, targetForm));
    sb.append("(");
    sb.append(method.params().stream()
        .map(MethodParam::name)
        .collect(Collectors.joining(", ")));
    sb.append(");");
  }

  private void buildMovableDirectReturnStatement(StringBuilder sb, MethodStatement method, ObjectReferenceForm targetForm) {
    sb.append("this.").append(childFieldName).append(".");
    sb.append(getObjectFormMethodName(method, targetForm));
    sb.append("(");
    sb.append(method.params().stream()
        .map(MethodParam::name)
        .collect(Collectors.joining(", ")));
    sb.append(");\n");
    sb.append("  return this;");
  }

  private void buildCastReturnStatement(StringBuilder sb, MethodStatement method, ObjectReferenceForm targetForm) {
    sb.append("return ");
    sb.append("(");
    appendObjectFormMethodReturnType(sb, method);
    sb.append(") ");
    sb.append("this.").append(childFieldName).append(".");
    sb.append(getObjectFormMethodName(method, targetForm));
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
      sb.append(buildObjectFormDeclaration(domainReturnType, getForm(), getMovabilityType(), true));
    } else {
      if (isMovableMethodOrTarget(method)) {
        sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.ObjectHandle, MovabilityTypes.Movable, true));
      } else if (isUnmovable(method)) {
        sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.ObjectHandle, MovabilityTypes.Unmovable, true));
      } else {
        sb.append(buildObjectFormDeclaration(domainReturnType, ObjectReferenceForms.ObjectHandle, MovabilityTypes.General, true));
      }
    }
  }

  protected String getObjectHandleSimpleName() {
    final String canonicalName;
    String superDomainCanonicalName = superDomainType.targetType().canonicalName();
    if (MovabilityTypes.Unmovable.is(getMovabilityType())) {
      canonicalName = NameConventionFunctions.getUnmovableObjectHandleTypename(superDomainCanonicalName, false);
    } else if (MovabilityTypes.Movable.is(getMovabilityType())) {
      canonicalName = NameConventionFunctions.getMovableObjectHandleTypename(superDomainCanonicalName, false);
    } else {
      throw UnexpectedExceptions.withMessage("Could not define movable type of the object handle {0}",
          sourceArtifact().canonicalName());
    }
    return addImportAndGetSimpleName(canonicalName);
  }

  protected String getMovableObjectHandleSimpleName() {
    String superDomainCanonicalName = superDomainType.targetType().canonicalName();
    return addImportAndGetSimpleName(NameConventionFunctions.getMovableObjectHandleTypename(superDomainCanonicalName, false));
  }

  private boolean isMovableMethod(MethodStatement method) {
    return ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving);
  }

  private boolean isMovableMethodOrTarget(MethodStatement method) {
    return ChannelFunctions.getTraverseTypes(method).stream().anyMatch(TraverseType::isMoving)
        || method.hasAnnotation(Movable.class);
  }

  private boolean isUnmovable(MethodStatement method) {
    return method.hasAnnotation(Unmovable.class);
  }
}
