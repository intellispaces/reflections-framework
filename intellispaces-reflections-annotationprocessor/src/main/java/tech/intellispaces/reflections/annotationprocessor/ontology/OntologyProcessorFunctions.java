package tech.intellispaces.reflections.annotationprocessor.ontology;

import java.util.ArrayList;
import java.util.List;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.reflections.annotation.Channel;
import tech.intellispaces.reflections.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.artifact.ArtifactTypes;
import tech.intellispaces.jstatements.customtype.CustomType;
import tech.intellispaces.jstatements.method.MethodStatement;

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
          ontologyType, ArtifactTypes.Channel, context.initialRoundEnvironment()
      )) {
        generators.add(new OntologyChannelGenerator(
            ontologyType, method
        ));
      }
    }
    return generators;
  }
}
