package tech.intellispaces.reflections.annotationprocessor.module;

import java.util.List;
import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;

import com.google.auto.service.AutoService;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.reflections.annotation.Module;
import tech.intellispaces.reflections.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.annotationprocessor.JaquariusArtifactProcessor;
import tech.intellispaces.jstatements.customtype.CustomType;

@AutoService(Processor.class)
public class ModuleProcessor extends ArtifactProcessor {

  public ModuleProcessor() {
    super(ElementKind.CLASS, Module.class, JaquariusArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType moduleType) {
    return AnnotationFunctions.isAutoGenerationEnabled(moduleType);
  }

  @Override
  public ArtifactValidator validator() {
    return new ModuleValidator();
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType moduleType, ArtifactGeneratorContext context) {
    return List.of(new UnitWrapperGenerator(moduleType));
  }
}
