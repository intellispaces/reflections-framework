package tech.intellispaces.framework.core.annotation.processor.objecthandle;

import tech.intellispaces.framework.commons.action.ActionBuilders;
import tech.intellispaces.framework.commons.action.Executor;
import tech.intellispaces.framework.commons.action.Getter;
import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.commons.type.Type;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.annotation.processor.AbstractObjectHandleGenerator;
import tech.intellispaces.framework.core.common.Actions;
import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.guide.n0.Mapper0;
import tech.intellispaces.framework.core.guide.n0.Mover0;
import tech.intellispaces.framework.core.guide.n1.Mapper1;
import tech.intellispaces.framework.core.guide.n1.Mover1;
import tech.intellispaces.framework.core.guide.n2.Mapper2;
import tech.intellispaces.framework.core.guide.n2.Mover2;
import tech.intellispaces.framework.core.guide.n3.Mapper3;
import tech.intellispaces.framework.core.guide.n3.Mover3;
import tech.intellispaces.framework.core.guide.n4.Mapper4;
import tech.intellispaces.framework.core.guide.n4.Mover4;
import tech.intellispaces.framework.core.guide.n5.Mapper5;
import tech.intellispaces.framework.core.guide.n5.Mover5;
import tech.intellispaces.framework.core.object.ObjectFunctions;
import tech.intellispaces.framework.core.system.Modules;
import tech.intellispaces.framework.core.traverse.TraverseTypes;
import tech.intellispaces.framework.javastatements.statement.StatementTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.NamedTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

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
  protected List<String> guideImplementationMethods;

  AbstractObjectHandleImplGenerator(CustomType objectHandleType) {
    super(objectHandleType);
  }

  @Override
  protected Stream<MethodStatement> getDomainMethods(CustomType objectHandleType) {
    CustomType domainType = ObjectFunctions.getDomainTypeOfObjectHandle(objectHandleType);
    return domainType.actualMethods().stream()
        .filter(m -> !m.isDefault());
  }

  protected void analyzeGuideGetters(CustomType objectHandleType) {
    this.guideGetters = getDomainMethods(objectHandleType)
        .map(this::buildGuideGetter)
        .toList();
  }

  protected void analyzeGuideImplementationMethods(CustomType objectHandleType) {
    this.guideImplementationMethods = getDomainMethods(objectHandleType)
        .map(this::buildGuideMethod)
        .toList();
  }

  private String buildGuideMethod(MethodStatement domainMethod) {
    var sb = new StringBuilder();
    sb.append("public ");
    sb.append(buildMethodSignature(domainMethod, "_" + domainMethod.name()));
    sb.append(" {\n");
    sb.append("  return super.");
    sb.append(domainMethod.name());
    sb.append("(");
    Executor commaAppender = Actions.buildCommaAppender(sb);
    for (MethodParam param : domainMethod.params()) {
      commaAppender.execute();
      sb.append(param.name());
    }
    sb.append(");\n}");
    return sb.toString();
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
    sb.append(context.addToImportAndGetSimpleName(ActionBuilders.class));
    sb.append(".cachedLazyGetter(() -> {\n  ");
    sb.append(buildGuideGetterSupplier(domainMethod));
    sb.append("\n});");
    return sb.toString();
  }

  private String buildGuideGetterName(MethodStatement domainMethod) {
    var sb = new StringBuilder();
    sb.append("_");
    sb.append(NameFunctions.joinMethodNameAndParameterTypes(domainMethod));
    sb.append("GuideGetter");
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
    if (type.isNamedTypeReference()) {
      NamedTypeReference namedType = type.asNamedTypeReferenceSurely();
      if (namedType.owner().statementType() == StatementTypes.Method) {
        if (namedType.extendedBounds().isEmpty()) {
          return "Object";
        }
      }
    }
    return ObjectFunctions.getObjectHandleTypename(type);
  }

  private String buildGuideGetterSupplier(MethodStatement domainMethod) {
    var sb = new StringBuilder();
    sb.append(context.addToImportAndGetSimpleName(Type.class));
    sb.append("<");
    sb.append(annotatedType.simpleName());
    sb.append(annotatedType.typeParametersFullDeclaration());
    sb.append("> sourceType = ").append(context.addToImportAndGetSimpleName(TypeFunctions.class)).append(".typeOf(");
    sb.append(annotatedType.simpleName());
    sb.append(".class);\n");
    sb.append("  return ");
    sb.append(context.addToImportAndGetSimpleName(Modules.class));
    sb.append(".activeModule().auto");
    sb.append(getTraverseType(domainMethod) == TraverseTypes.Mapping ? "Mapper" : "Mover");
    sb.append("ThruTransition");
    sb.append(domainMethod.params().size());
    sb.append("(");
    sb.append("sourceType, ");
    sb.append(context.addToImportAndGetSimpleName(NameFunctions.getTransitionClassCanonicalName(domainMethod)));
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
    return domainMethod.selectAnnotation(Transition.class).orElseThrow().type();
  }

  protected String getGeneratedClassCanonicalName() {
    return NameFunctions.getObjectHandleImplementationTypename(annotatedType);
  }

  protected void analyzeTypeParams(CustomType objectHandleType) {
    if (objectHandleType.typeParameters().isEmpty()) {
      typeParamsFull = typeParamsBrief = "";
      return;
    }

    var typeParamsFullBuilder = new StringBuilder();
    var typeParamsBriefBuilder = new StringBuilder();
    Executor commaAppender = Actions.buildCommaAppender(typeParamsFullBuilder)
        .then(Actions.buildCommaAppender(typeParamsBriefBuilder));

    typeParamsFullBuilder.append("<");
    typeParamsBriefBuilder.append("<");
    for (NamedTypeReference typeParam : objectHandleType.typeParameters()) {
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
  protected String buildMethod(MethodStatement domainMethod) {
    if (isDisableMoving(domainMethod)) {
      return "";
    }

    var sb = new StringBuilder();
    sb.append("public ");
    appendMethodTypeParameters(sb, domainMethod);
    appendMethodReturnType(sb, domainMethod);
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
    return sb.toString();
  }

  private String buildCastDeclaration(TypeReference type) {
    if (type.isNamedTypeReference()) {
      NamedTypeReference namedType = type.asNamedTypeReferenceSurely();
      if (namedType.owner().statementType() == StatementTypes.Method) {
        return "(" + namedType.name() + ") ";
      }
    }
    return "";
  }
}
