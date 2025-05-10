package tech.intellispaces.reflections.annotationprocessor.customizer;

import java.util.List;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;

import com.google.auto.service.AutoService;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.reflections.framework.annotation.AssistantCustomizer;
import tech.intellispaces.reflections.annotationprocessor.JaquariusArtifactProcessor;
import tech.intellispaces.jstatements.customtype.CustomType;

@AutoService(Processor.class)
public class AssistantCustomizerProcessor extends ArtifactProcessor {

  public AssistantCustomizerProcessor() {
    super(Set.of(ElementKind.INTERFACE), AssistantCustomizer.class, JaquariusArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType moduleType) {
    return true;
  }

  @Override
  public ArtifactValidator validator() {
    return new CustomizerValidator();
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType guideType, ArtifactGeneratorContext context) {
    return List.of();
  }
}
