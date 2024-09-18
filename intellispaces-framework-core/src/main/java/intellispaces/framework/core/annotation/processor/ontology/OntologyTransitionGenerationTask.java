package intellispaces.framework.core.annotation.processor.ontology;

import intellispaces.common.annotationprocessor.context.AnnotationProcessingContext;
import intellispaces.common.javastatement.Statement;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.processor.AbstractTransitionGenerationTask;
import intellispaces.framework.core.common.NameConventionFunctions;

import java.util.List;
import java.util.stream.Collectors;

public class OntologyTransitionGenerationTask extends AbstractTransitionGenerationTask {

  public OntologyTransitionGenerationTask(CustomType initiatorType, CustomType ontologyType, MethodStatement transitionMethod) {
    super(initiatorType, ontologyType, transitionMethod);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
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
    return NameConventionFunctions.getTransitionClassCanonicalName(transitionMethod);
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
