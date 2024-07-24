package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.commons.action.Executor;
import tech.intellispaces.framework.commons.action.string.StringActions;
import tech.intellispaces.framework.commons.string.StringFunctions;
import tech.intellispaces.framework.core.annotation.processor.AbstractTransitionGenerator;
import tech.intellispaces.framework.core.common.NameConventionFunctions;
import tech.intellispaces.framework.javastatements.statement.Statement;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.NamedTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

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
    for (NamedTypeReference typeParam : transitionMethod.typeParameters()) {
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
      for (NamedTypeReference typeParam : annotatedType.typeParameters()) {
        commaAppender.execute();
        sb.append(typeParam.formalBriefDeclaration());
      }
      sb.append(">");
    }
    sb.append(" ").append(StringFunctions.lowercaseFirstLetter(annotatedType.simpleName()));
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
