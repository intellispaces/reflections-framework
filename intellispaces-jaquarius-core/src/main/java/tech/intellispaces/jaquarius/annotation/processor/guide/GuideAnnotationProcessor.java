package tech.intellispaces.jaquarius.annotation.processor.guide;

import com.google.auto.service.AutoService;
import tech.intellispaces.jaquarius.annotation.Guide;
import tech.intellispaces.jaquarius.annotation.processor.AnnotationProcessorFunctions;
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
public class GuideAnnotationProcessor extends AnnotatedTypeProcessor {

  public GuideAnnotationProcessor() {
    super(Guide.class, Set.of(ElementKind.INTERFACE));
  }

  @Override
  public boolean isApplicable(CustomType moduleType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(moduleType);
  }

  @Override
  public AnnotatedTypeValidator getValidator() {
    return null;
  }

  @Override
  public List<Generator> makeGenerators(CustomType initiatorType, CustomType guideType, RoundEnvironment roundEnv) {
    return List.of(new AutoGuideGenerator(initiatorType, guideType));
  }
}
