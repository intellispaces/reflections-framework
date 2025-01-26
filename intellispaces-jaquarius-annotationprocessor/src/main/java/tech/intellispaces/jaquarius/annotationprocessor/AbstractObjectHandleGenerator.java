package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.general.exception.NotImplementedExceptions;
import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.general.type.ClassFunctions;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleMethodForm;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleMethodForms;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.BasicDomain;
import tech.intellispaces.jaquarius.space.domain.BasicDomains;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.customtype.InterfaceType;
import tech.intellispaces.java.reflection.customtype.Interfaces;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.CustomTypeReferences;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.NotPrimitiveReference;
import tech.intellispaces.java.reflection.reference.ReferenceBound;
import tech.intellispaces.java.reflection.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractObjectHandleGenerator extends JaquariusArtifactGenerator {
  protected final List<Map<String, String>> methods = new ArrayList<>();

  public AbstractObjectHandleGenerator(CustomType sourceArtifact) {
    super(sourceArtifact);
  }

  abstract protected ObjectHandleType getObjectHandleType();

  protected String movableClassSimpleName() {
    return addImportAndGetSimpleName(
        NameConventionFunctions.getMovableObjectHandleTypename(sourceArtifact().className())
    );
  }

  protected void analyzeObjectHandleMethods(CustomType type, ArtifactGeneratorContext context) {
    List<MethodStatement> methods = getObjectHandleMethods(type, context).toList();
    int methodOrdinal = 0;
    for (MethodStatement method : methods) {
      MethodStatement effectiveMethod = convertMethodBeforeGenerate(method);
      if (includeMethodForm(effectiveMethod, ObjectHandleMethodForms.Object)) {
        analyzeMethod(effectiveMethod, ObjectHandleMethodForms.Object, methodOrdinal++);
      }
      if (hasMethodNormalForm(effectiveMethod)) {
        if (includeMethodForm(effectiveMethod, ObjectHandleMethodForms.Normal)) {
          analyzeMethod(effectiveMethod, ObjectHandleMethodForms.Normal, methodOrdinal++);
        }
      }
    }
  }

  protected boolean includeMethodForm(MethodStatement method, ObjectHandleMethodForm methodForm) {
    return true;
  }

  protected boolean hasMethodNormalForm(MethodStatement method) {
    TypeReference returnTypeReference = method.returnType().orElseThrow();
    if (isPrimitiveWrapper(returnTypeReference)) {
      return true;
    }
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

  private void analyzeMethod(MethodStatement method, ObjectHandleMethodForm methodForm, int methodOrdinal) {
    methods.add(generateMethod(method, methodForm, methodOrdinal));
  }

  protected MethodStatement convertMethodBeforeGenerate(MethodStatement method) {
    return method;
  }

  protected Map<String, String> generateMethod(
      MethodStatement method, ObjectHandleMethodForm methodForm, int methodOrdinal
  ) {
    ObjectReferenceForm targetForm = getTargetForm(method, methodForm);

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

  protected ObjectReferenceForm getTargetForm(MethodStatement method, ObjectHandleMethodForm methodForm) {
    if (methodForm.is(ObjectHandleMethodForms.Object.name())) {
      return ObjectReferenceForms.Object;
    }
    if (methodForm.is(ObjectHandleMethodForms.Normal.name())) {
      return ObjectReferenceForms.Normal;
    }
    throw NotImplementedExceptions.withCode("Pelptg");
  }

  private boolean isDisableMoving(MethodStatement method) {
    return ChannelFunctions.isMovingBasedChannel(method) && ObjectHandleTypes.Unmovable.is(getObjectHandleType());
  }

  protected void appendMethodReturnType(StringBuilder sb, MethodStatement method) {
    if (method.returnType().isEmpty()) {
      sb.append("void");
    }
    TypeReference returnType = method.returnType().orElseThrow();
    sb.append(returnType.actualDeclaration(this::convertName));
  }

  private String convertName(String name) {
    BasicDomain basicDomain = BasicDomains.active().getByDomainName(NameConventionFunctions.convertJaquariusDomainName(name));
    if (basicDomain != null) {
      return addImportAndGetSimpleName(basicDomain.delegateClassName());
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
    if (ObjectReferenceForms.Object.is(targetForm)) {
      return domainMethod.name();
    } else if (ObjectReferenceForms.Normal.is(targetForm)) {
      if (isPrimitiveWrapper(domainMethod.returnType().orElseThrow())) {
        return domainMethod.name() + "AsPrimitive";
      } else {
        return domainMethod.name();
      }
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported object reference form - {0}", targetForm);
    }
  }

  protected void appendMethodReturnHandleType(
      StringBuilder sb, MethodStatement method, ObjectReferenceForm targetForm
  ) {
    if (ObjectReferenceForms.Object.is(targetForm)) {
      appendObjectFormMethodReturnType(sb, method);
    } else if (ObjectReferenceForms.Normal.is(targetForm)) {
      if (isPrimitiveWrapper(method.returnType().orElseThrow())) {
        CustomType ct = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
        sb.append(ClassFunctions.primitiveTypenameOfWrapper(ct.canonicalName()));
      } else {
        appendObjectFormMethodReturnType(sb, method);
      }
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported guide form - {0}", targetForm);
    }
  }

  protected void appendObjectFormMethodReturnType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (NameConventionFunctions.isConversionMethod(method)) {
      sb.append(buildObjectHandleDeclaration(domainReturnType, getObjectHandleType()));
    } else {
      if (method.hasAnnotation(Movable.class)) {
        sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Movable));
      } else if (method.hasAnnotation(Unmovable.class)) {
        sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Unmovable));
      } else {
        sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.General));
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

  protected void appendMethodParams(StringBuilder sb, MethodStatement method, ObjectHandleMethodForm methodForm) {
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : AnnotationGeneratorFunctions.rearrangementParams(method.params())) {
      commaAppender.run();
      sb.append(buildHandleDeclarationDefaultForm(
          AnnotationGeneratorFunctions.normalizeType(param.type()),
          ObjectHandleTypes.General,
          ObjectHandleMethodForms.Object.equals(methodForm) ? ObjectReferenceForms.Object :  ObjectReferenceForms.Normal
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

  protected boolean excludeDeepConversionMethods(MethodStatement method, CustomType customType) {
    if (!NameConventionFunctions.isConversionMethod(method)) {
      return true;
    }
    for (CustomTypeReference parent : customType.parentTypes()) {
      if (DomainFunctions.isAliasOf(parent, customType)) {
        if (excludeDeepConversionMethods(method, parent.targetType())) {
          return true;
        }
      }
      if (NameConventionFunctions.getConversionMethodName(parent).equals(method.name())) {
        return true;
      }
    }
    return false;
  }
}
