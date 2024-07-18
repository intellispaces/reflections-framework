package tech.intellispaces.framework.core.annotation.processor;

import tech.intellispaces.framework.commons.action.Executor;
import tech.intellispaces.framework.commons.action.string.StringActions;
import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.commons.string.StringFunctions;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.guide.GuideFunctions;
import tech.intellispaces.framework.core.guide.GuideKind;
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
import tech.intellispaces.framework.core.space.transition.TransitionFunctions;
import tech.intellispaces.framework.core.traverse.TraverseTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.CustomTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.NamedTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.NonPrimitiveTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGuideGenerator extends AbstractGenerator {
  private final MethodStatement transitionMethod;
  private String guideClassSimpleName;
  private String guideMethod;
  private String baseMethod;

  public AbstractGuideGenerator(CustomType annotatedType, MethodStatement transitionMethod) {
    super(annotatedType);
    this.transitionMethod = transitionMethod;
  }

  protected abstract CustomType getDomainType();

  protected abstract List<MethodParam> getQualifierMethodParams();

  protected abstract String getGuideClassCanonicalName();

  @Override
  protected String templateName() {
    return "/guide.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", generatedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("transitionClassSimpleName", transitionClassSimpleName());
    vars.put("guideClassSimpleName", guideClassSimpleName);
    vars.put("guideTypeParams", buildGuideTypeParams());
    vars.put("guideMethod", guideMethod);
    vars.put("baseMethod", baseMethod);
    vars.put("importedClasses", context.getImports());
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType() {
    context.generatedClassCanonicalName(getGuideClassCanonicalName());
    if (annotatedType.isNested()) {
      context.addImport(annotatedType.canonicalName());
    }
    context.addImport(TransitionFunctions.class);

    analyzeGuideType();
    return true;
  }

  private void analyzeGuideType() {
    Class<?> guideClass = getGuideClass();
    String abstractGuideCanonicalName = TypeFunctions.addPrefixToClassName("Basic", guideClass.getCanonicalName());
    guideClassSimpleName = context.addToImportAndGetSimpleName(abstractGuideCanonicalName);
    guideMethod = buildGuideMethod();
    baseMethod = buildBaseMethod(guideClass);
  }

  private Class<?> getGuideClass() {
    TraverseTypes traverseType = getTraverseType();
    int qualifierCount = getQualifierMethodParams().size();
    if (TraverseTypes.Mapping == traverseType) {
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

  private String buildGuideTypeParams() {
    var sb = new StringBuilder();
    sb.append("<");
    sb.append(getSourceObjectHandleRawSimpleName());
    sb.append(", ");
    sb.append(getTargetObjectHandleRawSimpleName());
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(getObjectHandleRawSimpleName(param.type()));
    }
    sb.append(">");
    return sb.toString();
  }

  private String buildGuideMethod() {
    var sb = new StringBuilder();
    String typeParams = getGuideMethodTypeParams();
    if (!typeParams.isEmpty()) {
      sb.append(typeParams);
      sb.append(" ");
    }
    if (ProcessorFunctions.isVoidType(transitionMethod.returnType().orElseThrow())) {
      sb.append("void");
    } else {
      sb.append(getTargetObjectHandleDeclaration());
    }
    sb.append(" ");
    sb.append(transitionMethod.name());
    sb.append("(");
    sb.append(getSourceObjectHandleDeclaration());
    sb.append(" ").append(StringFunctions.lowercaseFirstLetter(getDomainType().simpleName()));
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(getObjectHandleSimpleName(param.type())).append(getAssociatedTypesDeclaration(param.type()));
      sb.append(" ");
      sb.append(param.name());
    }
    sb.append(");");
    return sb.toString();
  }

  private String buildBaseMethod(Class<?> guideClass) {
    GuideKind guideKind = GuideFunctions.getGuideKind(guideClass);
    String sourceParamName = StringFunctions.lowercaseFirstLetter(getDomainType().simpleName());

    var sb = new StringBuilder();
    sb.append("default ");
    sb.append(getTargetObjectHandleRawSimpleName());
    sb.append(guideKind.isMapper() ? " map(" : " move(");
    sb.append(getSourceObjectHandleRawSimpleName());
    sb.append(" ").append(sourceParamName);
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(getObjectHandleRawSimpleName(param.type()));
      sb.append(" ");
      sb.append(param.name());
    }
    sb.append(") {\n  ");
    if (!ProcessorFunctions.isVoidType(transitionMethod.returnType().orElseThrow())) {
      sb.append("return ");
    }
    sb.append(transitionMethod.name());
    sb.append("(").append(sourceParamName);
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(param.name());
    }
    sb.append(");\n");
    if (ProcessorFunctions.isVoidType(transitionMethod.returnType().orElseThrow())) {
      sb.append("  return null;\n");
    }
    sb.append("}");
    return sb.toString();
  }

  private String getSourceObjectHandleDeclaration() {
    return getObjectHandleSimpleName(getDomainType()) + getAssociatedTypesDeclaration(getDomainType());
  }

  private String getTargetObjectHandleDeclaration() {
    TypeReference returnType = transitionMethod.returnType().orElseThrow();
    return getObjectHandleSimpleName(returnType) + getAssociatedTypesDeclaration(returnType);
  }

  private String getSourceObjectHandleRawSimpleName() {
    return getObjectHandleRawSimpleName(getDomainType());
  }

  private String getTargetObjectHandleRawSimpleName() {
    TypeReference returnType = transitionMethod.returnType().orElseThrow();
    return getObjectHandleRawSimpleName(returnType);
  }

  private String getObjectHandleSimpleName(CustomType type) {
    String objectHandleCanonicalName = ObjectFunctions.getObjectHandleTypename(type);
    return context.addToImportAndGetSimpleName(objectHandleCanonicalName);
  }

  private String getObjectHandleSimpleName(TypeReference type) {
    if (type.isNamedTypeReference()) {
      return type.asNamedTypeReferenceSurely().name();
    } else {
      String canonicalName = ObjectFunctions.getObjectHandleTypename(type);
      return context.addToImportAndGetSimpleName(canonicalName);
    }
  }

  private String getObjectHandleRawSimpleName(TypeReference type) {
    if (type.isNamedTypeReference()) {
      return "Object";
    } else {
      String canonicalName = ObjectFunctions.getObjectHandleTypename(type);
      String simpleName = context.addToImportAndGetSimpleName(canonicalName);
      if (type.isCustomTypeReference() && !type.asCustomTypeReferenceSurely().typeArguments().isEmpty()) {
        var sb = new StringBuilder();
        sb.append(simpleName);
        sb.append("<");
        Executor commaAppender = StringActions.commaAppender(sb);
        for (NonPrimitiveTypeReference typeArg : type.asCustomTypeReferenceSurely().typeArguments()) {
          commaAppender.execute();
          sb.append(getRawType(typeArg));
        }
        sb.append(">");
        return sb.toString();
      }
      return simpleName;
    }
  }

  private String getObjectHandleRawSimpleName(TypeReference type, boolean nested) {
    if (type.isWildcardTypeReference() && nested) {
      return "?";
    } else if (type.isNamedTypeReference()) {
      return nested ? "?" :"Object";
    } else {
      String canonicalName = ObjectFunctions.getObjectHandleTypename(type);
      String simpleName = context.addToImportAndGetSimpleName(canonicalName);
      if (type.isCustomTypeReference() && !type.asCustomTypeReferenceSurely().typeArguments().isEmpty()) {
        var sb = new StringBuilder();
        sb.append(simpleName);
        sb.append("<");
        Executor commaAppender = StringActions.commaAppender(sb);
        for (NonPrimitiveTypeReference typeArg : type.asCustomTypeReferenceSurely().typeArguments()) {
          commaAppender.execute();
          sb.append(typeArg.actualDeclaration());
        }
        sb.append(">");
        return sb.toString();
      }
      return simpleName;
    }
  }

  private String getRawType(TypeReference type) {
    if (type.isWildcardTypeReference()) {
      return "?";
    } else if (type.isNamedTypeReference()) {
      return "?";
    } else if (type.isCustomTypeReference()) {
      CustomTypeReference customTypeReference = type.asCustomTypeReferenceSurely();
      String simpleName = context.addToImportAndGetSimpleName(customTypeReference.targetType().canonicalName());
      if (customTypeReference.typeArguments().isEmpty()) {
        return simpleName;
      }
      var sb = new StringBuilder();
      sb.append(simpleName);
      sb.append("<");
      Executor commaAppender = StringActions.commaAppender(sb);
      for (NonPrimitiveTypeReference typeArg : type.asCustomTypeReferenceSurely().typeArguments()) {
        commaAppender.execute();
        sb.append(getRawType(typeArg));
      }
      sb.append(">");
      return sb.toString();
    } else {
      throw UnexpectedViolationException.withMessage("Unsupported type reference");
    }
  }

  private String getObjectHandleRawSimpleName(CustomType type) {
    String canonicalName = ObjectFunctions.getObjectHandleTypename(type);
    String simpleName = context.addToImportAndGetSimpleName(canonicalName);
    if (!type.typeParameters().isEmpty()) {
      var sb = new StringBuilder();
      sb.append(simpleName);
      sb.append("<");
      Executor commaAppender = StringActions.commaAppender(sb);
      for (NonPrimitiveTypeReference typeParam : type.typeParameters()) {
        commaAppender.execute();
        sb.append(getObjectHandleRawSimpleName(typeParam, true));
      }
      sb.append(">");
      return sb.toString();
    }
    return simpleName;
  }

  private String getAssociatedTypesDeclaration(TypeReference type) {
    if (type.isPrimitive() || type.isNamedTypeReference()) {
      return "";
    } else if (type.isCustomTypeReference()) {
      return type.asCustomTypeReferenceSurely().typeArgumentsDeclaration(context::addToImportAndGetSimpleName);
    } else {
      throw UnexpectedViolationException.withMessage("Not supported type");
    }
  }

  private String getAssociatedTypesDeclaration(CustomType type) {
    return type.typeParametersBriefDeclaration();
  }

  private TraverseTypes getTraverseType() {
    return transitionMethod.selectAnnotation(Transition.class).orElseThrow().type();
  }

  private String transitionClassSimpleName() {
    String canonicalName = NameFunctions.getTransitionClassCanonicalName(
        annotatedType.packageName(), getDomainType(), transitionMethod
    );
    return context.addToImportAndGetSimpleName(canonicalName);
  }

  private String getGuideMethodTypeParams() {
    if (getDomainType().typeParameters().isEmpty() && transitionMethod.typeParameters().isEmpty()) {
      return "";
    }
    var sb = new StringBuilder();
    Executor commaAppender = StringActions.commaAppender(sb);
    sb.append("<");
    for (NamedTypeReference typeParam : getDomainType().typeParameters()) {
      commaAppender.execute();
      sb.append(typeParam.formalFullDeclaration());
    }
    for (NamedTypeReference typeParam : transitionMethod.typeParameters()) {
      commaAppender.execute();
      sb.append(typeParam.formalFullDeclaration());
    }
    sb.append(">");
    return sb.toString();
  }
}
