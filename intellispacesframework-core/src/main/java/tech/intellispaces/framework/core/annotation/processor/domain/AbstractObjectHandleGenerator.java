package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.commons.action.Action;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.Domain;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.annotation.processor.AbstractGenerator;
import tech.intellispaces.framework.core.common.ActionFunctions;
import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.core.space.transition.TransitionFunctions;
import tech.intellispaces.framework.core.traverse.TraverseTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.CustomTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.NamedTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class AbstractObjectHandleGenerator extends AbstractGenerator {
  protected String domainTypeParamsFull;
  protected String domainTypeParamsBrief;
  protected List<String> methods;

  AbstractObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  abstract protected ObjectHandleTypes getObjectHandleType();

  protected void analyzeObjectHandleMethods(CustomType domainType) {
    this.methods = getDomainMethods(domainType)
        .map(this::buildMethod)
        .toList();
  }

  protected String movableClassSimpleName() {
    return TypeFunctions.getSimpleName(
        NameFunctions.getMovableObjectHandleTypename(annotatedType.className()));
  }

  protected Stream<MethodStatement> getDomainMethods(CustomType domainType) {
    return domainType.actualMethods().stream()
        .filter(m -> m.holder().hasAnnotation(Domain.class))
        .filter(m -> !m.isDefault());
  }

  protected String buildMethod(MethodStatement method) {
    var sb = new StringBuilder();

    Transition transition = TransitionFunctions.getDomainMainTransitionAnnotation(method);
    boolean disableMoving = transition.type() != TraverseTypes.Mapping &&
        ObjectHandleTypes.Unmovable == getObjectHandleType();

    addMethodTypeParameters(sb, method);
    if (disableMoving) {
      sb.append("default ");
    }
    addMethodReturnType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    addMethodParameters(sb, method);
    sb.append(")");
    addMethodExceptions(sb, method);

    if (disableMoving) {
      context.addImport(TraverseException.class);
      String exceptionSimpleName = context.simpleNameOf(TraverseException.class);
      sb.append(" {\n");
      sb.append("    throw ").append(exceptionSimpleName).append(
          ".withMessage(\"Unmovable object handle cannot be moved\");\n");
      sb.append("  }");
    }
    return sb.toString();
  }

  protected void addMethodReturnType(StringBuilder sb, MethodStatement method) {
    if (method.returnType().isEmpty()) {
      sb.append("void");
    } else {
      TypeReference domainReturnType = method.returnType().get();
      String domainReturnTypeClassName = domainReturnType.asCustomTypeReference()
          .map(CustomTypeReference::targetType)
          .map(CustomType::className)
          .orElse(null);
      final ObjectHandleTypes objectHandleType;
      if (domainReturnType.isCustomTypeReference() && !method.holder().className().equals(domainReturnTypeClassName)) {
        objectHandleType = ObjectHandleTypes.Common;
      } else {
        objectHandleType = getObjectHandleType();
      }
      sb.append(getObjectHandleCanonicalName(domainReturnType, objectHandleType));
    }
  }

  protected void addMethodTypeParameters(StringBuilder sb, MethodStatement method) {
    if (!method.typeParameters().isEmpty()) {
      sb.append("<");
      Action addCommaAction = ActionFunctions.buildAppendSeparatorAction(sb, ", ");
      for (NamedTypeReference namedTypeReference : method.typeParameters()) {
        addCommaAction.execute();
        sb.append(namedTypeReference.actualDeclaration());
      }
      sb.append("> ");
    }
  }

  protected void addMethodParameters(StringBuilder sb, MethodStatement method) {
    Action addCommaAction = ActionFunctions.buildAppendSeparatorAction(sb, ", ");
    for (MethodParam param : method.params()) {
      addCommaAction.execute();
      sb.append(param.type().actualDeclaration());
      sb.append(" ");
      sb.append(param.name());
    }
  }

  protected void addMethodExceptions(StringBuilder sb, MethodStatement method) {
    String exceptions = method.exceptions().stream()
        .map(e -> e.asCustomTypeReference().orElseThrow().targetType())
        .peek(e -> context.addImport(e.canonicalName()))
        .map(e -> context.simpleNameOf(e.canonicalName()))
        .collect(Collectors.joining(", "));
    if (!exceptions.isEmpty()) {
      sb.append(" throws ").append(exceptions);
    }
  }
}
