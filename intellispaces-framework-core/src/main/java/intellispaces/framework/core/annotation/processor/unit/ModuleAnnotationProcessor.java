package intellispaces.framework.core.annotation.processor.unit;

import com.google.auto.service.AutoService;
import intellispaces.common.annotationprocessor.AnnotatedTypeProcessor;
import intellispaces.common.annotationprocessor.generator.GenerationTask;
import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.framework.core.validation.ModuleValidator;
import intellispaces.common.javastatement.customtype.CustomType;

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
  public boolean isApplicable(CustomType moduleType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(moduleType);
  }

  @Override
  public AnnotatedTypeValidator getValidator() {
    return new ModuleValidator();
  }

  @Override
  public List<GenerationTask> makeTasks(CustomType initiatorType, CustomType moduleType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeModuleArtifactGenerators(initiatorType, moduleType);
  }
}
