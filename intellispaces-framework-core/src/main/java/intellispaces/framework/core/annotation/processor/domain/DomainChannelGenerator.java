package intellispaces.framework.core.annotation.processor.domain;

import intellispaces.common.action.runner.Runner;
import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.base.text.TextActions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.NamedReference;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.processor.AbstractChannelGenerator;
import intellispaces.framework.core.common.NameConventionFunctions;

import java.util.List;

public class DomainChannelGenerator extends AbstractChannelGenerator {

  public DomainChannelGenerator(
      CustomType initiatorType, CustomType domainType, MethodStatement channelMethod
  ) {
    super(initiatorType, domainType, channelMethod);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  protected String getChannelClassCanonicalName() {
    return NameConventionFunctions.getChannelClassCanonicalName(annotatedType.packageName(), annotatedType, channelMethod);
  }

  @Override
  protected String getChannelMethodSignature() {
    return buildMethodSignature(channelMethod, List.of(getSourceParamDeclaration()));
  }

  private String getSourceParamDeclaration() {
    var sb = new StringBuilder();
    sb.append(annotatedType.simpleName());
    if (!annotatedType.typeParameters().isEmpty()) {
      sb.append("<");
      Runner commaAppender = TextActions.skippingFirstTimeCommaAppender(sb);
      for (NamedReference typeParam : annotatedType.typeParameters()) {
        commaAppender.run();
        sb.append(typeParam.formalBriefDeclaration());
      }
      sb.append(">");
    }
    sb.append(" source");
    return sb.toString();
  }

  @Override
  protected List<TypeReference> getQualifierTypes() {
    return channelMethod.params().stream()
        .map(MethodParam::type)
        .toList();
  }
}
