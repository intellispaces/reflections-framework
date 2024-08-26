package intellispaces.core.annotation.processor.ontology;

import intellispaces.actions.common.string.StringActions;
import intellispaces.actions.runner.Runner;
import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.commons.type.TypeFunctions;
import intellispaces.core.annotation.processor.AbstractGuideGenerator;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.traverse.TraverseType;
import intellispaces.javastatements.JavaStatements;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodParam;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.NamedReference;
import intellispaces.javastatements.reference.TypeReference;

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
  protected String getGuideTypeParamDeclaration() {
    List<NamedReference> typeParams = transitionMethod.typeParameters();
    if (typeParams.isEmpty()) {
      return "";
    }

    var sb = new StringBuilder();
    sb.append("<");
    Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
    for (NamedReference typeParam : typeParams) {
      commaAppender.run();
      sb.append(typeParam.formalFullDeclaration());
    }
    sb.append(">");
    return sb.toString();
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
