package tech.intellispaces.framework.core.annotation.processor.ontology;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.processor.AbstractGuideGenerator;
import tech.intellispaces.framework.core.common.NameConventionFunctions;
import tech.intellispaces.framework.core.traverse.TraverseType;
import tech.intellispaces.framework.javastatements.JavaStatements;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

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
    if (reference.isPrimitive()) {
      return JavaStatements.customTypeStatement(
          TypeFunctions.getPrimitiveWrapperClass(reference.asPrimitiveTypeReferenceSurely().typename()));
    } else if (reference.isCustomTypeReference()) {
      return reference.asCustomTypeReferenceSurely().targetType();
    } else {
      throw UnexpectedViolationException.withMessage("Unsupported source domain type: {}", reference);
    }
  }

  private List<MethodParam> getQualifierMethodParamsInternal() {
    return transitionMethod.params().subList(1, transitionMethod.params().size());
  }
}
