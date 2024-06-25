package tech.intellispaces.framework.core.annotation.processor.objecthandle;

import com.google.auto.service.AutoService;
import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeValidator;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.core.annotation.processor.AbstractAnnotationProcessor;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class ObjectHandleAnnotationProcessor extends AbstractAnnotationProcessor {

  public ObjectHandleAnnotationProcessor() {
    super(ObjectHandle.class, Set.of(ElementKind.INTERFACE, ElementKind.CLASS));
  }

  @Override
  protected boolean isApplicable(CustomType objectHandleType) {
    return objectHandleType.isAbstract() && isAutoGenerationEnabled(objectHandleType);
  }

  @Override
  protected AnnotatedTypeValidator getValidator() {
    return null;
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType objectHandleType) {
    return List.of(new ObjectHandleImplGenerator(objectHandleType), new ObjectHandleMovableImplGenerator(objectHandleType));
  }
}
