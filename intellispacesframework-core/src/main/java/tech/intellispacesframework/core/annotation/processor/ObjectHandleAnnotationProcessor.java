package tech.intellispacesframework.core.annotation.processor;

import com.google.auto.service.AutoService;
import tech.intellispacesframework.annotationprocessor.AnnotatedTypeProcessor;
import tech.intellispacesframework.annotationprocessor.maker.ArtifactMaker;
import tech.intellispacesframework.core.annotation.ObjectHandle;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class ObjectHandleAnnotationProcessor extends AnnotatedTypeProcessor {

  public ObjectHandleAnnotationProcessor() {
    super(ObjectHandle.class, Set.of(ElementKind.INTERFACE, ElementKind.CLASS));
  }

  @Override
  protected boolean isApplicable(CustomType annotatedType) {
    return annotatedType.isAbstract();
  }

  @Override
  protected List<ArtifactMaker> getArtifactMakers(CustomType annotatedType) {
    return List.of(new ObjectHandleImplMaker());
  }
}
