package tech.intellispaces.jaquarius.annotationprocessor.preprocessing;

import com.google.auto.service.AutoService;
import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.jaquarius.annotation.Preprocessing;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorConstants;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class PreprocessingAnnotationProcessor extends ArtifactProcessor {

  public PreprocessingAnnotationProcessor() {
    super(
        Set.of(ElementKind.CLASS, ElementKind.INTERFACE),
        Preprocessing.class,
        AnnotationProcessorConstants.SOURCE_VERSION
    );
  }

  @Override
  public boolean isApplicable(CustomType customType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(customType);
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType sourceArtifact, ArtifactGeneratorContext context) {
    return AnnotationProcessorFunctions.makePreprocessingArtifactGenerators(sourceArtifact, context);
  }
}
