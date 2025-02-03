package tech.intellispaces.jaquarius.annotationprocessor.ontology;

import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.ArtifactTypes;

import java.util.ArrayList;
import java.util.List;

public interface OntologyProcessorFunctions {

  static List<ArtifactGenerator> makeOntologyArtifactGenerators(
      CustomType ontologyType, ArtifactGeneratorContext context
  ) {
    List<ArtifactGenerator> generators = new ArrayList<>();
    for (MethodStatement method : ontologyType.declaredMethods()) {
      if (!method.hasAnnotation(Channel.class)) {
        continue;
      }
      if (AnnotationProcessorFunctions.isAutoGenerationEnabled(
          ontologyType, ArtifactTypes.Channel, context.roundEnvironment()
      )) {
        generators.add(new OntologyChannelGenerator(
            ontologyType, method
        ));
      }
    }
    return generators;
  }
}
