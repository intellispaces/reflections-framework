package intellispaces.framework.core.annotation.processor.domain;

import intellispaces.common.action.runner.Runner;
import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.base.text.TextActions;
import intellispaces.common.javastatement.Statement;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.NamedReference;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.processor.AbstractTransitionGenerator;
import intellispaces.framework.core.common.NameConventionFunctions;

import java.util.List;

public class DomainTransitionGenerator extends AbstractTransitionGenerator {

  public DomainTransitionGenerator(
      CustomType initiatorType, CustomType domainType, MethodStatement transitionMethod
  ) {
    super(initiatorType, domainType, transitionMethod);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  protected String getTransitionClassCanonicalName() {
    return NameConventionFunctions.getTransitionClassCanonicalName(annotatedType.packageName(), annotatedType, transitionMethod);
  }

  @Override
  protected String getTransitionClassTypeParams() {
    return DomainGenerationFunctions.getTransitionTypeParams(annotatedType, transitionMethod);
  }

  @Override
  protected String getTransitionMethodSignature() {
    return buildMethodSignature(transitionMethod, List.of(getSourceParamDeclaration()));
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
  protected Statement getSourceType() {
    return annotatedType;
  }

  @Override
  protected TypeReference getResultType() {
    return transitionMethod.returnType().orElseThrow();
  }

  @Override
  protected List<TypeReference> getQualifierTypes() {
    return transitionMethod.params().stream()
        .map(MethodParam::type)
        .toList();
  }
}
