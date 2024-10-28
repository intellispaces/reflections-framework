package intellispaces.jaquarius.processor.unit;

import com.google.auto.service.AutoService;
import intellispaces.common.annotationprocessor.AnnotatedTypeProcessor;
import intellispaces.common.annotationprocessor.generator.Generator;
import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.jaquarius.annotation.Module;
import intellispaces.jaquarius.processor.AnnotationProcessorFunctions;
import intellispaces.jaquarius.validation.ModuleValidator;

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
