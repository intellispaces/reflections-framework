package tech.intellispaces.reflections.framework.annotationprocessor.ontology;

import java.util.ArrayList;
import java.util.List;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.framework.artifact.ArtifactTypes;

public interface OntologyProcessorFunctions {

  static List<ArtifactGenerator> makeOntologyArtifactGenerators(
      CustomType ontologyType, ArtifactGeneratorContext context
  ) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    for (MethodStatement method : ontologyType.declaredMethods()) {
      if (!method.hasAnnotation(Channel.class)) {
        continue;
      }
      if (AnnotationFunctions.isAutoGenerationEnabled(
          ontologyType, ArtifactTypes.ReflectionChannel, context.initialRoundEnvironment()
      )) {
        generators.add(new OntologyChannelGenerator(
            ontologyType, method
        ));
      }
    }
    return generators;
  }
}
