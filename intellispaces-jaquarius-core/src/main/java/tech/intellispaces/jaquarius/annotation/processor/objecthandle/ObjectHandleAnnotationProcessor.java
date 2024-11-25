package tech.intellispaces.jaquarius.annotation.processor.objecthandle;

import com.google.auto.service.AutoService;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
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
public class ObjectHandleAnnotationProcessor extends AnnotatedTypeProcessor {

  public ObjectHandleAnnotationProcessor() {
    super(ObjectHandle.class, Set.of(ElementKind.CLASS));
  }

  @Override
  public boolean isApplicable(CustomType objectHandleType) {
    return objectHandleType.isAbstract() && AnnotationProcessorFunctions.isAutoGenerationEnabled(objectHandleType);
  }

  @Override
  public AnnotatedTypeValidator getValidator() {
    return null;
  }

  @Override
  public List<Generator> makeGenerators(
      CustomType initiatorType, CustomType objectHandleType, RoundEnvironment roundEnv
  ) {
    return AnnotationProcessorFunctions.makeObjectHandleArtifactGenerators(initiatorType, objectHandleType);
  }
}
