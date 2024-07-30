package tech.intellispaces.core.annotation.processor.ontology;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.commons.type.TypeFunctions;
import tech.intellispaces.core.annotation.processor.AbstractGuideGenerator;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.traverse.TraverseType;
import tech.intellispaces.javastatements.JavaStatements;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodParam;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.TypeReference;

import java.util.List;

public class OntologyGuideGenerator extends AbstractGuideGenerator {
  private final CustomType domainType;
  private final List<MethodParam> qualifierMethodParams;

  public OntologyGuideGenerator(
      TraverseType traverseType, CustomType ontologyType, MethodStatement transitionMethod
  ) {
    super(traverseType, ontologyType, transitionMethod);
    this.domainType = getDomainTypeInternal();
    this.qualifierMethodParams = getQualifierMethodParamsInternal();
  }

  @Override
  protected CustomType getDomainType() {
    return domainType;
  }

  @Override
  protected List<MethodParam> getQualifierMethodParams() {
    return qualifierMethodParams;
  }

  @Override
  protected String getGuideClassCanonicalName() {
    return NameConventionFunctions.getGuideClassCanonicalName(
        traverseType, annotatedType.packageName(), annotatedType, transitionMethod
    );
  }

  private CustomType getDomainTypeInternal() {
    TypeReference reference = transitionMethod.params().get(0).type();
    if (reference.isPrimitiveReference()) {
      return JavaStatements.customTypeStatement(
          TypeFunctions.getPrimitiveWrapperClass(reference.asPrimitiveReferenceOrElseThrow().typename()));
    } else if (reference.isCustomTypeReference()) {
      return reference.asCustomTypeReferenceOrElseThrow().targetType();
    } else {
      throw UnexpectedViolationException.withMessage("Unsupported source domain type: {}", reference);
    }
  }

  private List<MethodParam> getQualifierMethodParamsInternal() {
    return transitionMethod.params().subList(1, transitionMethod.params().size());
  }
}
