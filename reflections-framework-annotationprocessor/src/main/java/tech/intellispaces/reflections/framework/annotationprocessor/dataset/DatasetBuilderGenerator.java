package tech.intellispaces.reflections.framework.annotationprocessor.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.collection.ArraysFunctions;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactGenerator;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.MovabilityTypes;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.traverse.TraverseTypes;

public class DatasetBuilderGenerator extends ReflectionsArtifactGenerator {
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
    addVariable("unmovableReflectionSimpleName",
        addImportAndGetSimpleName(ReflectionFunctions.getUnmovableReflectionTypename(sourceArtifact())));
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
      String reflectionType = buildObjectFormDeclaration(type, ReflectionForms.Reflection, MovabilityTypes.General, true);

      Map<String, String> properties = new HashMap<>();
      properties.put("type", reflectionType);
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
