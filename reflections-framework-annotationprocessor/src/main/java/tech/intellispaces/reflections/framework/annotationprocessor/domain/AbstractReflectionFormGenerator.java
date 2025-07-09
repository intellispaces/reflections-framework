package tech.intellispaces.reflections.framework.annotationprocessor.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.RoundEnvironment;

import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.core.Rid;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.customtype.Interfaces;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodSignatureDeclarations;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.CustomTypeReference;
import tech.intellispaces.javareflection.reference.CustomTypeReferences;
import tech.intellispaces.javareflection.reference.NamedReference;
import tech.intellispaces.javareflection.reference.NotPrimitiveReference;
import tech.intellispaces.javareflection.reference.ReferenceBound;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.javareflection.reference.TypeReferenceFunctions;
import tech.intellispaces.reflections.framework.ArtifactType;
import tech.intellispaces.reflections.framework.annotation.Movable;
import tech.intellispaces.reflections.framework.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.framework.annotationprocessor.AnnotationGeneratorFunctions;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactGenerator;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.node.ReflectionsNodeFunctions;
import tech.intellispaces.reflections.framework.reflection.MovabilityType;
import tech.intellispaces.reflections.framework.reflection.MovabilityTypes;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.settings.DomainAssignments;
import tech.intellispaces.reflections.framework.settings.DomainReference;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;

public abstract class AbstractReflectionFormGenerator extends ReflectionsArtifactGenerator {
  protected boolean isAlias;
  protected Rid domainRid;
  protected CustomType domainType;
  protected String domainTypename;
  protected String generalReflection;
  protected String baseReflection;
  protected String primaryReflection;
  protected String typeParamsFull;
  protected String typeParamsBrief;
  protected String domainTypeParamsFull;
  protected String domainTypeParamsBrief;
  protected String primaryDomainSimpleName;
  protected String primaryDomainTypeArguments;
  protected final List<Map<String, String>> methods = new ArrayList<>();
  protected final List<Map<String, String>> rawDomainMethods = new ArrayList<>();
  protected final List<String> underlyingTypes = new ArrayList<>();

  public AbstractReflectionFormGenerator(CustomType domainTypename) {
    super(domainTypename);
  }

  abstract protected ReflectionForm getForm();

  abstract protected MovabilityType getMovabilityType();

  abstract protected List<ArtifactType> relatedArtifactTypes();

  protected String movableClassSimpleName() {
    return addImportAndGetSimpleName(
        NameConventionFunctions.getMovableReflectionTypeName(sourceArtifact().className(), false)
    );
  }

  protected void analyzeDomain() {
    typeParamsFull = ReflectionFunctions.getObjectFormTypeParamDeclaration(
        sourceArtifact(), ReflectionForms.Reflection, MovabilityTypes.General, this::addImportAndGetSimpleName, false, true
    );
    typeParamsBrief = ReflectionFunctions.getObjectFormTypeParamDeclaration(
        sourceArtifact(), ReflectionForms.Reflection, MovabilityTypes.General, this::addImportAndGetSimpleName, false, false
    );
    domainTypeParamsFull = sourceArtifact().typeParametersFullDeclaration();
    domainTypeParamsBrief = sourceArtifact().typeParametersBriefDeclaration();
    generalReflection = addImportAndGetSimpleName(
        NameConventionFunctions.getGeneralReflectionTypeName(sourceArtifact().className(), false)
    );
  }

  protected String getDomainTypeParamsBrief(CustomTypeReference domainType) {
    if (domainType.typeArguments().isEmpty()) {
      return "";
    }

    var sb = new StringBuilder();
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    sb.append("<");
    for (NotPrimitiveReference typeArgument : domainType.typeArguments()) {
      commaAppender.run();
      if (typeArgument.isCustomTypeReference()) {
        CustomTypeReference customTypeReference = typeArgument.asCustomTypeReferenceOrElseThrow();
        sb.append(addImportAndGetSimpleName(customTypeReference.targetType().canonicalName()));
      } else if (typeArgument.isNamedReference()) {
        NamedReference namedReference = typeArgument.asNamedReferenceOrElseThrow();
        sb.append(namedReference.name());
      } else {
        throw NotImplementedExceptions.withCode("xG3KKaWv");
      }
    }
    sb.append(">");
    return sb.toString();
  }

  private Map<String, String> buildConversionMethod(CustomTypeReference parent) {
    var sb = new StringBuilder();
    String targetType = buildObjectFormDeclaration(parent, getForm(), getMovabilityType(), true);

    DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByType(DomainAssignments.Number);
    if (parent.targetType().canonicalName().equals(domain.classCanonicalName())) {
      underlyingTypes.add(domain.domainName());
    } else {
      underlyingTypes.add(targetType);
    }
    sb.append(targetType);
    sb.append(" ");
    sb.append(NameConventionFunctions.getConversionMethodName(parent));
    sb.append("()");
    return Map.of(
        "declaration", sb.toString()
    );
  }

  protected void analyzeObjectFormMethods(CustomType type, ArtifactGeneratorContext context) {
    List<MethodStatement> methods = getObjectFormMethods(type, context).toList();
    int methodOrdinal = 0;
    for (MethodStatement method : methods) {
      MethodStatement effectiveMethod = convertMethodBeforeGenerate(method);
      analyzeRawDomainMethod(effectiveMethod);
      if (includeMethodForm(effectiveMethod, ReflectionForms.Reflection)) {
        analyzeMethod(effectiveMethod, ReflectionForms.Reflection, methodOrdinal++);
      }
      if (includeMethodForm(effectiveMethod,  ReflectionForms.Primitive)) {
        analyzeMethod(effectiveMethod, ReflectionForms.Primitive, methodOrdinal++);
      }
      if (includeMethodForm(effectiveMethod,  ReflectionForms.PrimitiveWrapper)) {
        analyzeMethod(effectiveMethod, ReflectionForms.PrimitiveWrapper, methodOrdinal++);
      }
    }
  }

  protected boolean includeMethodForm(MethodStatement method, ReflectionForm targetForm) {
    if (ReflectionForms.Reflection.is(targetForm)) {
      return ReflectionForms.Reflection.is(getForm());
    } else if (ReflectionForms.Primitive.is(targetForm)) {
      return isPrimitiveWrapper(method.returnType().orElseThrow());
    } else if (ReflectionForms.PrimitiveWrapper.is(targetForm)) {
      return false;
    }
    return true;
  }

  protected boolean hasMethodNormalForm(MethodStatement method) {
    for (MethodParam param : method.params()) {
      if (isPrimitiveWrapper(param.type())) {
        return true;
      }
    }
    return true;
  }

  protected boolean isPrimitiveWrapper(TypeReference typeReference) {
    if (typeReference.isCustomTypeReference()) {
      CustomType returnType = typeReference.asCustomTypeReferenceOrElseThrow().targetType();
      return ClassFunctions.isPrimitiveWrapperClass(returnType.canonicalName());
    }
    return false;
  }

  private void analyzeMethod(
          MethodStatement method, ReflectionForm targetForm, int methodOrdinal
  ) {
    methods.add(generateMethod(method, targetForm,  methodOrdinal));
  }

  protected MethodStatement convertMethodBeforeGenerate(MethodStatement method) {
    return method;
  }

  protected void analyzeRawDomainMethod(MethodStatement method) {
    boolean isRawDomainMethod = false;
    for (TypeReference paramType : method.parameterTypes()) {
      if (DomainFunctions.isDomainType(paramType)) {
        isRawDomainMethod = true;
        break;
      }
    }
    if (isRawDomainMethod) {
      rawDomainMethods.add(generateRawDomainMethod(method));
    }
  }

  protected Map<String, String> generateMethod(
          MethodStatement method, ReflectionForm targetForm, int methodOrdinal
  ) {
    var sb = new StringBuilder();
    appendMethodTypeParameters(sb, method);
    boolean disableMoving = isDisableMoving(method);
    if (disableMoving) {
      sb.append("default ");
    }
    appendMethodReturnTypeDeclaration(sb, method, targetForm);
    sb.append(" ");
    sb.append(getObjectFormMethodName(method, targetForm));
    sb.append("(");
    appendMethodParams(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);

    if (disableMoving) {
      addImport(TraverseException.class);
      String exceptionSimpleName = simpleNameOf(TraverseException.class);
      sb.append(" {\n");
      sb.append("    throw ").append(exceptionSimpleName).append(
          ".withMessage(\"Unmovable object cannot be moved\");\n");
      sb.append("  }");
    }
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", sb.toString()
    );
  }

  protected Map<String, String> generateRawDomainMethod(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("public ");
    sb.append(MethodSignatureDeclarations.build(method).get(this::addImport, this::simpleNameOf));
    sb.append(" {\n");
    sb.append("  return ");
    sb.append(method.name());
    sb.append("(");
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : method.params()) {
      commaAppender.run();
      if (DomainFunctions.isDomainType(param.type())) {
        sb.append("(");
        sb.append(ReflectionFunctions.getObjectFormDeclaration(param.type(), ReflectionForms.Reflection, MovabilityTypes.General, true, this::addImportAndGetSimpleName));
        sb.append(") ");
      }
      sb.append(param.name());
    }
    sb.append(");\n");
    sb.append("}\n");
    return Map.of("declaration", sb.toString());
  }

  protected boolean isDisableMoving(MethodStatement method) {
    return ChannelFunctions.isMovingBasedChannel(method)
        && ReflectionForms.Reflection.is(getForm())
        && !MovabilityTypes.Movable.is(getMovabilityType());
  }

  protected void appendMethodReturnType(StringBuilder sb, MethodStatement method) {
    if (method.returnType().isEmpty()) {
      sb.append("void");
    }
    TypeReference returnType = method.returnType().orElseThrow();
    sb.append(returnType.actualDeclaration(this::convertName));
  }

  private String convertName(String domainClassName) {
    DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByClassName(domainClassName);
    if (domain != null && domain.delegateClassName() != null) {
      return addImportAndGetSimpleName(domain.delegateClassName());
    }
    return addImportAndGetSimpleName(domainClassName);
  }

  protected Stream<MethodStatement> getObjectFormMethods(CustomType customType, ArtifactGeneratorContext context) {
    return customType.actualMethods().stream()
        .filter(m -> DomainFunctions.isDomainType(m.owner()));
  }

  protected CustomType buildActualType(CustomType domainType, ArtifactGeneratorContext context) {
    return buildActualType(domainType, context, false);
  }

  protected List<CustomType> findCustomizers(CustomType domainType, RoundEnvironment roundEnv) {
    var customizers = new ArrayList<CustomType>();
    for (ArtifactType artifactType : relatedArtifactTypes()) {
      customizers.addAll(AnnotationFunctions.findCustomizer(domainType, artifactType, roundEnv));
    }
    return customizers;
  }

  protected CustomType buildActualType(
      CustomType domainType, ArtifactGeneratorContext context, boolean includeInheritedMethods
  ) {
    var builder = Interfaces.build(domainType.asInterfaceOrElseThrow());
    findCustomizerMethods(domainType, context.initialRoundEnvironment(), includeInheritedMethods)
        .forEach(builder::addDeclaredMethod);

    var extendedInterfaces = new ArrayList<CustomTypeReference>();
    for (CustomTypeReference superDomain : domainType.parentTypes()) {
      extendedInterfaces.add(
          CustomTypeReferences.get(buildActualType(superDomain.targetType(), context, false), superDomain.typeArguments())
      );
    }
    builder.extendedInterfaces(extendedInterfaces);
    return builder.get();
  }

  private List<MethodStatement> findCustomizerMethods(
      CustomType domainType, RoundEnvironment roundEnv, boolean includeInheritedMethods
  ) {
    var methods = new ArrayList<MethodStatement>();
    addCustomizerMethods(methods, domainType, roundEnv, Map.of(), includeInheritedMethods);
    return methods;
  }

  private void addCustomizerMethods(
      List<MethodStatement> methods,
      CustomType domainType,
      RoundEnvironment roundEnv,
      Map<String, NotPrimitiveReference> initialTypeMapping,
      boolean includeInheritedMethods
  ) {
    for (ArtifactType artifactType : relatedArtifactTypes()) {
      List<CustomType> customizers = AnnotationFunctions.findCustomizer(
          domainType, artifactType, roundEnv
      );
      for (CustomType customizer : customizers) {
        methods.addAll(customizer.declaredMethods());
        if (includeInheritedMethods) {
          addInheritedMethods(customizer, methods, initialTypeMapping);
        }
      }
    }

    if (includeInheritedMethods) {
      for (CustomTypeReference parent : domainType.parentTypes()) {
        Map<String, NotPrimitiveReference> typeMapping = TypeReferenceFunctions.mergeTypeArgumentMapping(
            parent, initialTypeMapping
        );
        addCustomizerMethods(methods, parent.targetType(), roundEnv, typeMapping, true);
      }
    }
  }

  private void addInheritedMethods(
      CustomType type, List<MethodStatement> methods, Map<String, NotPrimitiveReference> initialTypeMapping
  ) {
    for (CustomTypeReference parent : type.parentTypes()) {
      Map<String, NotPrimitiveReference> typeMapping = TypeReferenceFunctions.mergeTypeArgumentMapping(parent, initialTypeMapping);
      for (MethodStatement method : parent.targetType().declaredMethods()) {
        methods.add(method.effective(typeMapping));
      }
      addInheritedMethods(parent.targetType(), methods, initialTypeMapping);
    }
  }

  protected String buildDomainType(CustomType domainType, List<NotPrimitiveReference> typeQualifiers) {
    StringBuilder sb = new StringBuilder();
    sb.append("Types.get(");
    sb.append(addImportAndGetSimpleName(domainType.canonicalName())).append(".class");
    for (NotPrimitiveReference typeQualifier : typeQualifiers) {
      sb.append(", ");
      analyzeDomainType(typeQualifier, sb);
    }
    sb.append(")");
    return sb.toString();
  }

  private void analyzeDomainType(NotPrimitiveReference typeReference, StringBuilder sb) {
    if (typeReference.isCustomTypeReference()) {
      CustomTypeReference customTypeReference = typeReference.asCustomTypeReferenceOrElseThrow();
      sb.append("Types.get(");
      sb.append(addImportAndGetSimpleName(customTypeReference.targetType().canonicalName())).append(".class");
      for (NotPrimitiveReference typeArg : customTypeReference.typeArguments()) {
        sb.append(", ");
        analyzeDomainType(typeArg, sb);
      }
      sb.append(")");
    } else if (typeReference.isNamedReference()) {
      NamedReference namedReference = typeReference.asNamedReferenceOrElseThrow();
      if (namedReference.extendedBounds().isEmpty()) {
        sb.append("Types.get(");
        sb.append(addImportAndGetSimpleName(Object.class)).append(".class");
        sb.append(")");
      } else {
        ReferenceBound extendedBound = namedReference.extendedBounds().get(0);
        analyzeDomainType(extendedBound, sb);
      }
    }
  }

  protected String getObjectFormMethodName(MethodStatement domainMethod, ReflectionForm targetForm) {
    if (ReflectionForms.Reflection.is(targetForm)) {
      return domainMethod.name();
    } else if (ReflectionForms.Primitive.is(targetForm)) {
      return domainMethod.name() + "AsPrimitive";
    } else if (ReflectionForms.PrimitiveWrapper.is(targetForm)) {
      return domainMethod.name() + "AsObject";
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported object reference form - {0}", targetForm);
    }
  }

  protected void appendMethodReturnTypeDeclaration(
      StringBuilder sb, MethodStatement method, ReflectionForm targetForm
  ) {
    if (ReflectionForms.Reflection.is(targetForm)) {
      appendObjectFormMethodReturnType(sb, method);
    } else if (ReflectionForms.Primitive.is(targetForm)) {
      CustomType returnType = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
      sb.append(ClassFunctions.primitiveTypenameOfWrapper(returnType.canonicalName()));
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported guide form - {0}", targetForm);
    }
  }

  protected void appendObjectFormMethodReturnType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (NameConventionFunctions.isConversionMethod(method)) {
      sb.append(buildObjectFormDeclaration(domainReturnType, getForm(), getMovabilityType(), true));
    } else {
      if (method.hasAnnotation(Movable.class)) {
        sb.append(buildObjectFormDeclaration(domainReturnType, getForm(), MovabilityTypes.Movable, true));
      } else {
        sb.append(buildObjectFormDeclaration(domainReturnType, getForm(), MovabilityTypes.General, true));
      }
    }
  }

  protected void appendMethodTypeParameters(StringBuilder sb, MethodStatement method) {
    if (!method.typeParameters().isEmpty()) {
      sb.append("<");
      RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
      for (NamedReference namedTypeReference : method.typeParameters()) {
        commaAppender.run();
        sb.append(namedTypeReference.actualDeclaration());
      }
      sb.append("> ");
    }
  }

  protected void appendMethodParams(StringBuilder sb, MethodStatement method) {
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : AnnotationGeneratorFunctions.rearrangementParams(method.params())) {
      commaAppender.run();
      sb.append(buildObjectFormDeclaration(
          AnnotationGeneratorFunctions.normalizeType(param.type()),
          ReflectionForms.Reflection,
          MovabilityTypes.General,
          true
      ));
      sb.append(" ");
      sb.append(param.name());
    }
  }

  protected void appendMethodExceptions(StringBuilder sb, MethodStatement method) {
    String exceptions = method.exceptions().stream()
        .map(e -> e.asCustomTypeReference().orElseThrow().targetType())
        .peek(e -> addImport(e.canonicalName()))
        .map(e -> simpleNameOf(e.canonicalName()))
        .collect(Collectors.joining(", "));
    if (!exceptions.isEmpty()) {
      sb.append(" throws ").append(exceptions);
    }
  }

  protected boolean excludeDeepConversionMethods(MethodStatement domainMethod, CustomType customType) {
    if (!NameConventionFunctions.isConversionMethod(domainMethod)) {
      return true;
    }
    for (CustomTypeReference parent : customType.parentTypes()) {
      if (excludeDeepConversionMethods(domainMethod, parent.targetType())) {
        return true;
      }
      if (NameConventionFunctions.getConversionMethodName(parent).equals(domainMethod.name())) {
        return true;
      }
    }
    return false;
  }
}
