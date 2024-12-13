package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.general.type.ClassFunctions;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.SpaceConstants;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.CustomTypeReferences;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractObjectHandleGenerator extends JaquariusArtifactGenerator {
  protected String domainTypeParamsFull;
  protected String domainTypeParamsBrief;
  protected final List<Map<String, String>> conversionMethods = new ArrayList<>();
  protected final List<Map<String, String>> methods = new ArrayList<>();

  public AbstractObjectHandleGenerator(CustomType customType) {
    super(customType);
  }

  abstract protected ObjectHandleTypes getObjectHandleType();

  protected String movableClassSimpleName() {
    return addToImportAndGetSimpleName(
        NameConventionFunctions.getMovableObjectHandleTypename(sourceArtifact().className())
    );
  }

  protected boolean isNotDomainClassGetter(MethodStatement method) {
    return !method.name().equals("domainClass") &&
        !method.name().equals(SpaceConstants.POINT_TO_DOMAIN_CHANNEL_SIMPLE_NAME);
  }

  protected void analyzeObjectHandleMethods(CustomType type, ArtifactGeneratorContext context) {
    List<MethodStatement> methods = getObjectHandleMethods(type, context).toList();
    int methodIndex = 0;
    for (MethodStatement method : methods) {
      MethodStatement effectiveMethod = convertMethodBeforeGenerate(method);
      analyzeMethod(effectiveMethod, ObjectReferenceForms.Object, methodIndex++);
      if (method.returnType().orElseThrow().isCustomTypeReference()) {
        CustomType returnType = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
        if (ClassFunctions.isPrimitiveWrapperClass(returnType.canonicalName())) {
          analyzeMethod(effectiveMethod, ObjectReferenceForms.Primitive, methodIndex++);
        }
      }
    }
  }

  protected void analyzeConversionMethods(CustomType domainType, ArtifactGeneratorContext context) {
    analyzeConversionMethods(domainType, domainType, context.roundEnvironment());
  }

  private void analyzeConversionMethods(
      CustomType customType, CustomType effectiveCustomType, RoundEnvironment roundEnv
  ) {
    Iterator<CustomTypeReference> parents = customType.parentTypes().iterator();
    Iterator<CustomTypeReference> effectiveParents = effectiveCustomType.parentTypes().iterator();
    while (parents.hasNext() && effectiveParents.hasNext()) {
      CustomTypeReference parent = parents.next();
      CustomTypeReference effectiveParent = effectiveParents.next();
      if (DomainFunctions.isAliasOf(parent, customType)) {
        analyzeConversionMethods(parent.targetType(), effectiveParent.effectiveTargetType(), roundEnv);
      } else {
        conversionMethods.add(buildConversionMethod(CustomTypeReferences.get(effectiveParent.effectiveTargetType())));
      }
    }
  }

  protected Map<String, String> buildConversionMethod(CustomTypeReference parent) {
    var sb = new StringBuilder();
    sb.append(getObjectHandleDeclaration(parent, getObjectHandleType()));
    sb.append(" ");
    sb.append(NameConventionFunctions.getConversionMethodName(parent));
    sb.append("()");
    return Map.of(
        "declaration", sb.toString()
    );
  }

  abstract protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, ArtifactGeneratorContext context
  );

  protected MethodStatement convertMethodBeforeGenerate(MethodStatement method) {
    return method;
  }

  protected void analyzeMethod(MethodStatement method, ObjectReferenceForm targetForm, int methodIndex) {
    methods.add(generateMethod(method, targetForm, methodIndex));
  }

  protected Map<String, String> generateMethod(MethodStatement method, ObjectReferenceForm targetForm, int methodIndex) {
    var sb = new StringBuilder();
    appendMethodTypeParameters(sb, method);
    boolean disableMoving = isDisableMoving(method);
    if (disableMoving) {
      sb.append("default ");
    }
    appendMethodReturnHandleType(sb, method, targetForm);
    sb.append(" ");
    sb.append(getMethodName(method, targetForm));
    sb.append("(");
    appendMethodParameters(sb, method);
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

  protected String getMethodName(MethodStatement method, ObjectReferenceForm targetForm) {
    if (targetForm == ObjectReferenceForms.Object) {
      return method.name();
    } else if (targetForm == ObjectReferenceForms.Primitive) {
      return method.name() + "Primitive";
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported guide form - {0}", targetForm);
    }
  }

  protected boolean isDisableMoving(MethodStatement method) {
    Channel channel = ChannelFunctions.getDomainMainChannelAnnotation(method);
    return ChannelFunctions.getTraverseType(channel).isMovingBased() &&
        getObjectHandleType() == ObjectHandleTypes.Unmovable;
  }

  protected void appendMethodReturnType(StringBuilder sb, MethodStatement method) {
    if (method.returnType().isEmpty()) {
      sb.append("void");
    }
    TypeReference returnType = method.returnType().orElseThrow();
    sb.append(returnType.actualDeclaration(this::addToImportAndGetSimpleName));
  }

  protected void appendMethodReturnHandleType(StringBuilder sb, MethodStatement method, ObjectReferenceForm targetForm) {
    if (targetForm == ObjectReferenceForms.Object) {
      appendMethodReturnHandleType(sb, method);
    } else if (targetForm == ObjectReferenceForms.Primitive) {
      CustomType ct = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
      sb.append(ClassFunctions.getPrimitiveTypeOfWrapper(ct.canonicalName()));
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported guide form - {0}", targetForm);
    }
  }

  protected void appendMethodReturnHandleType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (NameConventionFunctions.isConversionMethod(method)) {
      sb.append(getObjectHandleDeclaration(domainReturnType, getObjectHandleType()));
    } else {
      if (method.hasAnnotation(Movable.class)) {
        sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Movable));
      } else if (method.hasAnnotation(Unmovable.class)) {
        sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Unmovable));
      } else {
        sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Common));
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

  protected void appendMethodParameters(StringBuilder sb, MethodStatement method) {
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : GuideProcessorFunctions.rearrangementParams(method.params())) {
      commaAppender.run();
      sb.append(getObjectHandleDeclaration(GuideProcessorFunctions.normalizeType(param.type()), ObjectHandleTypes.Common));
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
