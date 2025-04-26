package tech.intellispaces.jaquarius.annotationprocessor.customizer;

import java.util.List;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;

import com.google.auto.service.AutoService;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.jaquarius.annotation.Customizer;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactProcessor;
import tech.intellispaces.reflection.customtype.CustomType;

@AutoService(Processor.class)
public class CustomizerProcessor extends ArtifactProcessor {

  public CustomizerProcessor() {
    super(Set.of(ElementKind.INTERFACE), Customizer.class, JaquariusArtifactProcessor.SOURCE_VERSION);
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
