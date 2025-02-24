package tech.intellispaces.jaquarius.annotationprocessor.dataset;

import tech.intellispaces.commons.action.runnable.RunnableAction;
import tech.intellispaces.commons.action.text.StringActions;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.base.exception.NotImplementedExceptions;
import tech.intellispaces.commons.base.type.Type;
import tech.intellispaces.commons.base.type.Types;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.commons.java.reflection.reference.NamedReference;
import tech.intellispaces.commons.java.reflection.reference.TypeReference;
import tech.intellispaces.jaquarius.annotation.Name;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.traverse.MappingTraverse;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.system.Modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UnmovableDatasetHandleGenerator extends JaquariusArtifactGenerator {
  protected String typeParamsBrief;
  private final List<Map<String, String>> projectionProperties = new ArrayList<>();

  public UnmovableDatasetHandleGenerator(CustomType dataType) {
    super(dataType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getDatasetClassName(sourceArtifact().className());
  }

  @Override
  protected String templateName() {
    return "/unmovable_data_handle.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    if (sourceArtifact().isNested()) {
      addImport(sourceArtifactName());
    }
    addImports(
        Name.class,
        ObjectHandle.class,
        Objects.class,
        Modules.class,
        Type.class,
        Types.class,
        Channel1.class,
        MappingTraverse.class,
        TraverseException.class,
        NotImplementedExceptions.class
    );

    analyzeTypeParams();
    analyzeProjections();

    addVariable("objectHandleClassName", NameConventionFunctions.getUnmovableObjectHandleTypename(sourceArtifact().className(), true));
    addVariable("typeParamsBrief", typeParamsBrief);
    addVariable("projections", projectionProperties);

    return true;
  }

  private void analyzeProjections() {
    for (MethodStatement method : sourceArtifact().actualMethods()) {
      TypeReference type = method.returnType().orElseThrow();
      String handleType = buildObjectHandleDeclaration(type, ObjectHandleTypes.Unmovable, true);

      Map<String, String> properties = new HashMap<>();
      properties.put("type", handleType);
      properties.put("name", method.name());
      projectionProperties.add(properties);
    }
  }

  protected void analyzeTypeParams() {
    if (sourceArtifact().typeParameters().isEmpty()) {
      typeParamsBrief = "";
      return;
    }

    var typeParamsFullBuilder = new StringBuilder();
    var typeParamsBriefBuilder = new StringBuilder();
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(typeParamsFullBuilder, typeParamsBriefBuilder);

    typeParamsFullBuilder.append("<");
    typeParamsBriefBuilder.append("<");
    for (NamedReference typeParam : sourceArtifact().typeParameters()) {
      commaAppender.run();
      typeParamsFullBuilder.append(typeParam.formalFullDeclaration());
      typeParamsBriefBuilder.append(typeParam.formalBriefDeclaration());
    }
    typeParamsFullBuilder.append(">");
    typeParamsBriefBuilder.append(">");
    typeParamsBrief = typeParamsBriefBuilder.toString();
  }
}
