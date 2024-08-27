package intellispaces.core.annotation.processor.ontology;

import intellispaces.actions.common.string.StringActions;
import intellispaces.actions.runner.Runner;
import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.commons.type.TypeFunctions;
import intellispaces.core.annotation.processor.AbstractGuideGenerator;
import intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.traverse.TraverseType;
import intellispaces.javastatements.JavaStatements;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodParam;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.NamedReference;
import intellispaces.javastatements.reference.NamedTypes;
import intellispaces.javastatements.reference.TypeReference;

import java.util.List;
import java.util.function.Function;

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

  @Override
  protected String buildGuideMethod() {
    if (transitionMethod.typeParameters().isEmpty()) {
      return super.buildGuideMethod(Function.identity());
    } else {
      var sb = new StringBuilder();
      sb.append("<");
      for (NamedReference param : transitionMethod.typeParameters()) {
        sb.append("_").append(param.name()).append(" extends ").append(param.name());
      }
      sb.append("> ");
      if (AnnotationProcessorFunctions.isVoidType(transitionMethod.returnType().orElseThrow())) {
        sb.append("void");
      } else {
        sb.append(buildTargetObjectHandleDeclaration(this::replaceType));
      }
      sb.append(" ");
      sb.append(transitionMethod.name());
      sb.append("(");
      sb.append(buildSourceObjectHandleDeclaration(this::replaceType));
      sb.append(" source");
      for (MethodParam param : getQualifierMethodParams()) {
        sb.append(", ");
        sb.append(buildObjectHandleDeclaration(param.type(), this::replaceType));
        sb.append(" ");
        sb.append(param.name());
      }
      sb.append(");");
      return sb.toString();

    }
  }

  private TypeReference replaceType(TypeReference type) {
    if (type.isNamedReference()) {
      return NamedTypes.build(type.asNamedReferenceOrElseThrow())
          .name("_" + type.asNamedReferenceOrElseThrow().name())
          .build();
    }
    return type;
  }
}
