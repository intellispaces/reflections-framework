package tech.intellispaces.core.annotation.processor.domain;

import tech.intellispaces.core.annotation.processor.AbstractGuideGenerator;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.traverse.TraverseType;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodParam;
import tech.intellispaces.javastatements.method.MethodStatement;

import java.util.List;

public class DomainGuideGenerator extends AbstractGuideGenerator {

  public DomainGuideGenerator(TraverseType traverseType, CustomType domainType, MethodStatement transitionMethod) {
    super(traverseType, domainType, transitionMethod);
  }

  @Override
  protected CustomType getDomainType() {
    return annotatedType;
  }

  @Override
  protected List<MethodParam> getQualifierMethodParams() {
    return transitionMethod.params();
  }

  @Override
  protected String getGuideClassCanonicalName() {
    return NameConventionFunctions.getGuideClassCanonicalName(
        traverseType, annotatedType.packageName(), annotatedType, transitionMethod
    );
  }
}
