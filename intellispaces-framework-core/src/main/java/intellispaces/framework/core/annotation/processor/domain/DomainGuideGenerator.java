package intellispaces.framework.core.annotation.processor.domain;

import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.framework.core.annotation.processor.AbstractGuideGenerator;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.traverse.TraverseType;

import java.util.List;

public class DomainGuideGenerator extends AbstractGuideGenerator {

  public DomainGuideGenerator(
      TraverseType traverseType, CustomType initiatorType, CustomType domainType, MethodStatement channelMethod
  ) {
    super(traverseType, initiatorType, domainType, channelMethod);
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
    return DomainGenerationFunctions.getChannelTypeParams(annotatedType, channelMethod);
  }

  @Override
  protected List<MethodParam> getQualifierMethodParams() {
    return channelMethod.params();
  }

  @Override
  protected String getGuideClassCanonicalName() {
    return NameConventionFunctions.getGuideClassCanonicalName(
        annotatedType.packageName(), annotatedType, channelMethod
    );
  }
}
