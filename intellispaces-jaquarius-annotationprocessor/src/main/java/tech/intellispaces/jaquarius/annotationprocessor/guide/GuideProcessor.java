package tech.intellispaces.jaquarius.annotationprocessor.guide;

import com.google.auto.service.AutoService;
import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.jaquarius.annotation.Guide;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorConstants;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;

@AutoService(Processor.class)
public class GuideProcessor extends ArtifactProcessor {

  public GuideProcessor() {
    super(ElementKind.INTERFACE, Guide.class, AnnotationProcessorConstants.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType moduleType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(moduleType);
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType guideType, ArtifactGeneratorContext context) {
    return List.of(new AutoGuideGenerator(guideType));
  }
}
