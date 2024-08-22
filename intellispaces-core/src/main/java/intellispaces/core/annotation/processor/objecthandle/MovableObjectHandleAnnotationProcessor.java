package intellispaces.core.annotation.processor.objecthandle;

import com.google.auto.service.AutoService;
import intellispaces.annotations.AnnotatedTypeProcessor;
import intellispaces.annotations.generator.ArtifactGenerator;
import intellispaces.annotations.validator.AnnotatedTypeValidator;
import intellispaces.core.annotation.MovableObjectHandle;
import intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.javastatements.customtype.CustomType;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class MovableObjectHandleAnnotationProcessor extends AnnotatedTypeProcessor {

  public MovableObjectHandleAnnotationProcessor() {
    super(MovableObjectHandle.class, Set.of(ElementKind.INTERFACE, ElementKind.CLASS));
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
    return AnnotationProcessorFunctions.makeMovableObjectHandleArtifactGenerators(objectHandleType);
  }
}
