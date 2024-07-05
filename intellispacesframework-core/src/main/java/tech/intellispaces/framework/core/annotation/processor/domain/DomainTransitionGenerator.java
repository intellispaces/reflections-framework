package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.commons.action.Action;
import tech.intellispaces.framework.commons.string.StringFunctions;
import tech.intellispaces.framework.core.annotation.processor.AbstractTransitionGenerator;
import tech.intellispaces.framework.core.common.ActionFunctions;
import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.NamedTypeReference;

import java.util.List;

public class DomainTransitionGenerator extends AbstractTransitionGenerator {

  public DomainTransitionGenerator(CustomType domainType, MethodStatement transitionMethod) {
    super(domainType, transitionMethod);
  }

  @Override
  protected String getTransitionClassCanonicalName() {
    return NameFunctions.getTransitionClassCanonicalName(annotatedType.packageName(), annotatedType, transitionMethod);
  }

  @Override
  protected String getTransitionMethodSignature() {
    return buildMethodSignature(transitionMethod, true, List.of(getSourceParamDeclaration()));
  }

  private String getSourceParamDeclaration() {
    var sb = new StringBuilder();
    sb.append(annotatedType.simpleName());
    if (!annotatedType.typeParameters().isEmpty()) {
      sb.append("<");
      Action addCommaAction = ActionFunctions.buildAppendSeparatorAction(sb, ", ");
      for (NamedTypeReference typeParam : annotatedType.typeParameters()) {
        addCommaAction.execute();
        sb.append(typeParam.formalBriefDeclaration());
      }
      sb.append(">");
    }
    sb.append(" ").append(StringFunctions.lowercaseFirstLetter(annotatedType.simpleName()));
    return sb.toString();
  }
}
