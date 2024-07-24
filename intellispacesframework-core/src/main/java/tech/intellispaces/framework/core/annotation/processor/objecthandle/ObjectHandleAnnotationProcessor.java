package tech.intellispaces.framework.core.annotation.processor.objecthandle;

import com.google.auto.service.AutoService;
import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeProcessor;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.annotationprocessor.validator.AnnotatedTypeValidator;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.framework.core.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class ObjectHandleAnnotationProcessor extends AnnotatedTypeProcessor {

  public ObjectHandleAnnotationProcessor() {
    super(ObjectHandle.class, Set.of(ElementKind.INTERFACE, ElementKind.CLASS));
  }

  @Override
  protected boolean isApplicable(CustomType objectHandleType) {
    return objectHandleType.isAbstract() && AnnotationProcessorFunctions.isAutoGenerationEnabled(objectHandleType);
  }

  @Override
  protected AnnotatedTypeValidator getValidator() {
    return null;
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType objectHandleType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeObjectHandleArtifactGenerators(objectHandleType);
  }
}
