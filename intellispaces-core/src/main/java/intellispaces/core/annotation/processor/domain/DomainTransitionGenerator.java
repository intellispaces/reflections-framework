package intellispaces.core.annotation.processor.domain;

import intellispaces.actions.common.string.StringActions;
import intellispaces.actions.runner.Runner;
import intellispaces.core.annotation.processor.AbstractTransitionGenerator;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.javastatements.Statement;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodParam;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.NamedReference;
import intellispaces.javastatements.reference.TypeReference;

import java.util.List;

public class DomainTransitionGenerator extends AbstractTransitionGenerator {

  public DomainTransitionGenerator(CustomType domainType, MethodStatement transitionMethod) {
    super(domainType, transitionMethod);
  }

  @Override
  protected String getTransitionClassCanonicalName() {
    return NameConventionFunctions.getTransitionClassCanonicalName(annotatedType.packageName(), annotatedType, transitionMethod);
  }

  @Override
  protected String getTransitionClassTypeParams() {
    if (annotatedType.typeParameters().isEmpty() && transitionMethod.typeParameters().isEmpty()) {
      return "";
    } else if (!annotatedType.typeParameters().isEmpty() && transitionMethod.typeParameters().isEmpty()) {
      return annotatedType.typeParametersFullDeclaration();
    } else if (annotatedType.typeParameters().isEmpty() && !transitionMethod.typeParameters().isEmpty()) {
      return getTransitionMethodTypeParametersDeclaration();
    } else {
      String domainTypeParams = annotatedType.typeParametersFullDeclaration();
      String transitionTypeParams = getTransitionMethodTypeParametersDeclaration();
      return domainTypeParams.substring(0, domainTypeParams.length() - 1) +
          ", " +
          transitionTypeParams.substring(1);
    }
  }

  private String getTransitionMethodTypeParametersDeclaration() {
    var sb = new StringBuilder();
    sb.append("<");

    boolean first = true;
    for (NamedReference typeParam : transitionMethod.typeParameters()) {
      if (!first) {
        sb.append(", ");
      }
      sb.append(typeParam.actualDeclaration());
      first = false;
    }

    sb.append(">");
    return sb.toString();
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
      Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
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
  protected TypeReference getTargetType() {
    return transitionMethod.returnType().orElseThrow();
  }

  @Override
  protected List<TypeReference> getQualifierTypes() {
    return transitionMethod.params().stream()
        .map(MethodParam::type)
        .toList();
  }
}
