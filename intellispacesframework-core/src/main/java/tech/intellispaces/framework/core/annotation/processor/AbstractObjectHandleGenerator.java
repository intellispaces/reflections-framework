package tech.intellispaces.framework.core.annotation.processor;

import tech.intellispaces.framework.commons.action.Executor;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.common.Actions;
import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.core.space.transition.TransitionFunctions;
import tech.intellispaces.framework.core.traverse.TraverseTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.NamedTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractObjectHandleGenerator extends AbstractGenerator {
  protected String domainTypeParamsFull;
  protected String domainTypeParamsBrief;
  protected List<String> methods;

  public AbstractObjectHandleGenerator(CustomType customType) {
    super(customType);
  }

  abstract protected ObjectHandleTypes getObjectHandleType();

  abstract protected Stream<MethodStatement> getDomainMethods(CustomType customType);

  protected void analyzeObjectHandleMethods(CustomType customType) {
    this.methods = getDomainMethods(customType)
        .map(this::buildMethod)
        .filter(m -> !m.isEmpty())
        .toList();
  }

  protected String movableClassSimpleName() {
    return TypeFunctions.getSimpleName(
        NameFunctions.getMovableObjectHandleTypename(annotatedType.className()));
  }

  protected String buildMethod(MethodStatement method) {
    var sb = new StringBuilder();
    appendMethodTypeParameters(sb, method);
    boolean disableMoving = isDisableMoving(method);
    if (disableMoving) {
      sb.append("default ");
    }
    appendMethodReturnType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParameters(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);

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

  protected boolean isDisableMoving(MethodStatement method) {
    Transition transition = TransitionFunctions.getDomainMainTransitionAnnotation(method);
    return transition.type() == TraverseTypes.Moving && getObjectHandleType() == ObjectHandleTypes.Unmovable;
  }

  protected void appendMethodReturnType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    sb.append(getObjectHandleCanonicalName(domainReturnType, ObjectHandleTypes.Common));
  }

  protected void appendMethodTypeParameters(StringBuilder sb, MethodStatement method) {
    if (!method.typeParameters().isEmpty()) {
      sb.append("<");
      Executor commaAppender = Actions.buildCommaAppender(sb);
      for (NamedTypeReference namedTypeReference : method.typeParameters()) {
        commaAppender.execute();
        sb.append(namedTypeReference.actualDeclaration());
      }
      sb.append("> ");
    }
  }

  protected void appendMethodParameters(StringBuilder sb, MethodStatement method) {
    Executor commaAppender = Actions.buildCommaAppender(sb);
    for (MethodParam param : method.params()) {
      commaAppender.execute();
      sb.append(param.type().actualDeclaration());
      sb.append(" ");
      sb.append(param.name());
    }
  }

  protected void appendMethodExceptions(StringBuilder sb, MethodStatement method) {
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
