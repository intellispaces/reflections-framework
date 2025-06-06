package tech.intellispaces.reflections.framework.annotationprocessor.channel;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.commons.type.ClassNameFunctions;
import tech.intellispaces.commons.type.PrimitiveFunctions;
import tech.intellispaces.commons.type.PrimitiveTypes;
import tech.intellispaces.core.Rid;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.CustomTypeReferences;
import tech.intellispaces.javareflection.reference.NamedReference;
import tech.intellispaces.javareflection.reference.NotPrimitiveReference;
import tech.intellispaces.javareflection.reference.PrimitiveReference;
import tech.intellispaces.javareflection.reference.ReferenceBound;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.javareflection.reference.Wildcards;
import tech.intellispaces.reflections.framework.annotation.Guide;
import tech.intellispaces.reflections.framework.annotation.Mapper;
import tech.intellispaces.reflections.framework.annotation.MapperOfMoving;
import tech.intellispaces.reflections.framework.annotation.Mover;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactGenerator;
import tech.intellispaces.reflections.framework.guide.n0.Mapper0;
import tech.intellispaces.reflections.framework.guide.n0.MapperOfMoving0;
import tech.intellispaces.reflections.framework.guide.n0.Mover0;
import tech.intellispaces.reflections.framework.guide.n1.Mapper1;
import tech.intellispaces.reflections.framework.guide.n1.MapperOfMoving1;
import tech.intellispaces.reflections.framework.guide.n1.Mover1;
import tech.intellispaces.reflections.framework.guide.n2.Mapper2;
import tech.intellispaces.reflections.framework.guide.n2.MapperOfMoving2;
import tech.intellispaces.reflections.framework.guide.n2.Mover2;
import tech.intellispaces.reflections.framework.guide.n3.Mapper3;
import tech.intellispaces.reflections.framework.guide.n3.MapperOfMoving3;
import tech.intellispaces.reflections.framework.guide.n3.Mover3;
import tech.intellispaces.reflections.framework.guide.n4.Mapper4;
import tech.intellispaces.reflections.framework.guide.n4.MapperOfMoving4;
import tech.intellispaces.reflections.framework.guide.n4.Mover4;
import tech.intellispaces.reflections.framework.guide.n5.Mapper5;
import tech.intellispaces.reflections.framework.guide.n5.MapperOfMoving5;
import tech.intellispaces.reflections.framework.guide.n5.Mover5;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.traverse.TraverseType;
import tech.intellispaces.reflections.framework.traverse.TraverseTypes;

public class DefaultGuideFormGenerator extends ReflectionsArtifactGenerator {
  private final ReflectionForm targetForm;
  private final TraverseType traverseType;
  private final MethodStatement channelMethod;
  private String guideClassSimpleName;
  private String guideAnnotation;
  private String guideMethod;
  private String traverseMethodMainForm;
  private String traverseMethodPrimitiveFormLong;
  private String traverseMethodPrimitiveFormDouble;

  public DefaultGuideFormGenerator(
      ReflectionForm targetForm,
      TraverseType traverseType,
      CustomType channelType,
      MethodStatement channelMethod
  ) {
    super(channelType);
    this.targetForm = targetForm;
    this.traverseType = traverseType;
    this.channelMethod = channelMethod;
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  public String generatedArtifactName() {
    return getGuideClassCanonicalName();
  }

  @Override
  protected String templateName() {
    return "/guide.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    if (sourceArtifact().isNested()) {
      addImport(sourceArtifact().canonicalName());
    }
    addImports(
        Rid.class,
        Guide.class,
        Objects.class,
        ChannelFunctions.class,
        ReflectionForm.class,
        ReflectionForms.class,
        PrimitiveFunctions.class,
        UnexpectedExceptions.class
    );

    analyzeGuideType();

    addVariable("channelClassSimpleName", channelClassSimpleName());
    addVariable("guideClassSimpleName", guideClassSimpleName);
    addVariable("sourceType", buildSourceReflectionDeclaration(this::replaceNamedReference, true));
    addVariable("sourceClass", getSourceTypename());
    addVariable("guideTypeParams", buildGuideTypeParams());
    addVariable("guideAnnotation", guideAnnotation);
    addVariable("guideMethod", guideMethod);
    addVariable("traverseMethodMainForm", traverseMethodMainForm);
    addVariable("targetForm", targetForm.name());
    addVariable("traverseMethodPrimitiveFormLong", traverseMethodPrimitiveFormLong);
    addVariable("traverseMethodPrimitiveFormDouble", traverseMethodPrimitiveFormDouble);
    return true;
  }

  private String getSourceTypename() {
    TypeReference sourceType = channelMethod.params().get(0).type();
    String sourceTypename = ReflectionFunctions.getObjectFormTypename(
        ReflectionForms.Reflection,
        sourceType,
        this::replaceNamedReference
    );
    return sourceType.isCustomTypeReference() ? addImportAndGetSimpleName(sourceTypename) : sourceTypename;
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
            sb.append(ReflectionFunctions.getGeneralReflectionTypename(bound));
          }
        }
      }
      sb.append("> ");
    }
    sb.append(buildTargetReflectionFormDeclaration());
    sb.append(" ");
    sb.append(channelMethod.name());
    sb.append("(");
    sb.append(buildSourceReflectionDeclaration(false));
    sb.append(" source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      if (param.type().isPrimitiveReference()) {
        sb.append(param.type().asPrimitiveReferenceOrElseThrow().primitiveType().typename());
      } else {
        sb.append(buildReflectionDeclaration(ReflectionForms.Reflection, param.type(), Function.identity()));
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
    return addImportAndGetSimpleName(abstractGuideCanonicalName);
  }

  private Class<?> getGuideClass() {
    int qualifierCount = getQualifierMethodParams().size();
    if (TraverseTypes.Mapping.is(traverseType)) {
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
    } else if (TraverseTypes.Moving.is(traverseType)) {
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
    } else if (TraverseTypes.MappingOfMoving.is(traverseType)) {
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
    sb.append(buildSourceReflectionDeclaration(this::replaceNamedReference, true));
    if (TraverseTypes.Mapping.is(traverseType) || TraverseTypes.MappingOfMoving.is(traverseType)) {
      sb.append(", ");
      sb.append(buildTargetReflectionDeclaration(this::replaceNamedReference, true));
    }
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(buildReflectionDeclaration(ReflectionForms.Reflection, param.type(), this::replaceNamedReference));
    }
    sb.append(">");
    return sb.toString();
  }

  private void analyzeGuideMethod() {
    if (TraverseTypes.Mapping.is(traverseType)) {
      addImport(Mapper.class);
      guideAnnotation = "@" + Mapper.class.getSimpleName();
    } else if (TraverseTypes.Moving.is(traverseType)) {
      addImport(Mover.class);
      guideAnnotation = "@" + Mover.class.getSimpleName();
    } else if (TraverseTypes.MappingOfMoving.is(traverseType)) {
      addImport(MapperOfMoving.class);
      guideAnnotation = "@" + MapperOfMoving.class.getSimpleName();
    }

    guideMethod = buildGuideMethod();
  }

  private String buildTraverseMethodMainForm() {
    var sb = new StringBuilder();
    sb.append("default ");
    sb.append(buildTargetReflectionDeclaration(this::replaceNamedReference, true));
    sb.append(" traverse(");
    sb.append(buildSourceReflectionDeclaration(this::replaceNamedReference, true));
    sb.append(" ").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(buildReflectionDeclaration(ReflectionForms.Reflection, param.type(), this::replaceNamedReference));
      sb.append(" ");
      sb.append(param.name());
    }
    sb.append(") {\n");
    sb.append("  Objects.requireNonNull(source);\n");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append("  Objects.requireNonNull(").append(param.name()).append(");\n");
    }
    if (!channelMethod.returnType().orElseThrow().isVoidType()) {
      sb.append("  return ");
    }
    sb.append(channelMethod.name());
    sb.append("(").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(param.name());
    }
    sb.append(");\n");
    if (channelMethod.returnType().orElseThrow().isVoidType()) {
      sb.append("  return null;\n");
    }
    sb.append("}");
    return sb.toString();
  }

  private String buildTraverseMethodPrimitiveFormLong() {
    var sb = new StringBuilder();
    sb.append("default int traverseToInt(");
    sb.append(buildSourceReflectionDeclaration(this::replaceNamedReference, true));
    sb.append(" ").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(buildReflectionDeclaration(ReflectionForms.Reflection, param.type(), this::replaceNamedReference));
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
    sb.append(buildSourceReflectionDeclaration(this::replaceNamedReference, true));
    sb.append(" ").append("source");
    for (MethodParam param : getQualifierMethodParams()) {
      sb.append(", ");
      sb.append(buildReflectionDeclaration(ReflectionForms.Reflection, param.type(), this::replaceNamedReference));
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
    if (returnType.isNamedReference()) {
      sb.append("  throw UnexpectedExceptions.withMessage(\"Invalid operation\");\n");
    } else if (returnType.isCustomTypeReference() && (
        !ClassFunctions.isPrimitiveWrapperClass(returnType.asCustomTypeReferenceOrElseThrow().targetType().canonicalName())
            || (ClassFunctions.isDoubleClass(returnType.asCustomTypeReferenceOrElseThrow().targetType().canonicalName()) && longForm
    ))) {
      sb.append("  throw UnexpectedExceptions.withMessage(\"Invalid operation\");\n");
    } else {
      sb.append("  Objects.requireNonNull(source);\n");
      for (MethodParam param : getQualifierMethodParams()) {
        sb.append("  Objects.requireNonNull(").append(param.name()).append(");\n");
      }
      sb.append("  return ");

      final boolean isBooleanReturnType;
      if (returnType.isPrimitiveReference()) {
        PrimitiveReference primitiveReference = returnType.asPrimitiveReferenceOrElseThrow();
        isBooleanReturnType = PrimitiveTypes.Boolean.is(primitiveReference.primitiveType());
      } else {
        String returnTypeCanonicalName = returnType.asCustomTypeReferenceOrElseThrow().targetType().canonicalName();
        isBooleanReturnType = ClassFunctions.isBooleanClass(returnTypeCanonicalName);
      }
      if (isBooleanReturnType) {
        if (longForm) {
          sb.append("PrimitiveFunctions.booleanToInt(");
        } else {
          sb.append("PrimitiveFunctions.booleanToDouble(");
        }
      } else {
        if (longForm) {
          sb.append("(int) ");
        } else {
          sb.append("(double) ");
        }
        sb.append("(Object) ");
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

  private String buildSourceReflectionDeclaration(boolean full) {
    return buildSourceReflectionDeclaration(Function.identity(), full);
  }

  private String buildSourceReflectionDeclaration(
      Function<TypeReference, TypeReference> typeReplacer, boolean full
  ) {
    return buildReflectionDeclaration(
        ReflectionForms.Reflection,
        channelMethod.params().get(0).type().asCustomTypeReferenceOrElseThrow(),
        typeReplacer,
        full
    );
  }

  protected String buildTargetReflectionDeclaration(
      Function<TypeReference, TypeReference> typeReplacer, boolean full
  ) {
    TypeReference returnType = channelMethod.returnType().orElseThrow();
    return buildReflectionDeclaration(ReflectionForms.Reflection, returnType, typeReplacer, full);
  }

  private String buildTargetReflectionFormDeclaration() {
    if (ReflectionForms.Reflection.is(targetForm)) {
      return buildTargetReflectionDeclaration(Function.identity(), false);
    } else if (ReflectionForms.Primitive.is(targetForm)) {
      return ClassFunctions.primitiveTypenameOfWrapper(
          channelMethod.returnType().orElseThrow()
              .asCustomTypeReferenceOrElseThrow()
              .targetType()
              .canonicalName());
    }
    throw UnexpectedExceptions.withMessage("Not supported guide form - {0}", targetForm.name());
  }

  private String buildReflectionDeclaration(
          ReflectionForm form, TypeReference type, Function<TypeReference, TypeReference> typeReplacer
  ) {
    return buildReflectionDeclaration(form, type, typeReplacer, true);
  }

  private String buildReflectionDeclaration(
          ReflectionForm form, TypeReference type, Function<TypeReference, TypeReference> typeReplacer, boolean full
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
        sb.append(buildReflectionDeclaration(form, bound, typeReplacer, true));
      }
      return sb.toString();
    } else if (type.isPrimitiveReference()) {
      return ClassFunctions.wrapperClassOfPrimitive(type.asPrimitiveReferenceOrElseThrow().typename()).getSimpleName();
    } else {
      String canonicalName = ReflectionFunctions.getObjectFormTypename(form, type, typeReplacer);
      String name = type.isCustomTypeReference() ? addImportAndGetSimpleName(canonicalName) : canonicalName;
      if (type.isCustomTypeReference() && !type.asCustomTypeReferenceOrElseThrow().typeArguments().isEmpty()) {
        var sb = new StringBuilder();
        sb.append(name);
        sb.append("<");
        RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
        for (NotPrimitiveReference typeArg : type.asCustomTypeReferenceOrElseThrow().typeArguments()) {
          commaAppender.run();
          sb.append(buildReflectionDeclaration(form, typeArg, typeReplacer, full));
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
    return addImportAndGetSimpleName(sourceArtifact().canonicalName());
  }

  private List<MethodParam> getQualifierMethodParams() {
    return channelMethod.params().subList(1, channelMethod.params().size());
  }

  private String getGuideClassCanonicalName() {
    return NameConventionFunctions.getGuideClassCanonicalName(
        targetForm, sourceArtifact().packageName(), sourceArtifact(), channelMethod
    );
  }
}
