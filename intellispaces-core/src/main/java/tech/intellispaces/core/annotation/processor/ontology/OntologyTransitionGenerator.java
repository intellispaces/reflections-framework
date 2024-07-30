package tech.intellispaces.core.annotation.processor.ontology;

import tech.intellispaces.commons.type.TypeFunctions;
import tech.intellispaces.core.annotation.processor.AbstractTransitionGenerator;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.javastatements.Statement;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodParam;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.TypeReference;

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
    String simpleName = NameConventionFunctions.getDefaultTransitionClassSimpleName(transitionMethod);
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
