package intellispaces.framework.core.annotation.processor.domain;

import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.framework.core.annotation.processor.AbstractGuideGenerationTask;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.traverse.TraverseType;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;

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
