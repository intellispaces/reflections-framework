package tech.intellispaces.framework.core.annotation.processor.ontology;

import tech.intellispaces.framework.commons.string.StringFunctions;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.processor.AbstractTransitionGenerator;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;

public class OntologyTransitionGenerator extends AbstractTransitionGenerator {

  public OntologyTransitionGenerator(CustomType ontologyType, MethodStatement transitionMethod) {
    super(ontologyType, transitionMethod);
  }

  @Override
  protected String getTransitionMethodSignature() {
    return buildMethodSignature(transitionMethod);
  }

  @Override
  protected String getTransitionClassCanonicalName() {
    String packageName = TypeFunctions.getPackageName(annotatedType.canonicalName());
    String simpleName = StringFunctions.capitalizeFirstLetter(transitionMethod.name()) + "Transition";
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }
}
