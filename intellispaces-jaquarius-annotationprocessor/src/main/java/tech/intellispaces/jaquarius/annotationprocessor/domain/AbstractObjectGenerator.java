package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.jaquarius.ArtifactType;
import tech.intellispaces.jaquarius.Jaquarius;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationGeneratorFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.MovabilityType;
import tech.intellispaces.jaquarius.object.reference.MovabilityTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.jaquarius.settings.KeyDomain;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.reflection.customtype.CustomType;
import tech.intellispaces.reflection.customtype.InterfaceType;
import tech.intellispaces.reflection.customtype.Interfaces;
import tech.intellispaces.reflection.method.MethodParam;
import tech.intellispaces.reflection.method.MethodSignatureDeclarations;
import tech.intellispaces.reflection.method.MethodStatement;
import tech.intellispaces.reflection.reference.CustomTypeReference;
import tech.intellispaces.reflection.reference.CustomTypeReferences;
import tech.intellispaces.reflection.reference.NamedReference;
import tech.intellispaces.reflection.reference.NotPrimitiveReference;
import tech.intellispaces.reflection.reference.ReferenceBound;
import tech.intellispaces.reflection.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractObjectGenerator extends JaquariusArtifactGenerator {
  protected boolean isAlias;
  protected String domainType;
  protected String generalObjectHandle;
  protected String baseObjectHandle;
  protected String primaryObjectHandle;
  protected String typeParamsFull;
  protected String typeParamsBrief;
  protected String domainTypeParamsFull;
  protected String domainTypeParamsBrief;
  protected String primaryDomainSimpleName;
  protected String primaryDomainTypeArguments;
  protected final List<Map<String, String>> methods = new ArrayList<>();
  protected final List<Map<String, String>> conversionMethods = new ArrayList<>();
  protected final List<Map<String, String>> rawDomainMethods = new ArrayList<>();

  public AbstractObjectGenerator(CustomType domainType) {
    super(domainType);
  }

  abstract protected ObjectReferenceForm getForm();

  abstract protected MovabilityType getMovabilityType();

  abstract protected List<ArtifactType> relatedArtifactTypes();

  protected String movableClassSimpleName() {
    return addImportAndGetSimpleName(
        NameConventionFunctions.getMovableObjectHandleTypename(sourceArtifact().className(), false)
    );
  }

  protected void analyzeDomain() {
    typeParamsFull = ObjectReferenceFunctions.getObjectFormTypeParamDeclaration(
        sourceArtifact(), ObjectReferenceForms.Plain, MovabilityTypes.Undefined, this::addImportAndGetSimpleName, false, true
    );
    typeParamsBrief = ObjectReferenceFunctions.getObjectFormTypeParamDeclaration(
        sourceArtifact(), ObjectReferenceForms.Plain, MovabilityTypes.Undefined, this::addImportAndGetSimpleName, false, false
    );
    domainTypeParamsFull = sourceArtifact().typeParametersFullDeclaration();
    domainTypeParamsBrief = sourceArtifact().typeParametersBriefDeclaration();
    generalObjectHandle = addImportAndGetSimpleName(
        NameConventionFunctions.getUndefinedObjectHandleTypename(sourceArtifact().className())
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

  protected void analyzeConversionMethods(CustomType domainType) {
    Collection<CustomTypeReference> parents = DomainFunctions.getEffectiveSuperDomains(domainType);
    parents.stream()
        .map(this::buildConversionMethod)
        .forEach(conversionMethods::add);
  }

  private Map<String, String> buildConversionMethod(CustomTypeReference parent) {
    var sb = new StringBuilder();
    sb.append(buildObjectFormDeclaration(parent, getForm(), getMovabilityType(), true));
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
      if (hasMethodNormalForm(effectiveMethod)) {
        if (includeMethodForm(effectiveMethod, ObjectReferenceForms.Plain)) {
          analyzeMethod(effectiveMethod, ObjectReferenceForms.Plain, methodOrdinal++);
        }
      }
      if (includeMethodForm(effectiveMethod, ObjectReferenceForms.ObjectHandle)) {
        analyzeMethod(effectiveMethod, ObjectReferenceForms.ObjectHandle, methodOrdinal++);
      }
      if (includeMethodForm(effectiveMethod,  ObjectReferenceForms.Primitive)) {
        analyzeMethod(effectiveMethod, ObjectReferenceForms.Primitive, methodOrdinal++);
      }
      if (includeMethodForm(effectiveMethod,  ObjectReferenceForms.PrimitiveWrapper)) {
        analyzeMethod(effectiveMethod, ObjectReferenceForms.PrimitiveWrapper, methodOrdinal++);
      }
    }
  }

  protected boolean includeMethodForm(MethodStatement method, ObjectReferenceForm targetForm) {
    if (ObjectReferenceForms.Plain.is(targetForm)) {
      return ObjectReferenceForms.Plain.is(getForm());
    } else if (ObjectReferenceForms.ObjectHandle.is(targetForm)) {
      return ObjectReferenceForms.ObjectHandle.is(getForm());
    } else if (ObjectReferenceForms.Primitive.is(targetForm)) {
      return isPrimitiveWrapper(method.returnType().orElseThrow());
    } else if (ObjectReferenceForms.PrimitiveWrapper.is(targetForm)) {
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
      MethodStatement method, ObjectReferenceForm targetForm, int methodOrdinal
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
      MethodStatement method, ObjectReferenceForm targetForm, int methodOrdinal
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
        sb.append(ObjectReferenceFunctions.getObjectFormDeclaration(param.type(), ObjectReferenceForms.Plain, MovabilityTypes.Undefined, true, this::addImportAndGetSimpleName));
        sb.append(") ");
      }
      sb.append(param.name());
    }
    sb.append(");\n");
    sb.append("}\n");
    return Map.of("declaration", sb.toString());
  }

  private boolean isDisableMoving(MethodStatement method) {
    return ChannelFunctions.isMovingBasedChannel(method)
        && ObjectReferenceForms.ObjectHandle.is(getForm())
        && MovabilityTypes.Unmovable.is(getMovabilityType());
  }

  protected void appendMethodReturnType(StringBuilder sb, MethodStatement method) {
    if (method.returnType().isEmpty()) {
      sb.append("void");
    }
    TypeReference returnType = method.returnType().orElseThrow();
    sb.append(returnType.actualDeclaration(this::convertName));
  }

  private String convertName(String name) {
    KeyDomain keyDomain = Jaquarius.settings().getKeyDomainByName(NameConventionFunctions.convertToDomainName(name));
    if (keyDomain != null && keyDomain.delegateClassName() != null) {
      return addImportAndGetSimpleName(keyDomain.delegateClassName());
    }
    return addImportAndGetSimpleName(name);
  }

  protected Stream<MethodStatement> getObjectFormMethods(CustomType customType, ArtifactGeneratorContext context) {
    return customType.actualMethods().stream()
        .filter(m -> DomainFunctions.isDomainType(m.owner()));
  }

  protected CustomType buildActualType(CustomType domainType, ArtifactGeneratorContext context) {
    InterfaceType domain = domainType.asInterfaceOrElseThrow();

    var builder = Interfaces.build(domain);
    findExtensionMethods(domain, context.initialRoundEnvironment()).forEach(builder::addDeclaredMethod);

    var extendedInterfaces = new ArrayList<CustomTypeReference>();
    for (CustomTypeReference superDomain : domain.extendedInterfaces()) {
      extendedInterfaces.add(
          CustomTypeReferences.get(buildActualType(superDomain.targetType(), context), superDomain.typeArguments())
      );
    }
    builder.extendedInterfaces(extendedInterfaces);
    return builder.get();
  }

  protected List<CustomType> findExtensions(CustomType domainType, RoundEnvironment roundEnv) {
    var extensions = new ArrayList<CustomType>();
    for (ArtifactType artifactType : relatedArtifactTypes()) {
      extensions.addAll(AnnotationFunctions.findArtifactExtensions(domainType, artifactType, roundEnv));
    }
    return extensions;
  }

  private List<MethodStatement> findExtensionMethods(CustomType domainType, RoundEnvironment roundEnv) {
    List<MethodStatement> methods = new ArrayList<>();
    for (ArtifactType artifactType : relatedArtifactTypes()) {
      List<CustomType> extensions = AnnotationFunctions.findArtifactExtensions(
          domainType, artifactType, roundEnv
      );
      for (CustomType extension : extensions) {
        methods.addAll(extension.declaredMethods());
      }
    }
    return methods;
  }

  protected String buildDomainType(CustomType domainType, List<NotPrimitiveReference> typeQualifiers) {
    StringBuilder sb = new StringBuilder();
    sb.append("Types.get(");
    sb.append(addImportAndGetSimpleName(domainType.canonicalName())).append(".class");
    for (NotPrimitiveReference typeQualifier : typeQualifiers) {
      sb.append(", ");
      analyzeDomainType(typeQualifier, sb);
    }
    sb.append(");");
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

  protected String getObjectFormMethodName(MethodStatement domainMethod, ObjectReferenceForm targetForm) {
    if (ObjectReferenceForms.Plain.is(targetForm) || ObjectReferenceForms.ObjectHandle.is(targetForm)) {
      return domainMethod.name();
    } else if (ObjectReferenceForms.Primitive.is(targetForm)) {
      return domainMethod.name() + "AsPrimitive";
    } else if (ObjectReferenceForms.PrimitiveWrapper.is(targetForm)) {
      return domainMethod.name() + "AsObject";
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported object reference form - {0}", targetForm);
    }
  }

  protected void appendMethodReturnTypeDeclaration(
      StringBuilder sb, MethodStatement method, ObjectReferenceForm targetForm
  ) {
    if (ObjectReferenceForms.Plain.is(targetForm) || ObjectReferenceForms.ObjectHandle.is(targetForm)) {
      appendObjectFormMethodReturnType(sb, method);
    } else if (ObjectReferenceForms.Primitive.is(targetForm)) {
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
      } else if (method.hasAnnotation(Unmovable.class)) {
        sb.append(buildObjectFormDeclaration(domainReturnType, getForm(), MovabilityTypes.Unmovable, true));
      } else {
        sb.append(buildObjectFormDeclaration(domainReturnType, getForm(), MovabilityTypes.Undefined, true));
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
          ObjectReferenceForms.Plain,
          MovabilityTypes.Undefined,
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
