package tech.intellispaces.reflections.framework.annotationprocessor.configuration;

import java.util.List;
import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;

import com.google.auto.service.AutoService;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.reflections.framework.annotation.Configuration;
import tech.intellispaces.reflections.framework.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactProcessor;
import tech.intellispaces.reflections.framework.annotationprocessor.module.UnitWrapperGenerator;

@AutoService(Processor.class)
public class ConfigurationProcessor extends ArtifactProcessor {

  public ConfigurationProcessor() {
    super(ElementKind.CLASS, Configuration.class, ReflectionsArtifactProcessor.SOURCE_VERSION);
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
