package intellispaces.framework.core.annotation.processor.ontology;

import intellispaces.common.action.runner.Runner;
import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.text.TextActions;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.JavaStatements;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.NamedReference;
import intellispaces.common.javastatement.reference.NamedTypes;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.processor.AbstractGuideGenerator;
import intellispaces.framework.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.traverse.TraverseType;

import java.util.List;
import java.util.function.Function;

public class OntologyGuideGenerator extends AbstractGuideGenerator {
  private final CustomType domainType;
  private final List<MethodParam> qualifierMethodParams;

  public OntologyGuideGenerator(
      TraverseType traverseType, CustomType initiatorType, CustomType ontologyType, MethodStatement channelMethod
  ) {
    super(traverseType, initiatorType, ontologyType, channelMethod);
    this.domainType = getDomainTypeInternal();
    this.qualifierMethodParams = getQualifierMethodParamsInternal();
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  protected CustomType getDomainType() {
    return domainType;
  }

  @Override
  protected String getGuideTypeParamDeclaration() {
    List<NamedReference> typeParams = channelMethod.typeParameters();
    if (typeParams.isEmpty()) {
      return "";
    }

    var sb = new StringBuilder();
    sb.append("<");
    Runner commaAppender = TextActions.skippingFirstTimeCommaAppender(sb);
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
        annotatedType.packageName(), annotatedType, channelMethod
    );
  }

  private CustomType getDomainTypeInternal() {
    TypeReference reference = channelMethod.params().get(0).type();
    if (reference.isPrimitiveReference()) {
      return JavaStatements.customTypeStatement(
          TypeFunctions.getPrimitiveWrapperClass(reference.asPrimitiveReferenceOrElseThrow().typename()));
    } else if (reference.isCustomTypeReference()) {
      return reference.asCustomTypeReferenceOrElseThrow().targetType();
    } else {
      throw UnexpectedViolationException.withMessage("Unsupported source domain type: {0}", reference);
    }
  }

  private List<MethodParam> getQualifierMethodParamsInternal() {
    return channelMethod.params().subList(1, channelMethod.params().size());
  }

  @Override
  protected String buildGuideMethod() {
    if (channelMethod.typeParameters().isEmpty()) {
      return super.buildGuideMethod(Function.identity());
    } else {
      var sb = new StringBuilder();
      sb.append("<");
      for (NamedReference param : channelMethod.typeParameters()) {
        sb.append("_").append(param.name()).append(" extends ").append(param.name());
      }
      sb.append("> ");
      if (AnnotationProcessorFunctions.isVoidType(channelMethod.returnType().orElseThrow())) {
        sb.append("void");
      } else {
        sb.append(buildTargetObjectHandleDeclaration(this::replaceType));
      }
      sb.append(" ");
      sb.append(channelMethod.name());
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
