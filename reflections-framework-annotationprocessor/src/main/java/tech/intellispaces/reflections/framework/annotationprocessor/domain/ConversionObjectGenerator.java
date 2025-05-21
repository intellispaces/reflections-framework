package tech.intellispaces.reflections.framework.annotationprocessor.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.javareflection.common.LanguageFunctions;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.CustomTypeReference;
import tech.intellispaces.javareflection.reference.CustomTypeReferences;
import tech.intellispaces.javareflection.reference.NotPrimitiveReference;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.javareflection.reference.TypeReferenceFunctions;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotation.Movable;
import tech.intellispaces.reflections.framework.annotation.Unmovable;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.MovabilityTypes;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;
import tech.intellispaces.reflections.framework.traverse.TraverseType;

abstract class ConversionObjectGenerator extends AbstractReflectionFormGenerator {
  protected String domainClassSimpleName;
  protected String classTypeParams;
  protected String classTypeParamsBrief;
  protected String childReflectionType;
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
    classTypeParams = ReflectionFunctions.getObjectFormTypeParamDeclaration(
        sourceArtifact(), ReflectionForms.Reflection, MovabilityTypes.General, this::addImportAndGetSimpleName, false, true
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

  protected void analyzeReflectionMethods(ArtifactGeneratorContext context) {
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
          MethodStatement method, ReflectionForm targetForm, int methodOrdinal
  ) {
    if (method.hasAnnotation(Channel.class)) {
      return generateNormalMethod(method, targetForm, methodOrdinal);
    } else {
      return generateCustomizerMethod(convertMethodBeforeGenerate(method));
    }
  }

  private Map<String, String> generateNormalMethod(
          MethodStatement method, ReflectionForm targetForm, int methodOrdinal
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
      MethodStatement method, ReflectionForm targetForm
  ) {
    if (ReflectionForms.Primitive.is(targetForm)) {
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

  private void buildReturnStatement(StringBuilder sb, MethodStatement method, ReflectionForm targetForm) {
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
            !ReflectionFunctions.isDefaultReflectionType(actualReturnType)
                && !ReflectionFunctions.isDefaultReflectionType(expectedReturnType)
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
      StringBuilder sb, MethodStatement method, ReflectionForm targetForm
  ) {
    CustomType targetDomain = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();

    sb.append("return ");
    sb.append("this.").append(childFieldName);
    sb.append(DomainFunctions.buildConversionMethodsChain(sourceArtifact(), targetDomain));
    sb.append(";");
  }

  private void buildDirectReturnStatement(StringBuilder sb, MethodStatement method, ReflectionForm targetForm) {
    sb.append("return ");
    sb.append("this.").append(childFieldName).append(".");
    sb.append(getObjectFormMethodName(method, targetForm));
    sb.append("(");
    sb.append(method.params().stream()
        .map(MethodParam::name)
        .collect(Collectors.joining(", ")));
    sb.append(");");
  }

  private void buildMovableDirectReturnStatement(StringBuilder sb, MethodStatement method, ReflectionForm targetForm) {
    sb.append("this.").append(childFieldName).append(".");
    sb.append(getObjectFormMethodName(method, targetForm));
    sb.append("(");
    sb.append(method.params().stream()
        .map(MethodParam::name)
        .collect(Collectors.joining(", ")));
    sb.append(");\n");
    sb.append("  return this;");
  }

  private void buildCastReturnStatement(StringBuilder sb, MethodStatement method, ReflectionForm targetForm) {
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
        sb.append(buildObjectFormDeclaration(domainReturnType, ReflectionForms.Reflection, MovabilityTypes.Movable, true));
      } else if (isUnmovable(method)) {
        sb.append(buildObjectFormDeclaration(domainReturnType, ReflectionForms.Reflection, MovabilityTypes.Unmovable, true));
      } else {
        sb.append(buildObjectFormDeclaration(domainReturnType, ReflectionForms.Reflection, MovabilityTypes.General, true));
      }
    }
  }

  protected String getReflectionSimpleName() {
    final String canonicalName;
    String superDomainCanonicalName = superDomainType.targetType().canonicalName();
    if (MovabilityTypes.Unmovable.is(getMovabilityType())) {
      canonicalName = NameConventionFunctions.getUnmovableReflectionTypeName(superDomainCanonicalName, false);
    } else if (MovabilityTypes.Movable.is(getMovabilityType())) {
      canonicalName = NameConventionFunctions.getMovableReflectionTypeName(superDomainCanonicalName, false);
    } else {
      throw UnexpectedExceptions.withMessage("Could not define movable type of the reflection {0}",
          sourceArtifact().canonicalName());
    }
    return addImportAndGetSimpleName(canonicalName);
  }

  protected String getMovableReflectionSimpleName() {
    String superDomainCanonicalName = superDomainType.targetType().canonicalName();
    return addImportAndGetSimpleName(NameConventionFunctions.getMovableReflectionTypeName(superDomainCanonicalName, false));
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
