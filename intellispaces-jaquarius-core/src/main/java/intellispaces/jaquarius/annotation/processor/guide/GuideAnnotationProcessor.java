package intellispaces.jaquarius.annotation.processor.guide;

import com.google.auto.service.AutoService;
import intellispaces.common.annotationprocessor.AnnotatedTypeProcessor;
import intellispaces.common.annotationprocessor.generator.Generator;
import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.jaquarius.annotation.Guide;
import intellispaces.jaquarius.annotation.processor.AnnotationProcessorFunctions;

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
