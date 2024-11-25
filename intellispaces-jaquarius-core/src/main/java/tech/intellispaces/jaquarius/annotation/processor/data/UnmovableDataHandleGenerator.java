package tech.intellispaces.jaquarius.annotation.processor.data;

import tech.intellispaces.jaquarius.annotation.Name;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotation.processor.AbstractGenerator;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.MappingChannel;
import tech.intellispaces.jaquarius.common.NameConventionFunctions;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.object.ObjectHandleTypes;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.entity.exception.NotImplementedExceptions;
import tech.intellispaces.entity.type.Type;
import tech.intellispaces.entity.type.Types;
import tech.intellispaces.java.annotation.context.AnnotationProcessingContext;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.TypeReference;

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
    context.addImport(NotImplementedExceptions.class);
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
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(typeParamsFullBuilder, typeParamsBriefBuilder);

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
