package tech.intellispacesframework.core.annotation.processor.objecthandle;

import com.google.auto.service.AutoService;
import tech.intellispacesframework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispacesframework.core.annotation.ObjectHandle;
import tech.intellispacesframework.core.annotation.processor.AbstractAnnotationProcessor;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;

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
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType objectHandleType) {
    return List.of(new ObjectHandleImplGenerator(objectHandleType), new MovableObjectHandleImplGenerator(objectHandleType));
  }
}
