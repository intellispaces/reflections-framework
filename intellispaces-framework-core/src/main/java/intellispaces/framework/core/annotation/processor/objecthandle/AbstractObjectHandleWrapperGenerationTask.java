package intellispaces.framework.core.annotation.processor.objecthandle;

import intellispaces.common.action.Actions;
import intellispaces.common.action.runner.Runner;
import intellispaces.common.base.text.TextActions;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.NamedReference;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.common.javastatement.type.Types;
import intellispaces.framework.core.action.TraverseActions;
import intellispaces.framework.core.annotation.processor.AbstractObjectHandleGenerationTask;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.guide.GuideFunctions;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.object.ObjectHandleTypes;
import intellispaces.framework.core.space.transition.TransitionFunctions;
import intellispaces.framework.core.traverse.TraverseTypes;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

abstract class AbstractObjectHandleWrapperGenerationTask extends AbstractObjectHandleGenerationTask {
  protected String domainSimpleClassName;
  protected String typeParamsFull;
  protected String typeParamsBrief;
  private final List<MethodStatement> domainMethods;
  protected List<Object> constructors;
  protected List<String> guideActions;
  protected List<String> transitionActions;

  AbstractObjectHandleWrapperGenerationTask(CustomType initiatorType, CustomType objectHandleType) {
    super(initiatorType, objectHandleType);

    CustomType domainType = ObjectFunctions.getDomainTypeOfObjectHandle(objectHandleType);
    this.domainMethods = domainType.actualMethods().stream()
        .filter(m -> !m.isDefault())
        .filter(this::isNotGetDomainMethod)
        .toList();
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType objectHandleType, RoundEnvironment roundEnv
  ) {
    return domainMethods.stream();
  }

  protected void analyzeTransitionActions() {
    this.transitionActions = domainMethods.stream()
        .map(this::buildTransitionAction)
        .toList();
  }

  private String buildTransitionAction(MethodStatement domainMethod) {
    var sb = new StringBuilder();
    sb.append(context.addToImportAndGetSimpleName(Actions.class));
    sb.append(".delegate");
    sb.append(domainMethod.params().size() + 1);
    sb.append("(");
    sb.append(context.addToImportAndGetSimpleName(Actions.class));
    sb.append(".cachedLazyGetter(");
    sb.append(context.addToImportAndGetSimpleName(TraverseActions.class));
    sb.append("::");
    sb.append(getTraverseType(domainMethod) == TraverseTypes.Mapping ? "map" : "move");
    sb.append("ThruTransition");
    sb.append(domainMethod.params().size());
    sb.append(",\n");
    sb.append("  ");
    sb.append(context.addToImportAndGetSimpleName(Types.class));
    sb.append(".<");
    sb.append(annotatedType.simpleName());
    sb.append(annotatedType.typeParametersFullDeclaration());
    sb.append(", ");
    sb.append(annotatedType.simpleName());
    sb.append("> of(");
    sb.append(annotatedType.simpleName());
    sb.append(".class),\n  ");
    sb.append(context.addToImportAndGetSimpleName(NameConventionFunctions.getTransitionClassCanonicalName(domainMethod)));
    sb.append(".class))");
    return sb.toString();
  }

  protected void analyzeGuideActions(CustomType objectHandleType) {
    this.guideActions = new ArrayList<>();

    List<MethodStatement> objectHandleMethods = objectHandleType.actualMethods();
    for (MethodStatement domainMethod : domainMethods) {
      if (domainMethod.isDefault()) {
        continue;
      }
      MethodStatement objectHandleMethod = findObjectHandleMethods(domainMethod, objectHandleMethods);
      if (objectHandleMethod == null) {
        this.guideActions.add("null");
      } else {
        this.guideActions.add(buildGuideAction(objectHandleMethod));
      }
    }
  }

  private MethodStatement findObjectHandleMethods(
      MethodStatement domainMethod, List<MethodStatement> objectHandleMethods
  ) {
    for (MethodStatement objectHandleMethod : objectHandleMethods) {
      if (!GuideFunctions.isGuideMethod(objectHandleMethod)) {
        continue;
      }
      if (
          domainMethod.name().equals(objectHandleMethod.name())
              && equalParams(domainMethod.params(), objectHandleMethod.params())
      ) {
        return objectHandleMethod;
      }
    }
    return null;
  }

  private boolean equalParams(List<MethodParam> domainMethodParams, List<MethodParam> guideMethodParams) {
    if (domainMethodParams.size() != guideMethodParams.size()) {
      return false;
    }

    boolean paramMatches = true;
    for (int i = 0; i < domainMethodParams.size(); i++) {
      MethodParam domainMethodParam = domainMethodParams.get(i);
      String domainMethodParamDeclaration = ObjectFunctions.getObjectHandleDeclaration(
          domainMethodParam.type(), ObjectHandleTypes.Common, Function.identity()
      );

      MethodParam guideMethodParam = guideMethodParams.get(i);
      String guideMethodParamDeclaration = guideMethodParam.type().actualDeclaration(TypeFunctions::shortenName);
      if (!domainMethodParamDeclaration.equals(guideMethodParamDeclaration)) {
        paramMatches = false;
        break;
      }
    }
    return paramMatches;
  }

  private String buildGuideAction(MethodStatement objectHandleMethod) {
    var sb = new StringBuilder();
    sb.append("Actions.of(super::");sb.append(objectHandleMethod.name());
    sb.append(", ");
    sb.append(buildProjectionActionTypeParameter(objectHandleMethod.returnType().orElseThrow()));
    sb.append(".class");
    for (MethodParam param : objectHandleMethod.params()) {
      sb.append(", ");
      sb.append(buildProjectionActionTypeParameter(param.type()));
      sb.append(".class");
    }
    sb.append(")");
    return sb.toString();
  }

  private String buildProjectionActionTypeParameter(TypeReference type) {
    if (type.isNamedReference()) {
      NamedReference namedType = type.asNamedReferenceOrElseThrow();
      if (namedType.extendedBounds().isEmpty()) {
        return "Object";
      }
    } else if (type.isCustomTypeReference()) {
      return context.addToImportAndGetSimpleName(type.asCustomTypeReferenceOrElseThrow().targetType().canonicalName());
    }
    return type.actualDeclaration(context::addToImportAndGetSimpleName);
  }

  private static TraverseTypes getTraverseType(MethodStatement domainMethod) {
    return TransitionFunctions.getTraverseType(domainMethod);
  }

  protected String getGeneratedClassCanonicalName() {
    return NameConventionFunctions.getObjectHandleWrapperCanonicalName(annotatedType);
  }

  protected void analyzeTypeParams(CustomType objectHandleType) {
    if (objectHandleType.typeParameters().isEmpty()) {
      typeParamsFull = typeParamsBrief = "";
      return;
    }

    var typeParamsFullBuilder = new StringBuilder();
    var typeParamsBriefBuilder = new StringBuilder();
    Runner commaAppender = TextActions.skippingFirstTimeCommaAppender(typeParamsFullBuilder, typeParamsBriefBuilder);

    typeParamsFullBuilder.append("<");
    typeParamsBriefBuilder.append("<");
    for (NamedReference typeParam : objectHandleType.typeParameters()) {
      commaAppender.run();
      typeParamsFullBuilder.append(typeParam.formalFullDeclaration());
      typeParamsBriefBuilder.append(typeParam.formalBriefDeclaration());
    }
    typeParamsFullBuilder.append(">");
    typeParamsBriefBuilder.append(">");
    typeParamsFull = typeParamsFullBuilder.toString();
    typeParamsBrief = typeParamsBriefBuilder.toString();
  }

  @SuppressWarnings("unchecked, rawtypes")
  protected void analyzeConstructors(CustomType objectHandleType) {
    List<MethodStatement> constructors;
    if (objectHandleType.asClass().isPresent()) {
      constructors = objectHandleType.asClass().get().constructors();
      List<Map<String, Object>> constructorDescriptors = new ArrayList<>();
      for (MethodStatement constructor : constructors) {
        constructorDescriptors.add(analyzeConstructor(constructor, context.getImportConsumer()));
      }
      this.constructors = (List) constructorDescriptors;
    } else {
      this.constructors = List.of();
    }
  }

  private Map<String, Object> analyzeConstructor(MethodStatement constructor, Consumer<String> imports) {
    Map<String, Object> constructorDescriptor = new HashMap<>();
    List<Map<String, String>> paramDescriptors = new ArrayList<>();
    for (MethodParam param : constructor.params()) {
      TypeReference type = param.type();
      type.dependencyTypenames().forEach(imports);
      paramDescriptors.add(Map.of(
              "name", param.name(),
              "type", type.actualDeclaration(context::simpleNameOf)
          )
      );
      type.asCustomTypeReference().ifPresent(t -> imports.accept(t.targetType().canonicalName()));
    }
    constructorDescriptor.put("params", paramDescriptors);
    return constructorDescriptor;
  }

  @Override
  protected void analyzeObjectHandleMethods(CustomType customType, RoundEnvironment roundEnv) {
    int index = 0;
    this.methods = new ArrayList<>();
    for (MethodStatement domainMethod : domainMethods) {
      this.methods.add(buildTransitionMethod(index++, domainMethod));
    }
  }

  protected Map<String, String> buildTransitionMethod(int methodIndex, MethodStatement domainMethod) {
    if (isDisableMoving(domainMethod)) {
      return Map.of();
    }

    var sb = new StringBuilder();
    sb.append("@Ordinal(").append(methodIndex).append(")\n");
    sb.append("public ");
    appendMethodTypeParameters(sb, domainMethod);
    appendMethodReturnHandleType(sb, domainMethod);
    sb.append(" ");
    sb.append(domainMethod.name());
    sb.append("(");
    appendMethodParameters(sb, domainMethod);
    sb.append(")");
    appendMethodExceptions(sb, domainMethod);
    sb.append(" {\n");
    sb.append("  return (");
    appendMethodReturnHandleType(sb, domainMethod);
    sb.append(") $handle.getTransitionAction(");
    sb.append(methodIndex);
    sb.append(").asAction");
    sb.append(domainMethod.params().size() + 1);
    sb.append("().execute(this");
    for (MethodParam param : domainMethod.params()) {
      sb.append(", ");
      sb.append(param.name());
    }
    sb.append(");\n}");
    return Map.of("declaration", sb.toString());
  }
}
