package tech.intellispaces.reflectionsj.annotationprocessor.ontology;

import java.util.ArrayList;
import java.util.List;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.reflectionsj.annotation.Channel;
import tech.intellispaces.reflectionsj.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflectionsj.artifact.ArtifactTypes;
import tech.intellispaces.statementsj.customtype.CustomType;
import tech.intellispaces.statementsj.method.MethodStatement;

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
