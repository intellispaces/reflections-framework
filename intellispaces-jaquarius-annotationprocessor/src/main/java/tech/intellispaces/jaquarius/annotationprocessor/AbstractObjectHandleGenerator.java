package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.commons.action.runnable.RunnableAction;
import tech.intellispaces.commons.action.text.StringActions;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.base.exception.UnexpectedExceptions;
import tech.intellispaces.commons.base.type.ClassFunctions;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.customtype.InterfaceType;
import tech.intellispaces.commons.java.reflection.customtype.Interfaces;
import tech.intellispaces.commons.java.reflection.method.MethodParam;
import tech.intellispaces.commons.java.reflection.method.MethodSignatureDeclarations;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.commons.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.commons.java.reflection.reference.CustomTypeReferences;
import tech.intellispaces.commons.java.reflection.reference.NamedReference;
import tech.intellispaces.commons.java.reflection.reference.NotPrimitiveReference;
import tech.intellispaces.commons.java.reflection.reference.ReferenceBound;
import tech.intellispaces.commons.java.reflection.reference.TypeReference;
import tech.intellispaces.jaquarius.Jaquarius;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.settings.KeyDomain;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseQualifierSetForm;
import tech.intellispaces.jaquarius.traverse.TraverseQualifierSetForms;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractObjectHandleGenerator extends JaquariusArtifactGenerator {
  protected final List<Map<String, String>> methods = new ArrayList<>();
  protected final List<Map<String, String>> rawDomainMethods = new ArrayList<>();

  public AbstractObjectHandleGenerator(CustomType sourceArtifact) {
    super(sourceArtifact);
  }

  abstract protected ObjectHandleType getObjectHandleType();

  protected String movableClassSimpleName() {
    return addImportAndGetSimpleName(
        NameConventionFunctions.getMovableObjectHandleTypename(sourceArtifact().className(), false)
    );
  }

  protected void analyzeObjectHandleMethods(CustomType type, ArtifactGeneratorContext context) {
    List<MethodStatement> methods = getObjectHandleMethods(type, context).toList();
    int methodOrdinal = 0;
    for (MethodStatement method : methods) {
      MethodStatement effectiveMethod = convertMethodBeforeGenerate(method);
      analyzeRawDomainMethod(effectiveMethod);
      if (includeMethodForm(effectiveMethod, TraverseQualifierSetForms.Object, ObjectReferenceForms.Default)) {
        analyzeMethod(effectiveMethod, TraverseQualifierSetForms.Object, ObjectReferenceForms.Default, methodOrdinal++);
      }
      if (hasMethodNormalForm(effectiveMethod)) {
        if (includeMethodForm(effectiveMethod, TraverseQualifierSetForms.Normal, ObjectReferenceForms.Default)) {
          analyzeMethod(effectiveMethod, TraverseQualifierSetForms.Normal, ObjectReferenceForms.Default, methodOrdinal++);
        }
      }
      if (includeMethodForm(effectiveMethod, TraverseQualifierSetForms.Normal, ObjectReferenceForms.Primitive)) {
        analyzeMethod(effectiveMethod, TraverseQualifierSetForms.Normal, ObjectReferenceForms.Primitive, methodOrdinal++);
      }
    }
  }

  protected boolean includeMethodForm(
      MethodStatement method, TraverseQualifierSetForm methodForm, ObjectReferenceForm targetForm
  ) {
    if (ObjectReferenceForms.Primitive.is(targetForm)) {
      return isPrimitiveWrapper(method.returnType().orElseThrow());
    }
    return true;
  }

  protected boolean hasMethodNormalForm(MethodStatement method) {
    for (MethodParam param : method.params()) {
      if (isPrimitiveWrapper(param.type())) {
        return true;
      }
    }
    return false;
  }

  protected boolean isPrimitiveWrapper(TypeReference typeReference) {
    if (typeReference.isCustomTypeReference()) {
      CustomType returnType = typeReference.asCustomTypeReferenceOrElseThrow().targetType();
      if (ClassFunctions.isPrimitiveWrapperClass(returnType.canonicalName())) {
        return true;
      }
    }
    return false;
  }

  private void analyzeMethod(
      MethodStatement method, TraverseQualifierSetForm methodForm, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    methods.add(generateMethod(method, methodForm, targetForm,  methodOrdinal));
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
      MethodStatement method, TraverseQualifierSetForm methodForm, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    var sb = new StringBuilder();
    appendMethodTypeParameters(sb, method);
    boolean disableMoving = isDisableMoving(method);
    if (disableMoving) {
      sb.append("default ");
    }
    appendMethodReturnHandleType(sb, method, targetForm);
    sb.append(" ");
    sb.append(getObjectHandleMethodName(method, targetForm));
    sb.append("(");
    appendMethodParams(sb, method, methodForm);
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
        sb.append(ObjectHandleFunctions.getObjectHandleDeclaration(param.type(), ObjectHandleTypes.UndefinedPureObject, true, this::addImportAndGetSimpleName));
        sb.append(") ");
      }
      sb.append(param.name());
    }
    sb.append(");\n");
    sb.append("}\n");
    return Map.of("declaration", sb.toString());
  }

  private boolean isDisableMoving(MethodStatement method) {
    return ChannelFunctions.isMovingBasedChannel(method) && ObjectHandleTypes.UnmovableHandle.is(getObjectHandleType());
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

  protected Stream<MethodStatement> getObjectHandleMethods(CustomType customType, ArtifactGeneratorContext context) {
    return customType.actualMethods().stream()
        .filter(m -> DomainFunctions.isDomainType(m.owner()));
  }

  protected CustomType buildActualType(CustomType domain, ArtifactGeneratorContext context) {
    InterfaceType domainInterface = domain.asInterfaceOrElseThrow();

    var builder = Interfaces.build(domainInterface);
    getAdditionalOMethods(domainInterface, context.roundEnvironment()).forEach(builder::addDeclaredMethod);

    var extendedInterfaces = new ArrayList<CustomTypeReference>();
    for (CustomTypeReference superDomain : domainInterface.extendedInterfaces()) {
      extendedInterfaces.add(
          CustomTypeReferences.get(buildActualType( superDomain.targetType(), context), superDomain.typeArguments())
      );
    }
    builder.extendedInterfaces(extendedInterfaces);
    return builder.get();
  }

  private List<MethodStatement> getAdditionalOMethods(CustomType customType, RoundEnvironment roundEnv) {
    List<MethodStatement> methods = new ArrayList<>();
    List<CustomType> artifactAddOns = AnnotationProcessorFunctions.findArtifactAddOns(
        customType, ArtifactTypes.ObjectHandle, roundEnv
    );
    for (CustomType artifactAddOn : artifactAddOns) {
      methods.addAll(artifactAddOn.declaredMethods());
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

  protected String getObjectHandleMethodName(MethodStatement domainMethod, ObjectReferenceForm targetForm) {
    if (ObjectReferenceForms.Default.is(targetForm)) {
      return domainMethod.name();
    } else if (ObjectReferenceForms.Primitive.is(targetForm)) {
      return domainMethod.name() + "AsPrimitive";
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported object reference form - {0}", targetForm);
    }
  }

  protected void appendMethodReturnHandleType(
      StringBuilder sb, MethodStatement method, ObjectReferenceForm targetForm
  ) {
    if (ObjectReferenceForms.Default.is(targetForm)) {
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
      sb.append(buildObjectHandleDeclaration(domainReturnType, getObjectHandleType(), true));
    } else {
      if (method.hasAnnotation(Movable.class)) {
        sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.MovablePureObject, true));
      } else if (method.hasAnnotation(Unmovable.class)) {
        sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.UnmovablePureObject, true));
      } else {
        sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.UndefinedPureObject, true));
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

  protected void appendMethodParams(StringBuilder sb, MethodStatement method, TraverseQualifierSetForm methodForm) {
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : AnnotationGeneratorFunctions.rearrangementParams(method.params())) {
      commaAppender.run();
      sb.append(buildHandleDeclarationDefaultForm(
          AnnotationGeneratorFunctions.normalizeType(param.type()),
          ObjectHandleTypes.UndefinedPureObject,
          true,
          ObjectHandleFunctions.getReferenceForm(param.type(), methodForm)
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
