package tech.intellispaces.jaquarius.annotation.processor.domain;

import tech.intellispaces.jaquarius.annotation.processor.AbstractChannelGenerator;
import tech.intellispaces.jaquarius.common.NameConventionFunctions;
import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.java.annotation.context.AnnotationProcessingContext;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.TypeReference;
import tech.intellispaces.java.reflection.method.MethodParam;

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
      RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
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
