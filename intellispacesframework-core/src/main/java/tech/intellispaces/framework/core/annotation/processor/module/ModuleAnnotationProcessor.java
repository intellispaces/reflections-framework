package tech.intellispaces.framework.core.annotation.processor.module;

import com.google.auto.service.AutoService;
import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeProcessor;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.annotationprocessor.validator.AnnotatedTypeValidator;
import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.framework.core.validation.ModuleValidator;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

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
