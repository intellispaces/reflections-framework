package intellispaces.core.annotation.processor.objecthandle;

import com.google.auto.service.AutoService;
import intellispaces.annotations.AnnotatedTypeProcessor;
import intellispaces.annotations.generator.ArtifactGenerator;
import intellispaces.annotations.validator.AnnotatedTypeValidator;
import intellispaces.core.annotation.ObjectHandle;
import intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.javastatements.customtype.CustomType;

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
  public List<ArtifactGenerator> makeArtifactGenerators(CustomType objectHandleType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeObjectHandleArtifactGenerators(objectHandleType);
  }
}
