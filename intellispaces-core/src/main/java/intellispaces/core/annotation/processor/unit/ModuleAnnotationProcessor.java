package intellispaces.core.annotation.processor.unit;

import com.google.auto.service.AutoService;
import intellispaces.annotations.AnnotatedTypeProcessor;
import intellispaces.annotations.generator.ArtifactGenerator;
import intellispaces.annotations.validator.AnnotatedTypeValidator;
import intellispaces.core.annotation.Module;
import intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.core.validation.ModuleValidator;
import intellispaces.javastatements.customtype.CustomType;

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
  public List<ArtifactGenerator> makeArtifactGenerators(CustomType moduleType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeModuleArtifactGenerators(moduleType);
  }
}
