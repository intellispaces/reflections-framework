package tech.intellispaces.jaquarius.annotationprocessor.module;

import com.google.auto.service.AutoService;
import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.ArtifactProcessor;
import tech.intellispaces.commons.annotation.processor.ArtifactValidator;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotationprocessor.ArtifactGenerationAnnotationFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactProcessor;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;

@AutoService(Processor.class)
public class ModuleProcessor extends ArtifactProcessor {

  public ModuleProcessor() {
    super(ElementKind.CLASS, Module.class, JaquariusArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType moduleType) {
    return ArtifactGenerationAnnotationFunctions.isAutoGenerationEnabled(moduleType);
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
