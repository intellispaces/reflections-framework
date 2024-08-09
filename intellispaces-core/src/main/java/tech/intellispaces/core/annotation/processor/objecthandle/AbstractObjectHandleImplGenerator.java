package tech.intellispaces.core.annotation.processor.objecthandle;

import tech.intellispaces.actions.Actions;
import tech.intellispaces.actions.common.string.StringActions;
import tech.intellispaces.actions.executor.Executor;
import tech.intellispaces.actions.getter.Getter;
import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.annotation.processor.AbstractObjectHandleGenerator;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.guide.GuideFunctions;
import tech.intellispaces.core.guide.n0.Mapper0;
import tech.intellispaces.core.guide.n0.Mover0;
import tech.intellispaces.core.guide.n1.Mapper1;
import tech.intellispaces.core.guide.n1.Mover1;
import tech.intellispaces.core.guide.n2.Mapper2;
import tech.intellispaces.core.guide.n2.Mover2;
import tech.intellispaces.core.guide.n3.Mapper3;
import tech.intellispaces.core.guide.n3.Mover3;
import tech.intellispaces.core.guide.n4.Mapper4;
import tech.intellispaces.core.guide.n4.Mover4;
import tech.intellispaces.core.guide.n5.Mapper5;
import tech.intellispaces.core.guide.n5.Mover5;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.core.space.transition.TransitionFunctions;
import tech.intellispaces.core.system.Modules;
import tech.intellispaces.core.traverse.TraverseTypes;
import tech.intellispaces.javastatements.StatementTypes;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodParam;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.NamedReference;
import tech.intellispaces.javastatements.reference.TypeReference;
import tech.intellispaces.javastatements.type.Type;
import tech.intellispaces.javastatements.type.Types;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

abstract class AbstractObjectHandleImplGenerator extends AbstractObjectHandleGenerator {
  protected String domainSimpleClassName;
  protected String typeParamsFull;
  protected String typeParamsBrief;
  protected List<Object> constructors;
  protected List<String> guideGetters;
  protected List<Map<String, String>> actionGetters;
  protected List<String> actionGetterSuppliers;

  AbstractObjectHandleImplGenerator(CustomType objectHandleType) {
    super(objectHandleType);
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType objectHandleType, RoundEnvironment roundEnv
  ) {
    CustomType domainType = ObjectFunctions.getDomainTypeOfObjectHandle(objectHandleType);
    return domainType.actualMethods().stream()
        .filter(m -> !m.isDefault())
        .filter(this::isNotGetDomainMethod);
  }

  protected void analyzeActionGetters(CustomType objectHandleType, RoundEnvironment roundEnv) {
    List<MethodStatement> methods = getObjectHandleMethods(objectHandleType, roundEnv).toList();
    this.actionGetters = methods.stream().map(this::buildGuideActionGetter).toList();
    this.actionGetterSuppliers = methods.stream().map(this::buildActionGetterSupplier).toList();
  }

  private Map<String, String> buildGuideActionGetter(MethodStatement domainMethod) {
    String actionGetterMame = buildActionGetterName(domainMethod);

    var declaration = new StringBuilder();
    declaration.append("private final ResettableGetter<Action> ");
    declaration.append(actionGetterMame);
    declaration.append(" = Actions.resettableGetter(\n");
    declaration.append("  Actions.get(super::").append(domainMethod.name());
    declaration.append(", ");
    declaration.append(buildGuideActionTypeParameter(domainMethod.returnType().orElseThrow())).append(".class");
    for (MethodParam param : domainMethod.params()) {
      declaration.append(", ");
      declaration.append(buildGuideActionTypeParameter(param.type())).append(".class");
    }
    declaration.append(")\n");
    declaration.append(");");
    return Map.of(
        "name", actionGetterMame,
        "declaration", declaration.toString()
    );
  }

  private String buildActionGetterSupplier(MethodStatement domainMethod) {
    var sb = new StringBuilder();
    sb.append("public ResettableGetter<Action> ");
    sb.append(GuideFunctions.getActionGetterSupplierName(domainMethod));
    sb.append("() {\n");
    sb.append("  return this.").append(buildActionGetterName(domainMethod)).append(";");
    sb.append("\n}");
    return sb.toString();
  }

  protected void analyzeGuideGetters(
      CustomType objectHandleType, RoundEnvironment roundEnv
  ) {
    this.guideGetters = getObjectHandleMethods(objectHandleType, roundEnv)
        .map(this::buildGuideGetter)
        .toList();
  }

  private String buildGuideGetter(MethodStatement domainMethod) {
    var sb = new StringBuilder();
    sb.append("private final ");
    sb.append(context.addToImportAndGetSimpleName(Getter.class));
    sb.append("<");
    sb.append(buildGuideTypeDeclaration(domainMethod));
    sb.append("> ");
    sb.append(buildGuideGetterName(domainMethod));
    sb.append(" = ");
    sb.append(context.addToImportAndGetSimpleName(Actions.class));
    sb.append(".cachedLazyGetter(() -> {\n  ");
    sb.append(buildGuideGetterSupplier(domainMethod));
    sb.append("\n});");
    return sb.toString();
  }

  private String buildGuideGetterName(MethodStatement domainMethod) {
    var sb = new StringBuilder();
    sb.append("_");
    sb.append(NameConventionFunctions.joinMethodNameAndParameterTypes(domainMethod));
    sb.append("GuideGetter");
    return sb.toString();
  }

  private String buildActionGetterName(MethodStatement domainMethod) {
    var sb = new StringBuilder();
    sb.append("_");
    sb.append(NameConventionFunctions.joinMethodNameAndParameterTypes(domainMethod));
    sb.append("ActionGetter");
    return sb.toString();
  }

  private String buildGuideTypeDeclaration(MethodStatement domainMethod) {
    Class<?> guideClass = getGuideClass(domainMethod);
    var sb = new StringBuilder();
    sb.append(context.addToImportAndGetSimpleName(guideClass));
    sb.append("<");
    sb.append(annotatedType.simpleName());
    sb.append(annotatedType.typeParametersFullDeclaration());
    sb.append(", ");
    sb.append(buildGuideMethodTypeParameter(domainMethod.returnType().orElseThrow()));
    for (MethodParam param : domainMethod.params()) {
      sb.append(", ");
      sb.append(buildGuideMethodTypeParameter(param.type()));
    }
    sb.append(">");
    return sb.toString();
  }

  private String buildGuideMethodTypeParameter(TypeReference type) {
    if (type.isNamedReference()) {
      NamedReference namedType = type.asNamedReferenceOrElseThrow();
      if (namedType.owner().statementType() == StatementTypes.Method) {
        if (namedType.extendedBounds().isEmpty()) {
          return "Object";
        }
      }
    }
    return ObjectFunctions.getBaseObjectHandleTypename(type);
  }

  private String buildGuideActionTypeParameter(TypeReference type) {
    if (type.isNamedReference()) {
      NamedReference namedType = type.asNamedReferenceOrElseThrow();
      if (namedType.extendedBounds().isEmpty()) {
        return "Object";
      }
    }
    return ObjectFunctions.getBaseObjectHandleTypename(type);
  }

  private String buildGuideGetterSupplier(MethodStatement domainMethod) {
    var sb = new StringBuilder();
    sb.append(context.addToImportAndGetSimpleName(Type.class));
    sb.append("<");
    sb.append(annotatedType.simpleName());
    sb.append(annotatedType.typeParametersFullDeclaration());
    sb.append("> sourceType = ").append(context.addToImportAndGetSimpleName(Types.class)).append(".of(");
    sb.append(annotatedType.simpleName());
    sb.append(".class);\n");
    sb.append("  return ");
    sb.append(context.addToImportAndGetSimpleName(Modules.class));
    sb.append(".currentModule().auto");
    sb.append(getTraverseType(domainMethod) == TraverseTypes.Mapping ? "Mapper" : "Mover");
    sb.append("ThruTransition");
    sb.append(domainMethod.params().size());
    sb.append("(sourceType, ");
    sb.append(context.addToImportAndGetSimpleName(NameConventionFunctions.getTransitionClassCanonicalName(domainMethod)));
    sb.append(".class);");
    return sb.toString();
  }

  private Class<?> getGuideClass(MethodStatement domainMethod) {
    int qualifierCount = domainMethod.params().size();
    if (TraverseTypes.Mapping == getTraverseType(domainMethod)) {
      return switch (qualifierCount) {
        case 0 -> Mapper0.class;
        case 1 -> Mapper1.class;
        case 2 -> Mapper2.class;
        case 3 -> Mapper3.class;
        case 4 -> Mapper4.class;
        case 5 -> Mapper5.class;
        default -> throw UnexpectedViolationException.withMessage("Unsupported number of mapper guide qualifies: {}",
            qualifierCount);
      };
    } else {
      return switch (qualifierCount) {
        case 0 -> Mover0.class;
        case 1 -> Mover1.class;
        case 2 -> Mover2.class;
        case 3 -> Mover3.class;
        case 4 -> Mover4.class;
        case 5 -> Mover5.class;
        default -> throw UnexpectedViolationException.withMessage("Unsupported number of mapper guide qualifies: {}",
            qualifierCount);
      };
    }
  }

  private static TraverseTypes getTraverseType(MethodStatement domainMethod) {
    return TransitionFunctions.getTraverseType(domainMethod);
  }

  protected String getGeneratedClassCanonicalName() {
    return NameConventionFunctions.getObjectHandleImplementationCanonicalName(annotatedType);
  }

  protected void analyzeTypeParams(CustomType objectHandleType) {
    if (objectHandleType.typeParameters().isEmpty()) {
      typeParamsFull = typeParamsBrief = "";
      return;
    }

    var typeParamsFullBuilder = new StringBuilder();
    var typeParamsBriefBuilder = new StringBuilder();
    Executor commaAppender = StringActions.commaAppender(typeParamsFullBuilder)
        .then(StringActions.commaAppender(typeParamsBriefBuilder));

    typeParamsFullBuilder.append("<");
    typeParamsBriefBuilder.append("<");
    for (NamedReference typeParam : objectHandleType.typeParameters()) {
      commaAppender.execute();
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
  protected Map<String, String> buildMethod(MethodStatement domainMethod) {
    if (isDisableMoving(domainMethod)) {
      return Map.of();
    }

    var sb = new StringBuilder();
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
    sb.append("  return ");
    sb.append(buildCastDeclaration(domainMethod.returnType().orElseThrow()));
    sb.append(buildGuideGetterName(domainMethod));
    sb.append(".get().traverse(this");
    for (MethodParam param : domainMethod.params()) {
      sb.append(", ");
      sb.append(param.name());
    }
    sb.append(");\n}");
    return Map.of("declaration", sb.toString());
  }

  private String buildCastDeclaration(TypeReference type) {
    if (type.isNamedReference()) {
      NamedReference namedType = type.asNamedReferenceOrElseThrow();
      if (namedType.owner().statementType() == StatementTypes.Method) {
        return "(" + namedType.name() + ") ";
      }
    }
    return "";
  }
}
