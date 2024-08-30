package intellispaces.core.annotation.processor.domain;

import intellispaces.annotations.context.AnnotationProcessingContext;
import intellispaces.core.annotation.processor.AbstractGuideGenerationTask;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.traverse.TraverseType;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodParam;
import intellispaces.javastatements.method.MethodStatement;

import java.util.List;

public class DomainGuideGenerationTask extends AbstractGuideGenerationTask {

  public DomainGuideGenerationTask(
      TraverseType traverseType, CustomType initiatorType, CustomType domainType, MethodStatement transitionMethod
  ) {
    super(traverseType, initiatorType, domainType, transitionMethod);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  protected CustomType getDomainType() {
    return annotatedType;
  }

  @Override
  protected String getGuideTypeParamDeclaration() {
    return DomainGenerationFunctions.getTransitionTypeParams(annotatedType, transitionMethod);
  }

  @Override
  protected List<MethodParam> getQualifierMethodParams() {
    return transitionMethod.params();
  }

  @Override
  protected String getGuideClassCanonicalName() {
    return NameConventionFunctions.getGuideClassCanonicalName(
        annotatedType.packageName(), annotatedType, transitionMethod
    );
  }
}
