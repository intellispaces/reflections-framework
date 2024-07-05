package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.core.annotation.processor.AbstractGuideGenerator;
import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;

import java.util.List;

public class DomainGuideGenerator extends AbstractGuideGenerator {
  private final MethodStatement transitionMethod;

  public DomainGuideGenerator(CustomType domainType, MethodStatement transitionMethod) {
    super(domainType, transitionMethod);
    this.transitionMethod = transitionMethod;
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
    return NameFunctions.getGuideClassCanonicalName(annotatedType.packageName(), annotatedType, transitionMethod);
  }
}
