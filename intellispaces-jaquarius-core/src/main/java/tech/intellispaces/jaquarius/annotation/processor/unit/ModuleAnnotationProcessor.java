package tech.intellispaces.jaquarius.annotation.processor.unit;

import com.google.auto.service.AutoService;
import tech.intellispaces.jaquarius.annotation.Module;
import tech.intellispaces.jaquarius.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.jaquarius.validation.ModuleValidator;
import tech.intellispaces.java.annotation.AnnotatedTypeProcessor;
import tech.intellispaces.java.annotation.generator.Generator;
import tech.intellispaces.java.annotation.validator.AnnotatedTypeValidator;
import tech.intellispaces.java.reflection.customtype.CustomType;

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
  public List<Generator> makeGenerators(CustomType initiatorType, CustomType moduleType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeModuleArtifactGenerators(initiatorType, moduleType);
  }
}
