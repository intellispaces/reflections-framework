package tech.intellispaces.core.annotation.processor.domain;

import tech.intellispaces.actions.common.string.StringActions;
import tech.intellispaces.actions.executor.Executor;
import tech.intellispaces.core.annotation.processor.AbstractTransitionGenerator;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.javastatements.Statement;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodParam;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.NamedReference;
import tech.intellispaces.javastatements.reference.TypeReference;

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
      Executor commaAppender = StringActions.commaAppender(sb);
      for (NamedReference typeParam : annotatedType.typeParameters()) {
        commaAppender.execute();
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
