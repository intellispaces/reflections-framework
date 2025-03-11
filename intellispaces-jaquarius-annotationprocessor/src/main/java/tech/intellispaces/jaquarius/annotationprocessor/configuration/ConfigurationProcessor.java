package tech.intellispaces.jaquarius.annotationprocessor.configuration;

import com.google.auto.service.AutoService;
import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.ArtifactProcessor;
import tech.intellispaces.commons.annotation.processor.ArtifactValidator;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.jaquarius.annotation.Configuration;
import tech.intellispaces.jaquarius.annotationprocessor.ArtifactGenerationAnnotationFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactProcessor;
import tech.intellispaces.jaquarius.annotationprocessor.module.UnitWrapperGenerator;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;

@AutoService(Processor.class)
public class ConfigurationProcessor extends ArtifactProcessor {

  public ConfigurationProcessor() {
    super(ElementKind.CLASS, Configuration.class, JaquariusArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType moduleType) {
    return ArtifactGenerationAnnotationFunctions.isAutoGenerationEnabled(moduleType);
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType configurationType, ArtifactGeneratorContext context) {
    return List.of(new UnitWrapperGenerator(configurationType));
  }
}
