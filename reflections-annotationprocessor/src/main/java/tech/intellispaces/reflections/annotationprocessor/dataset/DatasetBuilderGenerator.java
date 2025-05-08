package tech.intellispaces.reflections.annotationprocessor.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.collection.ArraysFunctions;
import tech.intellispaces.reflections.annotation.Channel;
import tech.intellispaces.reflections.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.reflections.naming.NameConventionFunctions;
import tech.intellispaces.reflections.object.reference.MovabilityTypes;
import tech.intellispaces.reflections.object.reference.ObjectReferenceForms;
import tech.intellispaces.reflections.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.reflections.traverse.TraverseTypes;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.jstatements.reference.TypeReference;

public class DatasetBuilderGenerator extends JaquariusArtifactGenerator {
  private final List<Map<String, String>> projectionProperties = new ArrayList<>();

  public DatasetBuilderGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  protected String templateName() {
    return "/dataset_builder.template";
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getDatasetBuilderCanonicalName(sourceArtifact().className());
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    analyzeProjections();

    addVariable("projections", projectionProperties);
    addVariable("unmovableObjectHandleSimpleName",
        addImportAndGetSimpleName(ObjectReferenceFunctions.getUnmovableObjectHandleTypename(sourceArtifact())));
    addVariable("unmovableDatasetSimpleName",
        addImportAndGetSimpleName(NameConventionFunctions.getUnmovableDatasetClassName(sourceArtifactName())));
    return true;
  }

  private void analyzeProjections() {
    for (MethodStatement method : sourceArtifact().actualMethods()) {
      if (isMovingMethod(method)) {
        continue;
      }
      TypeReference type = method.returnType().orElseThrow();
      String handleType = buildObjectFormDeclaration(type, ObjectReferenceForms.ObjectHandle, MovabilityTypes.Unmovable, true);

      Map<String, String> properties = new HashMap<>();
      properties.put("type", handleType);
      properties.put("name", method.name());
      projectionProperties.add(properties);
    }
  }

  private boolean isMovingMethod(MethodStatement method) {
    Optional<Channel> channel = method.selectAnnotation(Channel.class);
    if (channel.isEmpty()) {
      return true;
    }
    return ArraysFunctions.containsAny(
        channel.orElseThrow().allowedTraverse(), TraverseTypes.Moving, TraverseTypes.MappingOfMoving
    );
  }
}
