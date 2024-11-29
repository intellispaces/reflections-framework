package tech.intellispaces.jaquarius.annotation.processor.channel;

import tech.intellispaces.jaquarius.annotation.Guide;
import tech.intellispaces.jaquarius.annotation.Mapper;
import tech.intellispaces.jaquarius.annotation.MapperOfMoving;
import tech.intellispaces.jaquarius.annotation.Mover;
import tech.intellispaces.jaquarius.annotation.processor.AbstractGenerator;
import tech.intellispaces.jaquarius.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.jaquarius.common.NameConventionFunctions;
import tech.intellispaces.jaquarius.guide.n0.Mapper0;
import tech.intellispaces.jaquarius.guide.n0.MapperOfMoving0;
import tech.intellispaces.jaquarius.guide.n0.Mover0;
import tech.intellispaces.jaquarius.guide.n1.Mapper1;
import tech.intellispaces.jaquarius.guide.n1.MapperOfMoving1;
import tech.intellispaces.jaquarius.guide.n1.Mover1;
import tech.intellispaces.jaquarius.guide.n2.Mapper2;
import tech.intellispaces.jaquarius.guide.n2.MapperOfMoving2;
import tech.intellispaces.jaquarius.guide.n2.Mover2;
import tech.intellispaces.jaquarius.guide.n3.Mapper3;
import tech.intellispaces.jaquarius.guide.n3.MapperOfMoving3;
import tech.intellispaces.jaquarius.guide.n3.Mover3;
import tech.intellispaces.jaquarius.guide.n4.Mapper4;
import tech.intellispaces.jaquarius.guide.n4.MapperOfMoving4;
import tech.intellispaces.jaquarius.guide.n4.Mover4;
import tech.intellispaces.jaquarius.guide.n5.Mapper5;
import tech.intellispaces.jaquarius.guide.n5.MapperOfMoving5;
import tech.intellispaces.jaquarius.guide.n5.Mover5;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseType;
import tech.intellispaces.jaquarius.traverse.TraverseTypes;
import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.entity.exception.UnexpectedExceptions;
import tech.intellispaces.entity.type.ClassFunctions;
import tech.intellispaces.entity.type.ClassNameFunctions;
import tech.intellispaces.entity.type.PrimitiveFunctions;
import tech.intellispaces.java.annotation.context.AnnotationProcessingContext;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReferences;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.NotPrimitiveReference;
import tech.intellispaces.java.reflection.reference.ReferenceBound;
import tech.intellispaces.java.reflection.reference.TypeReference;
import tech.intellispaces.java.reflection.reference.Wildcards;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class ChannelGuideGenerator extends AbstractGenerator {
  private final ObjectReferenceForm targetForm;
  private final TraverseType traverseType;
  private final MethodStatement channelMethod;
  private String guideClassSimpleName;
  private String guideAnnotation;
  private String guideMethod;
  private String traverseMethodMainForm;
  private String traverseMethodPrimitiveFormLong;
  private String traverseMethodPrimitiveFormDouble;

  public ChannelGuideGenerator(
      ObjectReferenceForm targetForm,
      TraverseType traverseType,
      CustomType initiatorType,
      CustomType domainType,
      MethodStatement channelMethod
  ) {
    super(initiatorType, domainType);
    this.targetForm = targetForm;
    this.traverseType = traverseType;
    this.channelMethod = channelMethod;
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

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
    vars.put("guideClassSimpleName", guideClassSimpleName);
    vars.put("guideTypeParams", buildGuideTypeParams());
    vars.put("guideAnnotation", guideAnnotation);
    vars.put("guideMethod", guideMethod);
    vars.put("traverseMethodMainForm", traverseMethodMainForm);
    vars.put("targetForm", targetForm.name());
    vars.put("traverseMethodPrimitiveFormLong", traverseMethodPrimitiveFormLong);
    vars.put("traverseMethodPrimitiveFormDouble", traverseMethodPrimitiveFormDouble);
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
    context.addImport(ObjectReferenceForm.class);
    context.addImport(ObjectReferenceForms.class);
    context.addImport(UnexpectedExceptions.class);
    context.addImport(PrimitiveFunctions.class);
    analyzeGuideType();
    return true;
  }

  protected String buildGuideMethod() {
    var sb = new StringBuilder();
    if (!channelMethod.typeParameters().isEmpty()) {
      sb.append("<");
      RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
      for (NamedReference typeParam : channelMethod.typeParameters()) {
        commaAppender.run();
        sb.append(typeParam.name());
        if (!typeParam.extendedBounds().isEmpty()) {
          sb.append(" extends ");
          for (ReferenceBound bound : typeParam.extendedBounds()) {
            sb.append(ObjectHandleFunctions.getCommonObjectHandleTypename(bound));
          }
        }
      }
      sb.append("> ");
    }
    sb.append(buildTargetObjectHandleFormDeclaration());
    sb.append(" ");
    sb.append(channelMethod.name());
    sb.append("(");
    sb.append(buildSourceObjectHandleDeclaration(false));
    sb.append(" source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      if (param.type().isPrimitiveReference()) {
        sb.append(param.type().asPrimitiveReferenceOrElseThrow().primitiveType().typename());
      } else {
        sb.append(buildObjectHandleDeclaration(param.type(), Function.identity()));
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
    traverseMethodMainForm = buildTraverseMethodMainForm();
    traverseMethodPrimitiveFormLong = buildTraverseMethodPrimitiveFormLong();
    traverseMethodPrimitiveFormDouble = buildTraverseMethodPrimitiveFormDouble();
    analyzeGuideMethod();
  }

  private String getGuideClassSimpleName(Class<?> guideClass) {
    String abstractGuideCanonicalName = ClassNameFunctions.addPrefixToSimpleName(
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
        default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifies: {0}",
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
        default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifies: {0}",
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
        default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifies: {0}",
            qualifierCount);
      };
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported traverse type: {0}", traverseType);
    }
  }

  protected String buildGuideTypeParams() {
    var sb = new StringBuilder();
    sb.append("<");
    sb.append(buildSourceObjectHandleDeclaration(this::replaceNamedReference, true));
    if (traverseType == TraverseTypes.Mapping || traverseType == TraverseTypes.MappingOfMoving) {
      sb.append(", ");
      sb.append(buildTargetObjectHandleDeclaration(this::replaceNamedReference, true));
    }
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(buildObjectHandleDeclaration(param.type(), this::replaceNamedReference));
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

  private String buildTraverseMethodMainForm() {
    var sb = new StringBuilder();
    sb.append("default ");
    sb.append(buildTargetObjectHandleDeclaration(this::replaceNamedReference, true));
    sb.append(" traverse(");
    sb.append(buildSourceObjectHandleDeclaration(this::replaceNamedReference, true));
    sb.append(" ").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(buildObjectHandleDeclaration(param.type(), this::replaceNamedReference));
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

  private String buildTraverseMethodPrimitiveFormLong() {
    var sb = new StringBuilder();
    sb.append("default int traverseToInt(");
    sb.append(buildSourceObjectHandleDeclaration(this::replaceNamedReference, true));
    sb.append(" ").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(buildObjectHandleDeclaration(param.type(), this::replaceNamedReference));
      sb.append(" ");
      sb.append(param.name());
    }
    sb.append(") {\n");
    buildPrimitiveMethodBody(sb, true);
    sb.append("}");
    return sb.toString();
  }

  private String buildTraverseMethodPrimitiveFormDouble() {
    var sb = new StringBuilder();
    sb.append("default double traverseToDouble(");
    sb.append(buildSourceObjectHandleDeclaration(this::replaceNamedReference, true));
    sb.append(" ").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(buildObjectHandleDeclaration(param.type(), this::replaceNamedReference));
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
        || !ClassFunctions.isPrimitiveWrapperClass(returnType.asCustomTypeReferenceOrElseThrow().targetType().canonicalName())
        || (ClassFunctions.isDoubleClass(returnType.asCustomTypeReferenceOrElseThrow().targetType().canonicalName()) && longForm)
    ) {
      sb.append("  throw UnexpectedExceptions.withMessage(\"Invalid operation\");\n");
    } else {
      sb.append("  Objects.requireNonNull(source);\n");
      for (MethodParam param : getQualifierMethodParams()) {
        sb.append("  Objects.requireNonNull(").append(param.name()).append(");\n");
      }
      sb.append("  return ");

      String returnTypeCanonicalName = returnType.asCustomTypeReferenceOrElseThrow().targetType().canonicalName();
      boolean isBooleanReturnType = ClassFunctions.isBooleanClass(returnTypeCanonicalName);
      if (isBooleanReturnType) {
        if (longForm) {
          sb.append("PrimitiveFunctions.booleanToInt(");
        } else {
          sb.append("PrimitiveFunctions.booleanToDouble(");
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

  protected String buildSourceObjectHandleDeclaration(boolean full) {
    return buildSourceObjectHandleDeclaration(Function.identity(), full);
  }

  protected String buildSourceObjectHandleDeclaration(
      Function<TypeReference, TypeReference> typeReplacer, boolean full
  ) {
    return buildObjectHandleDeclaration(
        channelMethod.params().get(0).type().asCustomTypeReferenceOrElseThrow(), typeReplacer, full
    );
  }

  protected String buildTargetObjectHandleDeclaration(
      Function<TypeReference, TypeReference> typeReplacer, boolean full
  ) {
    TypeReference returnType = channelMethod.returnType().orElseThrow();
    return buildObjectHandleDeclaration(returnType, typeReplacer, full);
  }

  private String buildTargetObjectHandleFormDeclaration() {
    if (targetForm == ObjectReferenceForms.Object) {
      return buildTargetObjectHandleDeclaration(Function.identity(), false);
    } else if (targetForm == ObjectReferenceForms.Primitive) {
      return ClassFunctions.getPrimitiveTypeOfWrapper(
          channelMethod.returnType().orElseThrow()
              .asCustomTypeReferenceOrElseThrow()
              .targetType()
              .canonicalName());
    }
    throw UnexpectedExceptions.withMessage("Not supported guide form - {0}", targetForm.name());
  }

  protected String buildObjectHandleDeclaration(
      TypeReference type, Function<TypeReference, TypeReference> typeReplacer
  ) {
    return buildObjectHandleDeclaration(type, typeReplacer, true);
  }

  protected String buildObjectHandleDeclaration(
      TypeReference type, Function<TypeReference, TypeReference> typeReplacer, boolean full
  ) {
    type = typeReplacer.apply(type);
    if (type.isNamedReference()) {
      NamedReference namedReference = type.asNamedReferenceOrElseThrow();
      if (!full || namedReference.extendedBounds().isEmpty()) {
        return type.asNamedReferenceOrElseThrow().name();
      }
      var sb = new StringBuilder();
      sb.append(type.asNamedReferenceOrElseThrow().name());
      sb.append(" extends ");
      RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
      for (ReferenceBound bound : namedReference.extendedBounds()) {
        commaAppender.run();
        sb.append(buildObjectHandleDeclaration(bound, typeReplacer));
      }
      return sb.toString();
    } else if (type.isPrimitiveReference()) {
      return ClassFunctions.getPrimitiveWrapperClass(type.asPrimitiveReferenceOrElseThrow().typename()).getSimpleName();
    } else {
      String canonicalName = ObjectHandleFunctions.getCommonObjectHandleTypename(type, typeReplacer);
      String name = type.isCustomTypeReference() ? context.addToImportAndGetSimpleName(canonicalName) : canonicalName;
      if (type.isCustomTypeReference() && !type.asCustomTypeReferenceOrElseThrow().typeArguments().isEmpty()) {
        var sb = new StringBuilder();
        sb.append(name);
        sb.append("<");
        RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
        for (NotPrimitiveReference typeArg : type.asCustomTypeReferenceOrElseThrow().typeArguments()) {
          commaAppender.run();
          sb.append(buildObjectHandleDeclaration(typeArg, typeReplacer, full));
        }
        sb.append(">");
        return sb.toString();
      }
      return name;
    }
  }

  private TypeReference replaceNamedReference(TypeReference type) {
    if (type.isNamedReference()) {
      NamedReference namedReference = type.asNamedReferenceOrElseThrow();
      if (namedReference.extendedBounds().isEmpty()) {
        return CustomTypeReferences.get(Object.class);
      }
      ReferenceBound bound = namedReference.extendedBounds().get(0);
      if (bound.isCustomTypeReference()) {
        return Wildcards.get(bound);
      }
    }
    return type;
  }

  private String channelClassSimpleName() {
    return context.addToImportAndGetSimpleName(annotatedType.canonicalName());
  }

  private List<MethodParam> getQualifierMethodParams() {
    return channelMethod.params().subList(1, channelMethod.params().size());
  }

  private String getGuideClassCanonicalName() {
    return NameConventionFunctions.getGuideClassCanonicalName(
        targetForm, annotatedType.packageName(), annotatedType, channelMethod
    );
  }
}
