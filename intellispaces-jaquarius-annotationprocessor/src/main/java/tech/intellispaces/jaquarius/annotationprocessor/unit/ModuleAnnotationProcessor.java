package tech.intellispaces.jaquarius.annotationprocessor.unit;

import com.google.auto.service.AutoService;
import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorConstants;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.module.ModuleValidator;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;

@AutoService(Processor.class)
public class ModuleAnnotationProcessor extends ArtifactProcessor {

  public ModuleAnnotationProcessor() {
    super(ElementKind.CLASS, Module.class, AnnotationProcessorConstants.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType moduleType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(moduleType);
  }

  @Override
  public ArtifactValidator validator() {
    return new ModuleValidator();
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType moduleType, ArtifactGeneratorContext context) {
    return AnnotationProcessorFunctions.makeModuleArtifactGenerators(moduleType);
  }
}
