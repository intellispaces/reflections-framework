package intellispaces.jaquarius.annotation.processor.data;

import intellispaces.common.action.runner.Runner;
import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.base.text.TextActions;
import intellispaces.common.base.type.Type;
import intellispaces.common.base.type.Types;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.NamedReference;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.jaquarius.annotation.Name;
import intellispaces.jaquarius.annotation.ObjectHandle;
import intellispaces.jaquarius.channel.Channel1;
import intellispaces.jaquarius.channel.MappingChannel;
import intellispaces.jaquarius.common.NameConventionFunctions;
import intellispaces.jaquarius.exception.TraverseException;
import intellispaces.jaquarius.object.ObjectHandleTypes;
import intellispaces.jaquarius.annotation.processor.AbstractGenerator;
import intellispaces.jaquarius.system.Modules;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UnmovableDataHandleGenerator extends AbstractGenerator {
  protected String typeParamsBrief;
  private final List<Map<String, String>> projectionProperties = new ArrayList<>();

  public UnmovableDataHandleGenerator(CustomType initiatorType, CustomType dataType) {
    super(initiatorType, dataType);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  public String artifactName() {
    return NameConventionFunctions.getDataClassName(annotatedType.className());
  }

  @Override
  protected String templateName() {
    return "/unmovable_data_handle.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    return Map.of(
        "generatedAnnotation", makeGeneratedAnnotation(),
        "packageName", context.packageName(),
        "sourceClassName", sourceClassCanonicalName(),
        "sourceClassSimpleName", sourceClassSimpleName(),
        "objectHandleClassName", NameConventionFunctions.getUnmovableObjectHandleTypename(annotatedType.className()),
        "typeParamsBrief", typeParamsBrief,
        "classSimpleName", context.generatedClassSimpleName(),
        "importedClasses", context.getImports(),
        "projections", projectionProperties
    );
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }
    context.addImport(Name.class);
    context.addImport(ObjectHandle.class);
    context.addImport(Modules.class);
    context.addImport(Type.class);
    context.addImport(Types.class);
    context.addImport(Channel1.class);
    context.addImport(TraverseException.class);
    context.addImport(Objects.class);
    context.addImport(MappingChannel.class);

    analyzeTypeParams(annotatedType);
    analyzeProjections();
    return true;
  }

  private void analyzeProjections() {
    for (MethodStatement method : annotatedType.actualMethods()) {
      TypeReference type = method.returnType().orElseThrow();
      String handleType = getObjectHandleDeclaration(type, ObjectHandleTypes.Unmovable);

      Map<String, String> properties = new HashMap<>();
      properties.put("type", handleType);
      properties.put("name", method.name());
      projectionProperties.add(properties);
    }
  }

  protected void analyzeTypeParams(CustomType objectHandleType) {
    if (objectHandleType.typeParameters().isEmpty()) {
      typeParamsBrief = "";
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
    typeParamsBrief = typeParamsBriefBuilder.toString();
  }
}
