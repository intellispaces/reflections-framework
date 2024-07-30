package tech.intellispaces.core.annotation.processor.module;

import com.google.auto.service.AutoService;
import tech.intellispaces.annotations.AnnotatedTypeProcessor;
import tech.intellispaces.annotations.generator.ArtifactGenerator;
import tech.intellispaces.annotations.validator.AnnotatedTypeValidator;
import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.core.validation.ModuleValidator;
import tech.intellispaces.javastatements.customtype.CustomType;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class ModuleAnnotationProcessor extends AnnotatedTypeProcessor {

  public ModuleAnnotationProcessor() {
    super(Module.class, Set.of(ElementKind.CLASS));
  }

  @Override
  protected boolean isApplicable(CustomType moduleType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(moduleType);
  }

  @Override
  protected AnnotatedTypeValidator getValidator() {
    return new ModuleValidator();
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType moduleType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeModuleArtifactGenerators(moduleType);
  }
}
