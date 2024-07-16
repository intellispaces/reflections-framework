package tech.intellispaces.framework.core.annotation.processor.ontology;

import tech.intellispaces.framework.commons.string.StringFunctions;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.processor.AbstractTransitionGenerator;
import tech.intellispaces.framework.javastatements.statement.Statement;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import java.util.List;
import java.util.stream.Collectors;

public class OntologyTransitionGenerator extends AbstractTransitionGenerator {

  public OntologyTransitionGenerator(CustomType ontologyType, MethodStatement transitionMethod) {
    super(ontologyType, transitionMethod);
  }

  @Override
  protected String getTransitionClassTypeParams() {
    if (transitionMethod.typeParameters().isEmpty()) {
      return "";
    }
    return "<" + transitionMethod.typeParameters().stream()
        .map(p -> p.actualDeclaration(context::addToImportAndGetSimpleName))
        .collect(Collectors.joining(", ")) + ">";
  }

  @Override
  protected String getTransitionMethodSignature() {
    return buildMethodSignature(
        transitionMethod,
        transitionMethod.name(),
        false,
        false,
        List.of()
    );
  }

  @Override
  protected String getTransitionClassCanonicalName() {
    String packageName = TypeFunctions.getPackageName(annotatedType.canonicalName());
    String simpleName = StringFunctions.capitalizeFirstLetter(transitionMethod.name()) + "Transition";
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  @Override
  protected Statement getSourceType() {
    return transitionMethod.params().get(0).type();
  }

  @Override
  protected TypeReference getTargetType() {
    return transitionMethod.returnType().orElseThrow();
  }

  @Override
  protected List<TypeReference> getQualifierTypes() {
    return transitionMethod.params().subList(1, transitionMethod.params().size()).stream()
        .map(MethodParam::type)
        .toList();
  }
}
