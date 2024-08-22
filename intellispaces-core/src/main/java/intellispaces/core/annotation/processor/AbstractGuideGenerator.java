package intellispaces.core.annotation.processor;

import intellispaces.actions.common.string.StringActions;
import intellispaces.actions.runner.Runner;
import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.commons.type.TypeFunctions;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.guide.GuideFunctions;
import intellispaces.core.guide.GuideKind;
import intellispaces.core.guide.n0.Mapper0;
import intellispaces.core.guide.n0.Mover0;
import intellispaces.core.guide.n1.Mapper1;
import intellispaces.core.guide.n1.Mover1;
import intellispaces.core.guide.n2.Mapper2;
import intellispaces.core.guide.n2.Mover2;
import intellispaces.core.guide.n3.Mapper3;
import intellispaces.core.guide.n3.Mover3;
import intellispaces.core.guide.n4.Mapper4;
import intellispaces.core.guide.n4.Mover4;
import intellispaces.core.guide.n5.Mapper5;
import intellispaces.core.guide.n5.Mover5;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.core.space.transition.TransitionFunctions;
import intellispaces.core.traverse.TraverseType;
import intellispaces.core.traverse.TraverseTypes;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodParam;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.CustomTypeReference;
import intellispaces.javastatements.reference.NamedReference;
import intellispaces.javastatements.reference.NotPrimitiveReference;
import intellispaces.javastatements.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGuideGenerator extends AbstractGenerator {
  protected final TraverseType traverseType;
  protected final MethodStatement transitionMethod;
  private String guideClassSimpleName;
  private String guideMethod;
  private String baseMethod;

  public AbstractGuideGenerator(
      TraverseType traverseType, CustomType annotatedType, MethodStatement transitionMethod
  ) {
    super(annotatedType);
    this.traverseType = traverseType;
    this.transitionMethod = transitionMethod;
  }

  protected abstract CustomType getDomainType();

  protected abstract List<MethodParam> getQualifierMethodParams();

  protected abstract String getGuideClassCanonicalName();

  @Override
  public String getArtifactName() {
    return getGuideClassCanonicalName();
  }

  @Override
  protected String templateName() {
    return "/guide.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
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
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
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
    String abstractGuideCanonicalName = TypeFunctions.addPrefixToClassName("Abstract", guideClass.getCanonicalName());
    guideClassSimpleName = context.addToImportAndGetSimpleName(abstractGuideCanonicalName);
    guideMethod = buildGuideMethod();
    baseMethod = buildBaseMethod(guideClass);
  }

  private Class<?> getGuideClass() {
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
    if (AnnotationProcessorFunctions.isVoidType(transitionMethod.returnType().orElseThrow())) {
      sb.append("void");
    } else {
      sb.append(getTargetObjectHandleDeclaration());
    }
    sb.append(" ");
    sb.append(transitionMethod.name());
    sb.append("(");
    sb.append(getSourceObjectHandleDeclaration());
    sb.append(" source");
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

    var sb = new StringBuilder();
    sb.append("default ");
    sb.append(getTargetObjectHandleRawSimpleName());
    sb.append(guideKind.isMapper() ? " map(" : " move(");
    sb.append(getSourceObjectHandleRawSimpleName());
    sb.append(" ").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(getObjectHandleRawSimpleName(param.type()));
      sb.append(" ");
      sb.append(param.name());
    }
    sb.append(") {\n  ");
    if (!AnnotationProcessorFunctions.isVoidType(transitionMethod.returnType().orElseThrow())) {
      sb.append("return ");
    }
    sb.append(transitionMethod.name());
    sb.append("(").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(param.name());
    }
    sb.append(");\n");
    if (AnnotationProcessorFunctions.isVoidType(transitionMethod.returnType().orElseThrow())) {
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
    String objectHandleCanonicalName = ObjectFunctions.getBaseObjectHandleTypename(type);
    return context.addToImportAndGetSimpleName(objectHandleCanonicalName);
  }

  private String getObjectHandleSimpleName(TypeReference type) {
    if (type.isNamedReference()) {
      return type.asNamedReferenceOrElseThrow().name();
    } else {
      String canonicalName = ObjectFunctions.getBaseObjectHandleTypename(type);
      return context.addToImportAndGetSimpleName(canonicalName);
    }
  }

  private String getObjectHandleRawSimpleName(TypeReference type) {
    if (type.isNamedReference()) {
      return "Object";
    } else {
      String canonicalName = ObjectFunctions.getBaseObjectHandleTypename(type);
      String simpleName = context.addToImportAndGetSimpleName(canonicalName);
      if (type.isCustomTypeReference() && !type.asCustomTypeReferenceOrElseThrow().typeArguments().isEmpty()) {
        var sb = new StringBuilder();
        sb.append(simpleName);
        sb.append("<");
        Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
        for (NotPrimitiveReference typeArg : type.asCustomTypeReferenceOrElseThrow().typeArguments()) {
          commaAppender.run();
          sb.append(getRawType(typeArg));
        }
        sb.append(">");
        return sb.toString();
      }
      return simpleName;
    }
  }

  private String getObjectHandleRawSimpleName(TypeReference type, boolean nested) {
    if (type.isWildcard() && nested) {
      return "?";
    } else if (type.isNamedReference()) {
      return nested ? "?" :"Object";
    } else {
      String canonicalName = ObjectFunctions.getBaseObjectHandleTypename(type);
      String simpleName = context.addToImportAndGetSimpleName(canonicalName);
      if (type.isCustomTypeReference() && !type.asCustomTypeReferenceOrElseThrow().typeArguments().isEmpty()) {
        var sb = new StringBuilder();
        sb.append(simpleName);
        sb.append("<");
        Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
        for (NotPrimitiveReference typeArg : type.asCustomTypeReferenceOrElseThrow().typeArguments()) {
          commaAppender.run();
          sb.append(typeArg.actualDeclaration());
        }
        sb.append(">");
        return sb.toString();
      }
      return simpleName;
    }
  }

  private String getRawType(TypeReference type) {
    if (type.isWildcard()) {
      return "?";
    } else if (type.isNamedReference()) {
      return "?";
    } else if (type.isCustomTypeReference()) {
      CustomTypeReference customTypeReference = type.asCustomTypeReferenceOrElseThrow();
      String simpleName = context.addToImportAndGetSimpleName(customTypeReference.targetType().canonicalName());
      if (customTypeReference.typeArguments().isEmpty()) {
        return simpleName;
      }
      var sb = new StringBuilder();
      sb.append(simpleName);
      sb.append("<");
      Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
      for (NotPrimitiveReference typeArg : type.asCustomTypeReferenceOrElseThrow().typeArguments()) {
        commaAppender.run();
        sb.append(getRawType(typeArg));
      }
      sb.append(">");
      return sb.toString();
    } else {
      throw UnexpectedViolationException.withMessage("Unsupported type reference");
    }
  }

  private String getObjectHandleRawSimpleName(CustomType type) {
    String canonicalName = ObjectFunctions.getBaseObjectHandleTypename(type);
    String simpleName = context.addToImportAndGetSimpleName(canonicalName);
    if (!type.typeParameters().isEmpty()) {
      var sb = new StringBuilder();
      sb.append(simpleName);
      sb.append("<");
      Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
      for (NotPrimitiveReference typeParam : type.typeParameters()) {
        commaAppender.run();
        sb.append(getObjectHandleRawSimpleName(typeParam, true));
      }
      sb.append(">");
      return sb.toString();
    }
    return simpleName;
  }

  private String getAssociatedTypesDeclaration(TypeReference type) {
    if (type.isPrimitiveReference() || type.isNamedReference()) {
      return "";
    } else if (type.isCustomTypeReference()) {
      return type.asCustomTypeReferenceOrElseThrow().typeArgumentsDeclaration(context::addToImportAndGetSimpleName);
    } else {
      throw UnexpectedViolationException.withMessage("Not supported type");
    }
  }

  private String getAssociatedTypesDeclaration(CustomType type) {
    return type.typeParametersBriefDeclaration();
  }

  private String transitionClassSimpleName() {
    String canonicalName = NameConventionFunctions.getTransitionClassCanonicalName(
        annotatedType.packageName(), getDomainType(), transitionMethod
    );
    return context.addToImportAndGetSimpleName(canonicalName);
  }

  private String getGuideMethodTypeParams() {
    if (getDomainType().typeParameters().isEmpty() && transitionMethod.typeParameters().isEmpty()) {
      return "";
    }
    var sb = new StringBuilder();
    Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
    sb.append("<");
    for (NamedReference typeParam : getDomainType().typeParameters()) {
      commaAppender.run();
      sb.append(typeParam.formalFullDeclaration());
    }
    for (NamedReference typeParam : transitionMethod.typeParameters()) {
      commaAppender.run();
      sb.append(typeParam.formalFullDeclaration());
    }
    sb.append(">");
    return sb.toString();
  }
}
