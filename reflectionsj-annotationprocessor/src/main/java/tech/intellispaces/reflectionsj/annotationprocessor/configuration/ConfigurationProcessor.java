package tech.intellispaces.reflectionsj.annotationprocessor.configuration;

import java.util.List;
import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;

import com.google.auto.service.AutoService;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.reflectionsj.annotation.Configuration;
import tech.intellispaces.reflectionsj.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflectionsj.annotationprocessor.JaquariusArtifactProcessor;
import tech.intellispaces.reflectionsj.annotationprocessor.module.UnitWrapperGenerator;
import tech.intellispaces.statementsj.customtype.CustomType;

@AutoService(Processor.class)
public class ConfigurationProcessor extends ArtifactProcessor {

  public ConfigurationProcessor() {
    super(ElementKind.CLASS, Configuration.class, JaquariusArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType moduleType) {
    return AnnotationFunctions.isAutoGenerationEnabled(moduleType);
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
