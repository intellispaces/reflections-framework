package intellispaces.framework.core.annotation.processor;

import intellispaces.common.action.runner.Runner;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.math.MathFunctions;
import intellispaces.common.base.text.TextActions;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.NotPrimitiveReference;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.Guide;
import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.MapperOfMoving;
import intellispaces.framework.core.annotation.Mover;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.GuideForms;
import intellispaces.framework.core.guide.n0.Mapper0;
import intellispaces.framework.core.guide.n0.MapperOfMoving0;
import intellispaces.framework.core.guide.n0.Mover0;
import intellispaces.framework.core.guide.n1.Mapper1;
import intellispaces.framework.core.guide.n1.MapperOfMoving1;
import intellispaces.framework.core.guide.n1.Mover1;
import intellispaces.framework.core.guide.n2.Mapper2;
import intellispaces.framework.core.guide.n2.MapperOfMoving2;
import intellispaces.framework.core.guide.n2.Mover2;
import intellispaces.framework.core.guide.n3.Mapper3;
import intellispaces.framework.core.guide.n3.MapperOfMoving3;
import intellispaces.framework.core.guide.n3.Mover3;
import intellispaces.framework.core.guide.n4.Mapper4;
import intellispaces.framework.core.guide.n4.MapperOfMoving4;
import intellispaces.framework.core.guide.n4.Mover4;
import intellispaces.framework.core.guide.n5.Mapper5;
import intellispaces.framework.core.guide.n5.MapperOfMoving5;
import intellispaces.framework.core.guide.n5.Mover5;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.space.channel.ChannelFunctions;
import intellispaces.framework.core.traverse.TraverseType;
import intellispaces.framework.core.traverse.TraverseTypes;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public abstract class AbstractGuideGenerator extends AbstractGenerator {
  protected final GuideForm guideForm;
  protected final TraverseType traverseType;
  protected final MethodStatement channelMethod;
  private String guideClassSimpleName;
  private String guideAnnotation;
  private String guideMethod;
  private String baseMethod;
  private String longPrimitiveMethod;
  private String doublePrimitiveMethod;
  private String guideTypeParamsFull;

  public AbstractGuideGenerator(
      GuideForm guideForm,
      TraverseType traverseType,
      CustomType initiatorType,
      CustomType annotatedType,
      MethodStatement channelMethod
  ) {
    super(initiatorType, annotatedType);
    this.guideForm = guideForm;
    this.traverseType = traverseType;
    this.channelMethod = channelMethod;
  }

  protected abstract CustomType getDomainType();

  abstract protected String getGuideTypeParamDeclaration();

  protected abstract List<MethodParam> getQualifierMethodParams();

  protected abstract String getGuideClassCanonicalName();

  @Override
  public String artifactName() {
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
    vars.put("channelClassSimpleName", channelClassSimpleName());
    vars.put("guideTypeParamsFull", guideTypeParamsFull);
    vars.put("guideClassSimpleName", guideClassSimpleName);
    vars.put("guideTypeParams", buildGuideTypeParams(Function.identity()));
    vars.put("guideAnnotation", guideAnnotation);
    vars.put("guideMethod", guideMethod);
    vars.put("baseMethod", baseMethod);
    vars.put("guideForm", guideForm.name());
    vars.put("longPrimitiveMethod", longPrimitiveMethod);
    vars.put("doublePrimitiveMethod", doublePrimitiveMethod);
    vars.put("importedClasses", context.getImports());
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());
    if (annotatedType.isNested()) {
      context.addImport(annotatedType.canonicalName());
    }
    context.addImport(Guide.class);
    context.addImport(ChannelFunctions.class);
    context.addImport(Objects.class);
    context.addImport(GuideForm.class);
    context.addImport(GuideForms.class);
    context.addImport(UnexpectedViolationException.class);
    context.addImport(MathFunctions.class);
    analyzeGuideType();
    return true;
  }

  protected String buildGuideMethod() {
    return buildGuideMethod(Function.identity());
  }

  protected String buildGuideMethod(Function<TypeReference, TypeReference> typeReplacer) {
    var sb = new StringBuilder();
    sb.append(buildTargetObjectHandleFormDeclaration(typeReplacer));
    sb.append(" ");
    sb.append(channelMethod.name());
    sb.append("(");
    sb.append(buildSourceObjectHandleDeclaration(typeReplacer));
    sb.append(" source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      if (param.type().isPrimitiveReference()) {
        sb.append(param.type().asPrimitiveReferenceOrElseThrow().typename());
      } else {
        sb.append(buildObjectHandleDeclaration(param.type(), typeReplacer));
      }
      sb.append(" ");
      sb.append(param.name());
    }
    sb.append(");");
    return sb.toString();
  }

  private void analyzeGuideType() {
    Class<?> guideClass = getGuideClass();
    guideClassSimpleName = getGuideClassSimpleName(guideClass);
    guideTypeParamsFull = getGuideTypeParamDeclaration();
    baseMethod = buildBaseMethod();
    longPrimitiveMethod = buildLongPrimitiveMethod();
    doublePrimitiveMethod = buildDoublePrimitiveMethod();
    analyzeGuideMethod();
  }

  private String getGuideClassSimpleName(Class<?> guideClass) {
    String abstractGuideCanonicalName = TypeFunctions.addPrefixToSimpleName(
        "Abstract", guideClass.getCanonicalName()
    );
    return context.addToImportAndGetSimpleName(abstractGuideCanonicalName);
  }

  private Class<?> getGuideClass() {
    int qualifierCount = getQualifierMethodParams().size();
    if (traverseType == TraverseTypes.Mapping) {
      return switch (qualifierCount) {
        case 0 -> Mapper0.class;
        case 1 -> Mapper1.class;
        case 2 -> Mapper2.class;
        case 3 -> Mapper3.class;
        case 4 -> Mapper4.class;
        case 5 -> Mapper5.class;
        default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifies: {0}",
            qualifierCount);
      };
    } else if (traverseType == TraverseTypes.Moving) {
      return switch (qualifierCount) {
        case 0 -> Mover0.class;
        case 1 -> Mover1.class;
        case 2 -> Mover2.class;
        case 3 -> Mover3.class;
        case 4 -> Mover4.class;
        case 5 -> Mover5.class;
        default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifies: {0}",
          qualifierCount);
      };
    } else if (traverseType == TraverseTypes.MappingOfMoving) {
      return switch (qualifierCount) {
        case 0 -> MapperOfMoving0.class;
        case 1 -> MapperOfMoving1.class;
        case 2 -> MapperOfMoving2.class;
        case 3 -> MapperOfMoving3.class;
        case 4 -> MapperOfMoving4.class;
        case 5 -> MapperOfMoving5.class;
        default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifies: {0}",
            qualifierCount);
      };
    } else {
      throw UnexpectedViolationException.withMessage("Unsupported traverse type: {0}", traverseType);
    }
  }

  protected String buildGuideTypeParams(Function<TypeReference, TypeReference> typeReplacer) {
    var sb = new StringBuilder();
    sb.append("<");
    sb.append(buildSourceObjectHandleDeclaration(typeReplacer));
    if (traverseType == TraverseTypes.Mapping || traverseType == TraverseTypes.MappingOfMoving) {
      sb.append(", ");
      sb.append(buildTargetObjectHandleDeclaration(typeReplacer));
    }
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(buildObjectHandleDeclaration(param.type(), typeReplacer));
    }
    sb.append(">");
    return sb.toString();
  }

  private void analyzeGuideMethod() {
    if (traverseType == TraverseTypes.Mapping) {
      context.addImport(Mapper.class);
      guideAnnotation = "@" + Mapper.class.getSimpleName();
    } else if (traverseType == TraverseTypes.Moving) {
      context.addImport(Mover.class);
      guideAnnotation = "@" + Mover.class.getSimpleName();
    } else if (traverseType == TraverseTypes.MappingOfMoving) {
      context.addImport(MapperOfMoving.class);
      guideAnnotation = "@" + MapperOfMoving.class.getSimpleName();
    }

    guideMethod = buildGuideMethod();
  }

  private String buildBaseMethod() {
    var sb = new StringBuilder();
    sb.append("default ");
    sb.append(buildTargetObjectHandleDeclaration(Function.identity()));
    sb.append(" traverse(");
    sb.append(buildSourceObjectHandleDeclaration(Function.identity()));
    sb.append(" ").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(buildObjectHandleDeclaration(param.type(), Function.identity()));
      sb.append(" ");
      sb.append(param.name());
    }
    sb.append(") {\n");
    sb.append("  Objects.requireNonNull(source);\n");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append("  Objects.requireNonNull(").append(param.name()).append(");\n");
    }
    if (!AnnotationProcessorFunctions.isVoidType(channelMethod.returnType().orElseThrow())) {
      sb.append("  return ");
    }
    sb.append(channelMethod.name());
    sb.append("(").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(param.name());
    }
    sb.append(");\n");
    if (AnnotationProcessorFunctions.isVoidType(channelMethod.returnType().orElseThrow())) {
      sb.append("  return null;\n");
    }
    sb.append("}");
    return sb.toString();
  }

  private String buildLongPrimitiveMethod() {
    var sb = new StringBuilder();
    sb.append("default int traverseToInt(");
    sb.append(buildSourceObjectHandleDeclaration(Function.identity()));
    sb.append(" ").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(buildObjectHandleDeclaration(param.type(), Function.identity()));
      sb.append(" ");
      sb.append(param.name());
    }
    sb.append(") {\n");
    buildPrimitiveMethodBody(sb, true);
    sb.append("}");
    return sb.toString();
  }

  private String buildDoublePrimitiveMethod() {
    var sb = new StringBuilder();
    sb.append("default double traverseToDouble(");
    sb.append(buildSourceObjectHandleDeclaration(Function.identity()));
    sb.append(" ").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(buildObjectHandleDeclaration(param.type(), Function.identity()));
      sb.append(" ");
      sb.append(param.name());
    }
    sb.append(") {\n");
    buildPrimitiveMethodBody(sb, false);
    sb.append("}");
    return sb.toString();
  }

  private void buildPrimitiveMethodBody(StringBuilder sb, boolean longForm) {
    TypeReference returnType = channelMethod.returnType().orElseThrow();
    if (returnType.isNamedReference()
        || !TypeFunctions.isPrimitiveWrapperClass(returnType.asCustomTypeReferenceOrElseThrow().targetType().canonicalName())
        || (TypeFunctions.isDoubleClass(returnType.asCustomTypeReferenceOrElseThrow().targetType().canonicalName()) && longForm)
    ) {
      sb.append("  throw UnexpectedViolationException.withMessage(\"Invalid operation\");\n");
    } else {
      sb.append("  Objects.requireNonNull(source);\n");
      for (MethodParam param : getQualifierMethodParams()) {
        sb.append("  Objects.requireNonNull(").append(param.name()).append(");\n");
      }
      sb.append("  return ");

      String returnTypeCanonicalName = returnType.asCustomTypeReferenceOrElseThrow().targetType().canonicalName();
      boolean isBooleanReturnType = TypeFunctions.isBooleanClass(returnTypeCanonicalName);
      if (isBooleanReturnType) {
        if (longForm) {
          sb.append("MathFunctions.booleanToInt(");
        } else {
          sb.append("MathFunctions.booleanToDouble(");
        }
      }
      sb.append(channelMethod.name());
      sb.append("(").append("source");
      for (MethodParam param : getQualifierMethodParams()) {
        sb.append(", ");
        sb.append(param.name());
      }
      if (isBooleanReturnType) {
        sb.append(")");
      }
      sb.append(");\n");
    }
  }

  protected String buildSourceObjectHandleDeclaration(
      Function<TypeReference, TypeReference> typeReplacer
  ) {
    return buildObjectHandleDeclaration(getDomainType());
  }

  private String buildTargetObjectHandleFormDeclaration(Function<TypeReference, TypeReference> typeReplacer) {
    if (guideForm == GuideForms.Main) {
      return buildTargetObjectHandleDeclaration(typeReplacer);
    } else if (guideForm == GuideForms.Primitive) {
      return TypeFunctions.getPrimitiveTypeOfWrapper(
          channelMethod.returnType().orElseThrow()
              .asCustomTypeReferenceOrElseThrow()
              .targetType()
              .canonicalName());
    }
    throw UnexpectedViolationException.withMessage("Not supported guide form - {0}", guideForm.name());
  }

  protected String buildTargetObjectHandleDeclaration(Function<TypeReference, TypeReference> typeReplacer) {
    TypeReference returnType = channelMethod.returnType().orElseThrow();
    return buildObjectHandleDeclaration(returnType, typeReplacer);
  }

  protected String buildObjectHandleDeclaration(
      TypeReference type, Function<TypeReference, TypeReference> typeReplacer
  ) {
    type = typeReplacer.apply(type);
    if (type.isNamedReference()) {
      return type.asNamedReferenceOrElseThrow().name();
    } else if (type.isPrimitiveReference()) {
      return TypeFunctions.getPrimitiveWrapperClass(type.asPrimitiveReferenceOrElseThrow().typename()).getSimpleName();
    } else {
      String canonicalName = ObjectFunctions.getCommonObjectHandleTypename(type, typeReplacer);
      String name = type.isCustomTypeReference() ? context.addToImportAndGetSimpleName(canonicalName) : canonicalName;
      if (type.isCustomTypeReference() && !type.asCustomTypeReferenceOrElseThrow().typeArguments().isEmpty()) {
        var sb = new StringBuilder();
        sb.append(name);
        sb.append("<");
        Runner commaAppender = TextActions.skippingFirstTimeCommaAppender(sb);
        for (NotPrimitiveReference typeArg : type.asCustomTypeReferenceOrElseThrow().typeArguments()) {
          commaAppender.run();
          sb.append(buildObjectHandleDeclaration(typeArg, typeReplacer));
        }
        sb.append(">");
        return sb.toString();
      }
      return name;
    }
  }

  private String buildObjectHandleDeclaration(TypeReference type, boolean nested) {
    if (type.isWildcard() && nested) {
      return "?";
    } else if (type.isNamedReference()) {
      return type.asNamedReferenceOrElseThrow().name();
    } else {
      String canonicalName = ObjectFunctions.getCommonObjectHandleTypename(type);
      String simpleName = context.addToImportAndGetSimpleName(canonicalName);
      if (type.isCustomTypeReference() && !type.asCustomTypeReferenceOrElseThrow().typeArguments().isEmpty()) {
        var sb = new StringBuilder();
        sb.append(simpleName);
        sb.append("<");
        Runner commaAppender = TextActions.skippingFirstTimeCommaAppender(sb);
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

  private String buildObjectHandleDeclaration(CustomType type) {
    String canonicalName = ObjectFunctions.getCommonObjectHandleTypename(type);
    String simpleName = context.addToImportAndGetSimpleName(canonicalName);
    if (!type.typeParameters().isEmpty()) {
      var sb = new StringBuilder();
      sb.append(simpleName);
      sb.append("<");
      Runner commaAppender = TextActions.skippingFirstTimeCommaAppender(sb);
      for (NotPrimitiveReference typeParam : type.typeParameters()) {
        commaAppender.run();
        sb.append(buildObjectHandleDeclaration(typeParam, true));
      }
      sb.append(">");
      return sb.toString();
    }
    return simpleName;
  }

  private String channelClassSimpleName() {
    String canonicalName = NameConventionFunctions.getChannelClassCanonicalName(
        annotatedType.packageName(), getDomainType(), channelMethod
    );
    return context.addToImportAndGetSimpleName(canonicalName);
  }
}
