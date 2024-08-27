package intellispaces.core.annotation.processor.domain;

import intellispaces.core.annotation.processor.AbstractGuideGenerator;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.traverse.TraverseType;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodParam;
import intellispaces.javastatements.method.MethodStatement;

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
  protected String getGuideTypeParamDeclaration() {
    return DomainGeneratorFunctions.getTransitionTypeParams(annotatedType, transitionMethod);
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
