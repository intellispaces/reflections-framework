package tech.intellispacesframework.core.annotation.processor;

import com.google.auto.service.AutoService;
import tech.intellispacesframework.annotationprocessor.AnnotatedTypeProcessor;
import tech.intellispacesframework.annotationprocessor.maker.ArtifactMaker;
import tech.intellispacesframework.core.annotation.AutoGeneration;
import tech.intellispacesframework.core.annotation.Domain;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class DomainAnnotationProcessor extends AnnotatedTypeProcessor {

  public DomainAnnotationProcessor() {
    super(Domain.class, Set.of(ElementKind.INTERFACE));
  }

  @Override
  protected boolean isApplicable(CustomType annotatedType) {
    return annotatedType.selectAnnotation(AutoGeneration.class).map(AutoGeneration::enabled).orElse(true);
  }

  @Override
  protected List<ArtifactMaker> getArtifactMakers(CustomType annotatedType) {
    return List.of(new ObjectHandleMaker());
  }
}
