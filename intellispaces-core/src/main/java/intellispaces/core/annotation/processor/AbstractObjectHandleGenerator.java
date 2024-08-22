package intellispaces.core.annotation.processor;

import intellispaces.actions.common.string.StringActions;
import intellispaces.actions.runner.Runner;
import intellispaces.core.annotation.Transition;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.exception.TraverseException;
import intellispaces.core.object.ObjectHandleTypes;
import intellispaces.core.space.SpaceConstants;
import intellispaces.core.space.transition.TransitionFunctions;
import intellispaces.core.traverse.TraverseTypes;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodParam;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.NamedReference;
import intellispaces.javastatements.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractObjectHandleGenerator extends AbstractGenerator {
  protected String domainTypeParamsFull;
  protected String domainTypeParamsBrief;
  protected List<Map<String, String>> methods;

  public AbstractObjectHandleGenerator(CustomType customType) {
    super(customType);
  }

  abstract protected ObjectHandleTypes getObjectHandleType();

  abstract protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, RoundEnvironment roundEnv
  );

  protected boolean isNotGetDomainMethod(MethodStatement method) {
    return !method.name().equals("domainClass") &&
        !method.name().equals(SpaceConstants.POINT_TO_DOMAIN_TRANSITION_SIMPLE_NAME);
  }

  protected void analyzeObjectHandleMethods(CustomType customType, RoundEnvironment roundEnv) {
    this.methods = getObjectHandleMethods(customType, roundEnv)
        .map(this::buildMethod)
        .filter(m -> !m.isEmpty())
        .toList();
  }

  protected String movableClassSimpleName() {
    return context.addToImportAndGetSimpleName(
        NameConventionFunctions.getMovableObjectHandleTypename(annotatedType.className())
    );
  }

  protected Map<String, String> buildMethod(MethodStatement method) {
    var sb = new StringBuilder();
    appendMethodTypeParameters(sb, method);
    boolean disableMoving = isDisableMoving(method);
    if (disableMoving) {
      sb.append("default ");
    }
    appendMethodReturnHandleType(sb, method);
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
          ".withMessage(\"Unmovable object cannot be moved\");\n");
      sb.append("  }");
    }
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", sb.toString()
    );
  }

  protected boolean isDisableMoving(MethodStatement method) {
    Transition transition = TransitionFunctions.getDomainMainTransitionAnnotation(method);
    return TransitionFunctions.getTraverseType(transition) == TraverseTypes.Moving &&
        getObjectHandleType() == ObjectHandleTypes.Unmovable;
  }

  protected void appendMethodReturnType(StringBuilder sb, MethodStatement method) {
    if (method.returnType().isEmpty()) {
      sb.append("void");
    }
    TypeReference returnType = method.returnType().orElseThrow();
    sb.append(returnType.actualDeclaration(context::addToImportAndGetSimpleName));
  }

  protected void appendMethodReturnHandleType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Common));
  }

  protected void appendMethodTypeParameters(StringBuilder sb, MethodStatement method) {
    if (!method.typeParameters().isEmpty()) {
      sb.append("<");
      Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
      for (NamedReference namedTypeReference : method.typeParameters()) {
        commaAppender.run();
        sb.append(namedTypeReference.actualDeclaration());
      }
      sb.append("> ");
    }
  }

  protected void appendMethodParameters(StringBuilder sb, MethodStatement method) {
    Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
    for (MethodParam param : method.params()) {
      commaAppender.run();
      sb.append(getObjectHandleDeclaration(param.type(), ObjectHandleTypes.Common));
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
